package com.dicoding.movie21.core.data.source.remote.network

import com.dicoding.movie21.core.data.source.remote.response.ListMovieResponse
import retrofit2.http.GET

interface ApiService {
    @GET("movie/popular")
    suspend fun getAllMovie(): ListMovieResponse
}