package com.demo.movies.data.remote

import com.demo.movies.common.SchedulerProvider
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieService: MovieService,
    private val schedulerProvider: SchedulerProvider
) {

    fun getNowPlaying(page: Int) = movieService.getNowPlaying(page)
        .subscribeOn(schedulerProvider.backgroundThread())
        .observeOn(schedulerProvider.uiThread())

}