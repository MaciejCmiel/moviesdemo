package com.demo.movies.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.movies.data.local.model.FavMovie
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface FavMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReplaceFavMovie(report: FavMovie): Completable

    @Query("SELECT * FROM fav_movies")
    fun getAllFavMovies(): Flowable<List<FavMovie>>

    @Query("DELETE FROM fav_movies WHERE title = :title")
    fun deleteMovieByTitle(title: String): Completable

}