package com.dicoding.movie21.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val overview: String,
    val title: String,
    val posterPath: String,
    val backdropPath: String,
    val releaseDate: String,
    var isFavorite: Boolean
): Parcelable