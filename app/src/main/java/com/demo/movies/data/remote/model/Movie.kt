package com.demo.movies.data.remote.model

data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    val overview: String,
    val poster_path: String,
    val title: String,
    val release_date: String,
    val vote_average: Double,
    val vote_count: Int
)