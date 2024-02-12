package com.dicoding.movie21.core.domain.usecase

import com.dicoding.movie21.core.domain.model.Movie
import com.dicoding.movie21.core.domain.repository.IMovieRepository

class MovieInteractor(private val iMovieRespository: IMovieRepository) : MovieUseCase {

    override fun getMovie () = iMovieRespository.getMovie()
    override fun getFavoriteMovie() = iMovieRespository.getFavoriteMovie()
    override fun setFavoriteMovie(dataDetail: Movie, state: Boolean) =iMovieRespository.setFavoriteMovie(dataDetail,state)
}