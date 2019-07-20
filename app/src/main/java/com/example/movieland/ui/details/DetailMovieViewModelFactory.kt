package com.example.movieland.ui.details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieland.database.Movie

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 *
 * Provides the MovieDatabaseDao and context to the ViewModel.
 */
class DetailMovieViewModelFactory(private val movie: Movie ,private val app : Application)
    : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailMovieViewModel::class.java)) {
            return DetailMovieViewModel(movie , app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
