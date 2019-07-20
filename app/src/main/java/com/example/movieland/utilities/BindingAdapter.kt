package com.example.movieland.utilities


import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.movieland.R
import com.example.movieland.database.Movie
import com.example.movieland.database.MovieTrailer
import com.example.movieland.ui.main.MovieApiStatus


/**
 * When there is no Mars property data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Movie>?) {
    val adapter = recyclerView.adapter as MovieShineAdapter

    adapter.submitList(data)
}

/**
 * When there is no Mars property data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("listDataTrailer")
fun bindTrailerRecyclerView(recyclerView: RecyclerView, data: List<MovieTrailer>?) {
    val adapter = recyclerView.adapter as TrailersAdapter
    data?.let { recyclerView.visibility = if (data.isNotEmpty()) View.VISIBLE else View.INVISIBLE }
    adapter.submitList(data)
}

/**
 * Uses the Glide library to load an image by URL into an [AppCompatImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: AppCompatImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = Uri.parse("https://image.tmdb.org/t/p/w500/$imgUrl")
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
            )
            .into(imgView)
    }
}

/**
 * This binding adapter displays the [MoviesApiStatus] of the network request in an AppCompatImageView view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("movieApiStatus")
fun bindStatus(statusImageView: AppCompatImageView, status: MovieApiStatus?) {
    when (status) {
        MovieApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        MovieApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        MovieApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("alertReleaseDate")
fun bindDate(textview: TextView, releaseDate: String?) {
    releaseDate?.let {
        val flag = textview.context.resources.getBoolean(R.bool.is_landscape)
        if (!flag) {
            textview.text = releaseDate.substring(0, 4)
        } else {
            textview.text = releaseDate
        }
    }
}

@BindingAdapter("bindTrailerNumber")
fun bindTrailerNumber(textview: TextView, numberOfTrailer: Int?) {
    numberOfTrailer?.let {
        textview.text = String.format(textview.context.resources.getString(R.string.trailer_1), numberOfTrailer)
    }
}

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(textView: TextView, data: List<MovieTrailer>?) {
    data?.let {
        var net: Boolean = false
        if (Utility.getNetworkAvailability(textView.context)) net = true

        textView.visibility =
            if (data.isNotEmpty()) {
                View.GONE
            } else {
                if (net) textView.text = "Trailers available"
                else textView.text = "Network available"
                View.VISIBLE
            }
    }
}