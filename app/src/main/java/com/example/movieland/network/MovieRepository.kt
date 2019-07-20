package com.example.movieland.network


import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieland.database.Movie
import com.example.movieland.database.MovieDatabase
import com.example.movieland.ui.main.MovieApiStatus
import com.example.movieland.utilities.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(
    private val database: MovieDatabase,
    private val context: Application
) {

    /**
     * A playlist of videos that can be shown on the screen.
     */
    val movies: LiveData<List<Movie>> = database.movieDatabaseDao.getAllMovies()

    private val _status = MutableLiveData<MovieApiStatus>()

    val status: LiveData<MovieApiStatus>
        get() = _status

    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     * To actually load the videos for use, observe [videos]
     */
    suspend fun refreshMovies(apiFilter: MovieApiFilter) {

        if (!Utility.getNetworkAvailability(context)) {
            _status.value = MovieApiStatus.ERROR
            return
        }

        _status.value = MovieApiStatus.LOADING

        var tempList: List<Movie>? = null

        withContext(Dispatchers.Main) {

            // Get the Deferred object for our Retrofit request
            var getMoviesDeferred = MovieLandService.MovieApi.retrofitService.getMoviesAsync(apiFilter.value)
            try {
                // this will run on a thread managed by Retrofit
                val listResult = getMoviesDeferred.await()
                tempList = listResult.results
                Log.v("Nabil_alhafez", "From MovieRepository try")


            } catch (e: Exception) {
                _status.value = MovieApiStatus.ERROR
                tempList = ArrayList()
                Log.v("Nabil_alhafez", "From MovieRepository catch ${"catch: " + e.message.toString()}")
            }

        }
        if (tempList.isNullOrEmpty()) {
            return
        }

        withContext(Dispatchers.IO) {
            database.movieDatabaseDao.insertAll(*tempList!!.toTypedArray())
        }
        _status.value = MovieApiStatus.DONE

    }
}
