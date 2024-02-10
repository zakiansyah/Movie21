package com.dicoding.movie21.di

import com.dicoding.movie21.core.domain.usecase.MovieInteractor
import com.dicoding.movie21.core.domain.usecase.MovieUseCase
import com.dicoding.movie21.detail.DetailViewModel
import com.dicoding.movie21.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<MovieUseCase>{ MovieInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }

}