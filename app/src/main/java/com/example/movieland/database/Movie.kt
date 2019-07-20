package com.example.movieland.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieland.utilities.Utility.INITIALIZED
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class MovieList(val results: List<Movie>)

@Parcelize
@Entity(tableName = "movieLand_table")
@JsonClass(generateAdapter = true)
data class Movie constructor(
    val vote_average: Double?,
    @PrimaryKey
    @Json(name = "id") val movieId: Long,
    val vote_count: Int?,
    val title: String?,
    @Json(name = "poster_path") val imgSrcUrl: String?,
    @Json(name = "backdrop_path") val backdrop_path: String?,
    val overview: String?,
    val release_date: String?
) : Parcelable


@JsonClass(generateAdapter = true)
data class MovieTrailers(var results: List<MovieTrailer>)


@JsonClass(generateAdapter = true)
data class MovieTrailer constructor(@Json(name = "key") val pathToYoutube: String = INITIALIZED)


