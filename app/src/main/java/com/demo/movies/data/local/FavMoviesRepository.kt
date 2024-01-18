package com.demo.movies.data.local

import com.demo.movies.data.local.model.FavMovie
import io.reactivex.Completable
import io.reactivex.Flowable

interface FavMoviesRepository {

    fun insertReplaceFavMovie(favMovie: FavMovie): Completable

    fun getAllFavMovies(): Flowable<List<FavMovie>>

    fun deleteMovie(movie: FavMovie): Completable

}