package com.demo.movies.data.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val backdropPathUrlHd: String,
    val overview: String,
    val posterPathUrlSd: String,
    val title: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    var isFavorite: Boolean = false
) : Parcelable