package com.dicoding.movie21.core.data.source.local

import com.dicoding.movie21.core.data.source.local.entity.MovieEntity
import com.dicoding.movie21.core.data.source.local.room.MovieDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val favDao: MovieDao) {
    fun getAllMovie(): Flow<List<MovieEntity>> = favDao.getAllMovie()
    fun getFavoriteMovie(): Flow<List<MovieEntity>> = favDao.getFavoriteMovie()
    suspend fun insertMovie(movieList: List<MovieEntity>) =favDao.insertMovie(movieList)
    fun setFavoriteMovie(movie: MovieEntity, newState: Boolean) {
        movie.isFavorite = newState
        favDao.updateFavoriteMovie(movie)
    }
}