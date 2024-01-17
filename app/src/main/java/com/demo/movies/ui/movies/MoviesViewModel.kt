package com.demo.movies.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.movies.common.SingleLiveEvent
import com.demo.movies.data.domain.Movie
import com.demo.movies.data.local.FavMoviesRepository
import com.demo.movies.data.local.model.FavMovie
import com.demo.movies.data.local.toDbModel
import com.demo.movies.data.remote.MovieRemoteRepository
import com.demo.movies.data.remote.model.MovieApiModel
import com.demo.movies.data.remote.toDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val remoteRepository: MovieRemoteRepository,
    private val favMoviesRepository: FavMoviesRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private var lastFetchedPage = 1
    private var totalPages = 1

    private val remoteMoviesMutableLiveData = MutableLiveData<MutableList<MovieApiModel>>()

    private val networkErrorMutableLiveData = MutableLiveData<Throwable>()
    val networkErrorLiveData: LiveData<Throwable> = networkErrorMutableLiveData

    private val addedToFavSingleLiveData = SingleLiveEvent<Movie>()
    val addedToFavLiveData: LiveData<Movie> = addedToFavSingleLiveData

    private val localMovies = MutableLiveData<List<FavMovie>>()

    private val combinedMoviesMediator = MediatorLiveData<List<Movie>?>()
    val combinedMoviesLiveData: LiveData<List<Movie>?> = combinedMoviesMediator

    init {
        // Update remoteMovies after local update
        combinedMoviesMediator.addSource(localMovies) { favMovies ->
            remoteMoviesMutableLiveData.value?.map { remoteMovie ->
                remoteMovie.toDomainModel(
                    // Check if the movie is in favorite movies list
                    isFavorite = favMovies.any {
                        remoteMovie.title == it.title
                    }
                )
            }.let(combinedMoviesMediator::postValue)
        }

        // Map received remote movies and mark as favorite
        combinedMoviesMediator.addSource(remoteMoviesMutableLiveData) { remoteMovies ->
            remoteMovies.map { remoteMovie ->
                remoteMovie.toDomainModel(
                    isFavorite = localMovies.value?.any {
                        remoteMovie.title == it.title
                    } ?: false
                )
            }.let(combinedMoviesMediator::postValue)
        }

        getFavoriteMovies()
        getMovies()
    }

    fun getNextPage() {
        if (lastFetchedPage < totalPages) {
            getMovies(++lastFetchedPage)
        }
    }

    private fun getMovies(page: Int = 1) {
        disposables.add(
            remoteRepository.getNowPlaying(page)
                .subscribe(
                    { nowPlaying ->
                        lastFetchedPage = nowPlaying.page
                        totalPages = nowPlaying.total_pages

                        remoteMoviesMutableLiveData.value.let { displayedMovies ->
                            displayedMovies ?: arrayListOf()
                        }.apply {
                            addAll(nowPlaying.results)
                        }.let(remoteMoviesMutableLiveData::postValue)

                    }, { throwable ->
                        networkErrorMutableLiveData.postValue(throwable)
                        Timber.d(throwable)
                    }
                )
        )
    }

    private fun getFavoriteMovies() {
        disposables.add(
            favMoviesRepository.getAllFavMovies()
                .subscribe(
                    localMovies::postValue,
                    Timber::e
                )
        )
    }

    fun addMovieToFavorites(movie: Movie) {
        disposables.add(
            favMoviesRepository.insertReplaceFavMovie(movie.toDbModel())
                .subscribe(
                    { addedToFavSingleLiveData.postValue(movie) },
                    Timber::e
                )
        )
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    fun removeFromFavorites(movie: Movie) {
        disposables.add(
            favMoviesRepository.deleteMovie(movie.toDbModel()).subscribe(
                { addedToFavSingleLiveData.postValue(movie) },
                Timber::e
            )
        )
    }

}