package com.demo.movies.data.remote

import com.demo.movies.data.remote.model.NowPlayingResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("3/movie/now_playing")
    fun getNowPlaying(
        @Query("page") page: Int
    ): Single<NowPlayingResponse>

}