package com.demo.movies.data.local

import com.demo.movies.common.SchedulerProvider
import com.demo.movies.common.setSchedulers
import com.demo.movies.data.local.dao.FavMovieDao
import com.demo.movies.data.local.model.FavMovie
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class RoomFavMoviesRepository @Inject constructor(
    private val favMovieDao: FavMovieDao,
    private val schedulerProvider: SchedulerProvider
) : FavMoviesRepository {

    override fun insertReplaceFavMovie(
        favMovie: FavMovie
    ): Completable = favMovieDao.insertReplaceFavMovie(favMovie)
        .setSchedulers(schedulerProvider)

    override fun getAllFavMovies(): Flowable<List<FavMovie>> =
        favMovieDao.getAllFavMovies()
            .setSchedulers(schedulerProvider)

    override fun deleteMovie(
        movie: FavMovie
    ): Completable = favMovieDao.deleteMovieByTitle(movie.title)
        .setSchedulers(schedulerProvider)

}