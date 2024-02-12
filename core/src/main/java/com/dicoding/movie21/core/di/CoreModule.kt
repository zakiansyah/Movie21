package com.dicoding.movie21.core.di

import android.util.Log
import androidx.room.Room
import com.dicoding.movie21.core.data.source.AppRepository
import com.dicoding.movie21.core.data.source.local.LocalDataSource
import com.dicoding.movie21.core.data.source.local.room.MovieDatabase
import com.dicoding.movie21.core.data.source.remote.RemoteDataSource
import com.dicoding.movie21.core.data.source.remote.network.ApiService
import com.dicoding.movie21.core.domain.repository.IMovieRepository
import com.dicoding.movie21.core.utils.AppExecutors
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val databaseModule = module {
    factory { get<MovieDatabase>().movieDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("movie21".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            MovieDatabase::class.java,"popular_db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single { AppExecutors() }
    single<IMovieRepository> {
        AppRepository(
            get(),
            get(),
            get()
        )
    }
}

val networkModule = module {
    single { AuthenticationInterceptor(apiKey = "e1b33ad4fd8ef57d26d4a2e302c7cdff") }
    single {
        val hostName = "api.themoviedb.org"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostName, "sha256/5VLcahb6x4EvvFrCF2TePZulWqrLHS2jCg9Ywv6JHog=")
            .add(hostName, "sha256/vxRon/El5KuI4vx5ey1DgmsYmRY0nDd5Cg4GfJ8S+bg=")
            .add(hostName, "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI=")
            .add(hostName, "sha256/KwccWaCgrnaw6tsrrSO61FgLacNgG2MMLq8GE6+oP5I=")
            .build()
        OkHttpClient.Builder()
            .addInterceptor(get<AuthenticationInterceptor>())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

class AuthenticationInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        val urlWithApiKey = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()

        Log.d("API_LOG", "URL with API Key: ${urlWithApiKey.toString()}")

        val requestBuilder = originalRequest.newBuilder().url(urlWithApiKey)
        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}
