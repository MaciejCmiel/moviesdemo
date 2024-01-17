package com.demo.movies.ui.moviedetails

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.demo.movies.R
import com.demo.movies.common.Constants.MOVIE_DTO_KEY
import com.demo.movies.common.getParcelableCompat
import com.demo.movies.data.domain.Movie
import com.demo.movies.databinding.FragmentMovieDetailsBinding
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentMovieDetails : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModels<MovieDetailsViewModel>()
    private lateinit var movie: Movie

    private val menuProvider by lazy {
        object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)

                (menu.findItem(R.id.fav_movie).actionView as? MaterialCheckBox)?.apply {
                    isChecked = movie.isFavorite
                    setOnClickListener {
                        onMovieCheckChange(movie.apply { isFavorite = isChecked })
                    }
                    buttonDrawable = AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.favorite_movie_selector
                    )
                    // hide Tick on check box *\xF0\x9F\x98\x85*
                    buttonIconTintList = ColorStateList.valueOf(Color.TRANSPARENT)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movie = requireArguments().getParcelableCompat(MOVIE_DTO_KEY, Movie::class.java) as? Movie
            ?: throw IllegalStateException("movie must not be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setObservers()
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)

    }

    private fun setViews() = with(binding) {
        Glide.with(root.context)
            .load(movie.backdropPathUrlHd)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(ivPoster)
        tvTitle.text = movie.title
        tvRating.text = root.context.getString(
            R.string.movie_rating,
            movie.voteAverage,
            movie.voteCount
        )
        tvReleaseDate.text = root.context.getString(
            R.string.movie_release_date,
            movie.releaseDate
        )
        tvDescription.text = movie.overview

    }

    private fun setObservers() = with(viewModel) {
        addedToFavLiveData.observe(viewLifecycleOwner, ::handleFavMovieUpdate)
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

    private fun onMovieCheckChange(selectedMovie: Movie) {
        if (selectedMovie.isFavorite) {
            viewModel.addMovieToFavorites(selectedMovie)
        } else {
            viewModel.removeFromFavorites(selectedMovie)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}