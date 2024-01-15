package com.demo.movies.data.remote

import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieService: MovieService
) {

    fun getNowPlaying() = movieService.getNowPlaying( 1)

}