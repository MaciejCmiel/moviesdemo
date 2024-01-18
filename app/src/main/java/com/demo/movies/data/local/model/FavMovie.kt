package com.demo.movies.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_movies")
data class FavMovie(
    @PrimaryKey
    val id: Long? = null,
    val title: String
)