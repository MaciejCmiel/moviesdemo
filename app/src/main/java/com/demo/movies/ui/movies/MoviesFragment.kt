package com.demo.movies.ui.movies

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.movies.R
import com.demo.movies.common.Constants
import com.demo.movies.data.domain.Movie
import com.demo.movies.databinding.FragmentMoviesBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MoviesFragment : Fragment(), MoviesRecyclerViewAdapter.MovieInteractionListener {

    private var _binding: FragmentMoviesBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModels<MoviesViewModel>()

    private val moviesAdapter: MoviesRecyclerViewAdapter
        get() = binding.rvMovies.adapter as MoviesRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setViews() = with(binding) {
        rvMovies.apply {
            adapter = MoviesRecyclerViewAdapter(mutableListOf(), this@MoviesFragment)
            layoutManager = getOrientationBasedLayoutManager()
        }
    }

    private fun getOrientationBasedLayoutManager() = when (resources.configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> GridLayoutManager(requireContext(), 2)
        else -> LinearLayoutManager(requireContext())
    }

    private fun setObservers() = with(viewModel) {
        combinedMoviesLiveData.observe(viewLifecycleOwner, ::updateMovies)
        networkErrorLiveData.observe(viewLifecycleOwner, ::handleError)
        addedToFavLiveData.observe(viewLifecycleOwner, ::handleFavMovieUpdate)
    }

    private fun updateMovies(movies: List<Movie>?) = with(binding) {
        emptyView.isVisible = movies.isNullOrEmpty()
        movies?.let(moviesAdapter::updateMovies)
    }

    private fun handleError(error: Throwable?) {
        showSnackbar(getString(R.string.network_error))
    }

    private fun handleFavMovieUpdate(movie: Movie) {
        showSnackbar(
            getString(
                if (movie.isFavorite) R.string.movie_added_to_fav
                else R.string.movie_removed_from_fav,
                movie.title
            )
        )
    }

    private fun showSnackbar(content: String) {
        Snackbar.make(requireView(), content, Snackbar.LENGTH_LONG).show()
    }

    override fun onItemClicked(selectedMovie: Movie) {
        findNavController().navigate(
            R.id.action_open_movie_details,
            bundleOf(Constants.MOVIE_DTO_KEY to selectedMovie)
        )
    }

    override fun onBottomReached() {
        viewModel.getNextPage()
    }

    override fun onMovieCheckChange(selectedMovie: Movie) {
        if (selectedMovie.isFavorite) {
            viewModel.addMovieToFavorites(selectedMovie)
        } else {
            viewModel.removeFromFavorites(selectedMovie)
        }
    }
}