package com.dicoding.movie21.detail

import androidx.lifecycle.ViewModel
import com.dicoding.movie21.core.domain.model.Movie
import com.dicoding.movie21.core.domain.usecase.MovieUseCase

class DetailViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {
    fun setFavoriteMovie(movie: Movie, newStatus:Boolean) =
        movieUseCase.setFavoriteMovie(movie, newStatus)

}