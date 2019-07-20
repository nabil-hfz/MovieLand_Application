package com.example.movieland.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movieland.database.MovieTrailer
import com.example.movieland.databinding.TrailerItemBinding


class TrailersAdapter(val context: Context, val clickListener: TrailerListener) :
    ListAdapter<MovieTrailer, TrailersAdapter.viewHolder>(TrailerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(TrailerItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class viewHolder(private val binding: TrailerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(movieTrailer: MovieTrailer, clickListener: TrailerListener) {
            binding.movieTrailer = movieTrailer
            binding.clickListener = clickListener
            binding.position = (adapterPosition + 1)
            binding.executePendingBindings()
        }
    }
}

class TrailerDiffCallback : DiffUtil.ItemCallback<MovieTrailer>() {
    override fun areItemsTheSame(oldItem: MovieTrailer, newItem: MovieTrailer): Boolean {
        return oldItem.pathToYoutube == newItem.pathToYoutube
    }

    override fun areContentsTheSame(oldItem: MovieTrailer, newItem: MovieTrailer): Boolean {
        return oldItem == newItem
    }

}

class TrailerListener(val clickListener: (selectedTrailer: String) -> Unit) {
    fun onClick(selectedTrailer: String) = clickListener(selectedTrailer)

}