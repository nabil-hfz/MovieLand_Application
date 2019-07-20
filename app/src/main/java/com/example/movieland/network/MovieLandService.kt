package com.example.movieland.network

import com.example.movieland.database.MovieList
import com.example.movieland.database.MovieTrailers
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

enum class MovieApiFilter(val value: String) {
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    SHOW_ALL("popular")
}

object MovieLandService {

    private val TAG = MovieLandService::class.simpleName.toString()

    private const val BASE_URL = "https://api.themoviedb.org/3/movie/"

    interface MoviewApiService {
        @GET("{filter}?api_key=64dabc8a36394ed80b29d39dd3a559f0")
        fun getMoviesAsync(@Path("filter")  filter : String): Deferred<MovieList>
        // The Coroutine Call MovieShineAdapter allows us to return a Deferred, a Job with a result





        // https://api.themoviedb.org/3/movie/66510/videos?api_key=64dabc8a36394ed80b29d39dd3a559f0&language=en-US
        @GET("{movieIe}/videos?api_key=64dabc8a36394ed80b29d39dd3a559f0")
        fun getMovieTrailerAsync(@Path("movieIe") movieIe : String): Call<MovieTrailers>
    }

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

    object MovieApi {
        val retrofitService: MoviewApiService by lazy { retrofit.create(MoviewApiService::class.java) }
    }
}