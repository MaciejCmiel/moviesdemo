package com.demo.movies.ui.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.demo.movies.common.SingleLiveEvent
import com.demo.movies.data.domain.Movie
import com.demo.movies.data.local.FavMoviesRepository
import com.demo.movies.data.local.toDbModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val favMoviesRepository: FavMoviesRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val addedToFavSingleLiveData = SingleLiveEvent<Movie>()
    val addedToFavLiveData: LiveData<Movie> = addedToFavSingleLiveData

    fun addMovieToFavorites(movie: Movie) {
        disposables.add(
            favMoviesRepository.insertReplaceFavMovie(movie.toDbModel())
                .subscribe(
                    { addedToFavSingleLiveData.postValue(movie) },
                    Timber::e
                )
        )
    }

    fun removeFromFavorites(movie: Movie) {
        disposables.add(
            favMoviesRepository.deleteMovie(movie.toDbModel()).subscribe(
                { addedToFavSingleLiveData.postValue(movie) },
                Timber::e
            )
        )
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}