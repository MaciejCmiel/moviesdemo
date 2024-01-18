package com.demo.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.demo.movies.data.local.dao.FavMovieDao
import com.demo.movies.data.local.model.FavMovie

@Database(
    entities = [FavMovie::class],
    version = 1,
    exportSchema = false
)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun favMovieDao(): FavMovieDao

    companion object {
        const val DATABASE_NAME = "MoviesDatabase.db"
    }
}