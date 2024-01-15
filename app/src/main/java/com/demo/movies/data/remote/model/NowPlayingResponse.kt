package com.demo.movies.data.remote.model

data class NowPlayingResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int

)