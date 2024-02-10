package com.dicoding.movie21.core.utils

import com.dicoding.movie21.core.data.source.local.entity.MovieEntity
import com.dicoding.movie21.core.data.source.remote.response.MovieResponse
import com.dicoding.movie21.core.domain.model.Movie

object DataMapper {
    fun mapResponsesToEntities(input: List<MovieResponse>): List<MovieEntity> {
        val movieList = ArrayList<MovieEntity>()
        input.map {
            val movie = MovieEntity(
                overview = it.overview,
                title = it.title,
                posterPath = it.posterPath,
                backdropPath = it.backdropPath,
                releaseDate = it.releaseDate,
                id = it.id,
                isFavorite = false
            )
            movieList.add(movie)
        }
        return movieList
    }

    fun mapEntitiesToDomain(input: List<MovieEntity>): List<Movie> =
        input.map {
            Movie(
                overview = it.overview,
                title = it.title,
                posterPath = it.posterPath,
                backdropPath = it.backdropPath,
                releaseDate = it.releaseDate,
                id = it.id,
                isFavorite = it.isFavorite

            )
        }

    fun mapDomainToEntity(input: Movie) = MovieEntity(
        overview = input.overview,
        title = input.title,
        posterPath = input.posterPath,
        backdropPath = input.backdropPath,
        releaseDate = input.releaseDate,
        id = input.id,
        isFavorite = input.isFavorite
    )
}