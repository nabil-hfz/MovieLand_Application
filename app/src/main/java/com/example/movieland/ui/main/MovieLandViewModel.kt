package com.example.movieland.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieland.database.Movie
import com.example.movieland.database.MovieDatabase
import com.example.movieland.network.MovieApiFilter
import com.example.movieland.network.MovieRepository
import kotlinx.coroutines.*

enum class MovieApiStatus { LOADING, ERROR, DONE }

class MovieLandViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // The external LiveData interface to the property is immutable, so only this class can modify
    lateinit var movies: LiveData<List<Movie>>

    private val _navigateToSelectedMovie = MutableLiveData<Movie>()

    val navigateToSelectedMovie: LiveData<Movie>
        get() = _navigateToSelectedMovie

    private val database = MovieDatabase.getInstance(application)

    val context = application

    val repository = MovieRepository(database, context)

    private val _isDatabaseEmpty = MutableLiveData<Boolean>()

    val isDatabaseEmpty: LiveData<Boolean>
        get() = _isDatabaseEmpty

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        getMoviesFromRepositoryAsync(MovieApiFilter.POPULAR)
    }


    /**
     * Gets Movies information from the Movie API Retrofit service and updates the
     * [moviesProperty] [List] and [MovieApiStatus] [LiveData]. The Retrofit service returns a
     * coroutine Deferred, which we await to get the result of the transaction.
     */
    private fun getMoviesFromRepositoryAsync(apiFilter: MovieApiFilter) {

        uiScope.launch {
            _isDatabaseEmpty.value = isDatabaseEmpty()
            repository.refreshMovies(apiFilter)
        }
        movies = repository.movies

    }

    private suspend fun isDatabaseEmpty(): Boolean {
        var isThereMovie = true
        withContext(Dispatchers.IO) {
            isThereMovie = (database.movieDatabaseDao.getMovie() == null)

        }
        return isThereMovie
    }


    fun displayPropertyDetails(movieProperty: Movie) {
        _navigateToSelectedMovie.value = movieProperty
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedMovie.value = null
    }

    fun updateFilter(filter: MovieApiFilter) {
        getMoviesFromRepositoryAsync(filter)
    }

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

//    private suspend fun clear() {
//        withContext(Dispatchers.IO) {
//            database.clear()
//        }
//    }
//
//    private suspend fun update(night: SleepNight) {
//        withContext(Dispatchers.IO) {
//            database.update(night)
//        }
//    }
//
//    private suspend fun insert(night: SleepNight) {
//        withContext(Dispatchers.IO) {
//            database.insert(night)
//        }
//    }
//
//    /**
//     * Executes when the START button is clicked.
//     */
//    fun onStartTracking() {
//        uiScope.launch {
//            // Create a new night, which captures the current time,
//            // and insert it into the database.
//            val newNight = SleepNight()
//
//            insert(newNight)
//
//            Movie.value = getMovieFromDatabase()
//        }
//    }
//
//    /**
//     * Executes when the STOP button is clicked.
//     */
//    fun onStopTracking() {
//        uiScope.launch {
//            // In Kotlin, the return@label syntax is used for specifying which function among
//            // several nested ones this statement returns from.
//            // In this case, we are specifying to return from launch(),
//            // not the lambda.
//            val oldNight = Movie.value ?: return@launch
//
//            // Update the night in the database to add the end time.
//            oldNight.endTimeMilli = System.currentTimeMillis()
//
//            update(oldNight)
//
//            // Set state to navigate to the SleepQualityFragment.
//            _navigateToSleepQuality.value = oldNight
//        }
//    }
//
//    /**
//     * Executes when the CLEAR button is clicked.
//     */
//    fun onClear() {
//        uiScope.launch {
//            // Clear the database table.
//            clear()
//
//            // And clear Movie since it's no longer in the database
//            Movie.value = null
//        }
//
//        // Show a snackbar message, because it's friendly.
//        _showSnackEventEvent.value = true
//    }

}
