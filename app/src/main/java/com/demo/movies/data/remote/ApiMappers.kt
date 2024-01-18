package com.demo.movies.data.remote

import com.demo.movies.common.Constants
import com.demo.movies.data.domain.Movie
import com.demo.movies.data.remote.model.MovieApiModel

fun MovieApiModel.toDomainModel(isFavorite: Boolean = false) = Movie(
    backdropPathUrlHd = "${Constants.IMAGE_ENDPOINT_HD}${backdrop_path}",
    overview = overview,
    posterPathUrlSd = "${Constants.IMAGE_ENDPOINT_SD}${poster_path}",
    title = title,
    releaseDate = release_date,
    voteAverage = vote_average,
    voteCount = vote_count,
    isFavorite = isFavorite
)