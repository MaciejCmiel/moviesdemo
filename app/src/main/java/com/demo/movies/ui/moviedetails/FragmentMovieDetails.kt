package com.demo.movies.ui.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.demo.movies.R
import com.demo.movies.common.Constants.IMAGE_ENDPOINT_HD
import com.demo.movies.common.Constants.MOVIE_DTO_KEY
import com.demo.movies.common.getParcelableCompat
import com.demo.movies.data.remote.model.Movie
import com.demo.movies.databinding.FragmentMovieDetailsBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FragmentMovieDetails : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var movie: Movie

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

        with(binding) {
            Glide.with(root.context)
                .load("${IMAGE_ENDPOINT_HD}${movie.backdrop_path}")
                .into(ivPoster)
            tvTitle.text = movie.title
            tvRating.text = root.context.getString(
                R.string.movie_rating,
                movie.vote_average,
                movie.vote_count
            )
            tvReleaseDate.text = root.context.getString(
                R.string.movie_release_date,
                movie.release_date
            )
            tvDescription.text = movie.overview
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}