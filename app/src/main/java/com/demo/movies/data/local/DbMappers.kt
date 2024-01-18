package com.demo.movies.data.local

import com.demo.movies.data.domain.Movie
import com.demo.movies.data.local.model.FavMovie

fun Movie.toDbModel() = FavMovie(title = title)