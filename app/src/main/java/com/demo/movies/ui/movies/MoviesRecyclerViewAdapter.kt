package com.demo.movies.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.movies.R
import com.demo.movies.data.domain.Movie
import com.demo.movies.databinding.MovieListItemBinding

internal class MoviesRecyclerViewAdapter(
    private val movies: MutableList<Movie>,
    private val movieInteractionListener: MovieInteractionListener
) : RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder) {
        if (position == movies.size - 1) {
            movieInteractionListener.onBottomReached()
        }
        bind(movies[position], movieInteractionListener)
        Unit
    }

    override fun getItemCount(): Int = movies.count()

    fun updateMovies(newList: List<Movie>) {
        movies.clear()
        movies.addAll(newList)
        notifyDataSetChanged()
    }

    internal class ViewHolder(
        val binding: MovieListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            movie: Movie,
            movieInteractionListener: MovieInteractionListener
        ) = with(binding) {
            root.setOnClickListener {
                movieInteractionListener.onItemClicked(movie)
            }

            Glide.with(root.context)
                .load(movie.posterPathUrlSd)
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
            ivFavorite.apply {
                isChecked = movie.isFavorite
                setOnClickListener {
                    movieInteractionListener.onMovieCheckChange(movie.copy(isFavorite = isChecked))
                }
            }
        }
    }

    interface MovieInteractionListener {

        fun onItemClicked(selectedMovie: Movie)

        fun onBottomReached()

        fun onMovieCheckChange(selectedMovie: Movie)

    }

}