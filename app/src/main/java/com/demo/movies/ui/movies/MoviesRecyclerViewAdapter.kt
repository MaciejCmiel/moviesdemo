package com.demo.movies.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.movies.R
import com.demo.movies.common.IMAGE_ENDPOINT
import com.demo.movies.data.remote.model.Movie
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
        bind(movies[position])
        with(binding) {
            root.setOnClickListener {
                movieInteractionListener.onItemClicked(position)
            }
        }
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

        fun bind(movie: Movie) = with(binding) {
            Glide.with(root.context)
                .load("$IMAGE_ENDPOINT${movie.poster_path}")
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
        }
    }

    interface MovieInteractionListener {

        fun onItemClicked(position: Int)

        fun onMovieChecked(checked: Boolean)

    }

}