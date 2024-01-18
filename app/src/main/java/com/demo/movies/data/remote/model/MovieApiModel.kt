package com.demo.movies.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieApiModel(
    val backdrop_path: String,
    val overview: String,
    val poster_path: String,
    val title: String,
    val release_date: String,
    val vote_average: Double,
    val vote_count: Int
) : Parcelable