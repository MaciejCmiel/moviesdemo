package com.demo.movies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.demo.movies.R
import com.demo.movies.databinding.FragmentMoviesBinding
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

        viewModel.getMovies()

    }

    private fun setViews() = with(binding) {

        rvMovies.apply {
            adapter = MoviesRecyclerViewAdapter(mutableListOf(), this@MoviesFragment)
        }

    }

    private fun setObservers() = with(viewModel) {

        moviesLiveData.observe(viewLifecycleOwner) {
            (binding.rvMovies.adapter as MoviesRecyclerViewAdapter).updateMovies(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(position: Int) {
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

    override fun onMovieChecked(checked: Boolean) {
        TODO("Not yet implemented")
    }
}