package com.demo.movies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.movies.R
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

        viewModel.getMovies()
    }

    private fun setViews() = with(binding) {

        rvMovies.apply {
            adapter = MoviesRecyclerViewAdapter(mutableListOf(), this@MoviesFragment)
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

    private fun setObservers() = with(viewModel) {

        moviesLiveData.observe(viewLifecycleOwner) {
            moviesAdapter.updateMovies(it)
        }
        networkErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }

    }

    private fun handleError(it: Throwable?) {
        Snackbar.make(requireView(), R.string.network_error, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(position: Int) {
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

    override fun onBottomReached() {
        viewModel.getNextPage()
    }

    override fun onMovieChecked(checked: Boolean) {
        TODO("Not yet implemented")
    }
}