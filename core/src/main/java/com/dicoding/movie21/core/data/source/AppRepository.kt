package com.dicoding.movie21.core.data.source

import com.dicoding.movie21.core.data.source.local.LocalDataSource
import com.dicoding.movie21.core.data.source.remote.RemoteDataSource
import com.dicoding.movie21.core.data.source.remote.network.Result
import com.dicoding.movie21.core.data.source.remote.response.MovieResponse
import com.dicoding.movie21.core.domain.repository.IMovieRepository
import com.dicoding.movie21.core.utils.AppExecutors
import com.dicoding.movie21.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IMovieRepository {

    override fun getMovie(): Flow<Resource<List<com.dicoding.movie21.core.domain.model.Movie>>> =
        object : NetworkBoundResource<List<com.dicoding.movie21.core.domain.model.Movie>, List<MovieResponse>>() {
            override fun loadFromDB(): Flow<List<com.dicoding.movie21.core.domain.model.Movie>> {
                return localDataSource.getAllMovie().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override suspend fun createCall(): Flow<Result<List<MovieResponse>>> =
                remoteDataSource.getAllMovie()


            override suspend fun saveCallResult(data: List<MovieResponse>) {
                val movieList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertMovie(movieList)
            }

            override fun shouldFetch(data: List<com.dicoding.movie21.core.domain.model.Movie>?): Boolean = true

        }.asFlow()



    override fun getFavoriteMovie(): Flow<List<com.dicoding.movie21.core.domain.model.Movie>> {
        return localDataSource.getFavoriteMovie().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun setFavoriteMovie(movie: com.dicoding.movie21.core.domain.model.Movie, state: Boolean) {
        val movieEntity = DataMapper.mapDomainToEntity(movie)
        appExecutors.diskIO().execute { localDataSource.setFavoriteMovie(movieEntity, state) }
    }

}