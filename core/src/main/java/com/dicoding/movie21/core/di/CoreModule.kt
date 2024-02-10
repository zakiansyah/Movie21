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
        Room.databaseBuilder(
            androidContext(),
            MovieDatabase::class.java,"popular_db"
        ).fallbackToDestructiveMigration().build()
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
        OkHttpClient.Builder()
            .addInterceptor(get<AuthenticationInterceptor>())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
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
