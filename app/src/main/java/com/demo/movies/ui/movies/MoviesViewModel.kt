package com.demo.movies.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.movies.common.SchedulerProvider
import com.demo.movies.data.remote.MovieRepository
import com.demo.movies.data.remote.model.Movie
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
    private var lastFetchedPage = 1
    private var totalPages = 1

    private val moviesMutableLiveData = MutableLiveData<MutableList<Movie>>()
    val moviesLiveData: LiveData<MutableList<Movie>> = moviesMutableLiveData

    private val networkErrorMutableLiveData = MutableLiveData<Throwable>()
    val networkErrorLiveData: LiveData<Throwable> = networkErrorMutableLiveData

    init {
        getMovies()
    }

    fun getNextPage() {
        if (lastFetchedPage < totalPages) {
            getMovies(++lastFetchedPage)
        }
    }

    private fun getMovies(page: Int = 1) {
        disposables.add(
            repository.getNowPlaying(page)
                .subscribeOn(schedulerProvider.backgroundThread())
                .observeOn(schedulerProvider.uiThread())
                .subscribe(
                    { nowPlaying ->
                        lastFetchedPage = nowPlaying.page
                        totalPages = nowPlaying.total_pages

                        moviesLiveData.value.let { displayedMovies ->
                            displayedMovies ?: arrayListOf()
                        }.apply {
                            addAll(nowPlaying.results)
                        }.let(moviesMutableLiveData::postValue)

                    }, { throwable ->
                        networkErrorMutableLiveData.postValue(throwable)
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