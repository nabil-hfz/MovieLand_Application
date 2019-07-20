package com.example.movieland.ui.details

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.movieland.database.Movie
import com.example.movieland.database.MovieTrailer
import com.example.movieland.database.MovieTrailers
import com.example.movieland.network.MovieLandService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMovieViewModel(private val selectedmovie: Movie, val app: Application) : AndroidViewModel(app) {


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

    private val _movie = MutableLiveData<Movie>()

    val movie: LiveData<Movie>
        get() = _movie

    private val _movieTrailers = MutableLiveData<List<MovieTrailer>>()

    val movieTrailers: LiveData<List<MovieTrailer>>
        get() = _movieTrailers

    private val _voteAverage = Transformations.map(movie) {
        it.vote_average.toString() + "/10"
    }

    val voteAverage: LiveData<String> get() = _voteAverage

    init {
        _movie.value = selectedmovie
        getMovieTrailers()

    }

    /**
     * Gets Movies information from the Movie API Retrofit service and updates the
     * [moviesProperty] [List] and [MovieApiStatus] [LiveData]. The Retrofit service returns a
     * coroutine Deferred, which we await to get the result of the transaction.
     */
    fun getMovieTrailers() {
        // Get the Deferred object for our Retrofit request
        MovieLandService.MovieApi.retrofitService.getMovieTrailerAsync(movie.value!!.movieId.toString())
            .enqueue(object : Callback<MovieTrailers> {
                override fun onFailure(call: Call<MovieTrailers>, t: Throwable) {
                    Log.v("Nabil_alhafez", "From MovieLandViewModel catch ${"Failure: " + t.message.toString()}")
                    _movieTrailers.value = ArrayList()
                }

                override fun onResponse(call: Call<MovieTrailers>, response: Response<MovieTrailers>) {
                    _movieTrailers.value = response.body()?.results
                    Log.v(
                        "Nabil_alhafez", "From MovieLandViewModel onResponse ${movie.value!!.movieId} " +
                                "\n ${_movieTrailers.value}"
                    )
                }

            })
    }
}


