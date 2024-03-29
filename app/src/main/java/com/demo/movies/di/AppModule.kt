package com.demo.movies.di

import android.app.Application
import androidx.room.Room
import com.demo.movies.common.AndroidSchedulerProvider
import com.demo.movies.common.Constants.BASE_URL
import com.demo.movies.common.SchedulerProvider
import com.demo.movies.data.local.FavMoviesRepository
import com.demo.movies.data.local.MoviesDatabase
import com.demo.movies.data.local.RoomFavMoviesRepository
import com.demo.movies.data.local.dao.FavMovieDao
import com.demo.movies.data.remote.AuthInterceptor
import com.demo.movies.data.remote.MovieRemoteRepository
import com.demo.movies.data.remote.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider = AndroidSchedulerProvider()

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideMovieService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieService: MovieService,
        schedulerProvider: SchedulerProvider
    ): MovieRemoteRepository = MovieRemoteRepository(movieService, schedulerProvider)

    @Provides
    @Singleton
    fun provideMoviesDatabase(
        application: Application
    ): MoviesDatabase = Room.databaseBuilder(
        application,
        MoviesDatabase::class.java,
        MoviesDatabase.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideFavMovieDao(
        moviesDatabase: MoviesDatabase
    ): FavMovieDao = moviesDatabase.favMovieDao()

    @Provides
    @Singleton
    fun provideFavMovieRepository(
        favMovieDao: FavMovieDao,
        schedulerProvider: SchedulerProvider
    ): FavMoviesRepository = RoomFavMoviesRepository(
        favMovieDao,
        schedulerProvider
    )
}