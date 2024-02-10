package com.dicoding.movie21.core.domain.repository

import com.dicoding.movie21.core.data.source.Resource
import com.dicoding.movie21.core.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun getMovie(): Flow<Resource<List<Movie>>>

    fun getFavoriteMovie(): Flow<List<Movie>>

    fun setFavoriteMovie(movie: Movie, state: Boolean)

}