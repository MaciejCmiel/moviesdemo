package com.demo.movies.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.movies.common.SchedulerProvider
import com.demo.movies.data.remote.model.Movie
import com.demo.movies.data.remote.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val moviesMutableLiveData = MutableLiveData<List<Movie>>()
    val moviesLiveData: LiveData<List<Movie>> = moviesMutableLiveData

    fun getMovies() {
        disposables.add(
            repository.getNowPlaying()
                .subscribeOn(schedulerProvider.backgroundThread())
                .observeOn(schedulerProvider.uiThread())
                .subscribe(
                    { nowPlaying ->
                        moviesMutableLiveData.postValue(nowPlaying.results)
                    }, { throwable ->
                        Timber.d(throwable)
                    }
                )
        )
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

}