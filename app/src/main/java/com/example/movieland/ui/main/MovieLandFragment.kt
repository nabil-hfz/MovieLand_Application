package com.example.movieland.ui.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.movieland.R
import com.example.movieland.databinding.MovieLandFragmentBinding
import com.example.movieland.network.MovieApiFilter
import com.example.movieland.utilities.MovieListener
import com.example.movieland.utilities.MovieShineAdapter
import com.example.movieland.utilities.Utility

class MovieLandFragment : Fragment() {

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
     * do in this Fragment.
     */
    private val viewModel: MovieLandViewModel by lazy {
        val activity = requireNotNull(this.activity) { "You can only access the viewModel after onActivityCreated()" }
        ViewModelProviders.of(this, MovieViewModelFactory(activity.application))
            .get(MovieLandViewModel::class.java)
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding: MovieLandFragmentBinding = inflate(
            inflater,
            R.layout.movie_land_fragment,
            container,
            false
        )

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        binding.lifecycleOwner = this

        //val application = requireNotNull(this.activity).application
        //  binding.recyclerView.itemAnimator = DefaultItemAnimator()

        binding.viewModel = viewModel


        binding.recyclerView.adapter =
            MovieShineAdapter(MovieListener {
                viewModel.displayPropertyDetails(it)
            })

        viewModel.repository.status.observe(this, Observer {
            binding.imageStatus.visibility = View.GONE

            if (it == MovieApiStatus.LOADING) {
                binding.deleteProgress.visibility = View.VISIBLE
            } else if (it == MovieApiStatus.DONE) {
                binding.deleteProgress.visibility = View.GONE

            } else {
                binding.deleteProgress.visibility = View.GONE
                if (viewModel.isDatabaseEmpty.value!!)
                    binding.imageStatus.visibility = View.VISIBLE
            }
        })
        viewModel.navigateToSelectedMovie.observe(this, Observer {
            if (it != null) {
                this.findNavController().navigate(MovieLandFragmentDirections.actionDetailMovieFragment(it))
                viewModel.displayPropertyDetailsComplete()
            }
        })
        setHasOptionsMenu(true)
        return binding.root
    }

    /**
     * Inflates the overflow menu that contains filtering options.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Updates the filter in the [OverviewViewModel] when the menu items are selected from the
     * overflow menu.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!Utility.getNetworkAvailability(context = context!!.applicationContext)) {
            Toast.makeText(context, "Network unavailable", Toast.LENGTH_LONG).show()
            return false
        }
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.popular_menu -> MovieApiFilter.POPULAR
                R.id.top_rated_menu -> MovieApiFilter.TOP_RATED
                else -> MovieApiFilter.SHOW_ALL
            }
        )
        return true
    }
}
