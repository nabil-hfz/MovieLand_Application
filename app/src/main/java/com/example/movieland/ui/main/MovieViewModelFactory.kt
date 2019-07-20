package com.example.movieland.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 *
 * Provides the MovieDatabaseDao and context to the ViewModel.
 */
class MovieViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieLandViewModel::class.java)) {
            return MovieLandViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}