package com.dicoding.movie21.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.movie21.core.domain.usecase.MovieUseCase

class HomeViewModel( movieUseCase: MovieUseCase) : ViewModel() {
    val movie = movieUseCase.getMovie().asLiveData()
}