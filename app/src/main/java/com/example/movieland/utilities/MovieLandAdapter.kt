package com.example.movieland.utilities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movieland.database.Movie
import com.example.movieland.databinding.GridViewItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class MovieShineAdapter(val clickListener: MovieListener) : ListAdapter<Movie,
        MovieShineAdapter.viewHolder>(MovieDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = GridViewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val movieItem = getItem(position)
        holder.bind(clickListener, movieItem)
    }


    class viewHolder(private val binding: GridViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: MovieListener, movie: Movie) {
            binding.movie = movie
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.movieId == newItem.movieId
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}

class MovieListener(val clickListener: (selectedMovie: Movie) -> Unit) {
    fun onClick(movie: Movie) = clickListener(movie)
}
