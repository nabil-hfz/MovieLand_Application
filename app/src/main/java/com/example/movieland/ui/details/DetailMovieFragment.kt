package com.example.movieland.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.movieland.databinding.DetailMovieFragmentBinding
import com.example.movieland.utilities.TrailerListener
import com.example.movieland.utilities.TrailersAdapter
import com.example.movieland.utilities.Utility


class DetailMovieFragment : Fragment() {

    private lateinit var viewModel: DetailMovieViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DetailMovieFragmentBinding.inflate(inflater)

        val application = requireNotNull(this.activity!!).application

        binding.lifecycleOwner = this

        val selectedMovie = DetailMovieFragmentArgs.fromBundle(arguments!!).selectedMovie

        val detailMovieViewModelFactory = DetailMovieViewModelFactory(selectedMovie, application)

        viewModel = ViewModelProviders.of(this, detailMovieViewModelFactory).get(DetailMovieViewModel::class.java)

        binding.viewModel = viewModel

        binding.trailerRecyclerView.addItemDecoration(DividerItemDecoration(context , RecyclerView.VERTICAL))

        binding.trailerRecyclerView.setHasFixedSize(true)

        binding.trailerRecyclerView.adapter = TrailersAdapter(context!!, TrailerListener { youtubeTrailerId ->
            if (youtubeTrailerId.isNotEmpty()) {

                // context is not around, we can safely discard this click since the Fragment is no
                // longer on the screen
                val packageManager = context?.packageManager ?: return@TrailerListener

                // Try to generate a direct intent to the YouTube app
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$youtubeTrailerId"))

                if (intent.resolveActivity(packageManager) != null) startActivity(intent)
                else {
                    // YouTube app isn't found, use the web url
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=$youtubeTrailerId")
                    )
                    if (intent.resolveActivity(packageManager) != null) startActivity(intent)
                    else Toast.makeText(
                        application.applicationContext,
                        "you don't have a programme ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                if (!Utility.getNetworkAvailability(application)) {
                    Toast.makeText(application.applicationContext, "Network unavailable", Toast.LENGTH_LONG).show()
                    return@TrailerListener
                }
                viewModel.getMovieTrailers()
            }
        })
        return binding.root
    }
}



