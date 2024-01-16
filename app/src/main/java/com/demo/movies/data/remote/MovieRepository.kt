package com.demo.movies.data.remote

import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieService: MovieService
) {

    fun getNowPlaying(page: Int) = movieService.getNowPlaying(page)

}