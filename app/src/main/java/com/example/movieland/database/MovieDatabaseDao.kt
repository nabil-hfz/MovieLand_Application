package com.example.movieland.database

import androidx.lifecycle.LiveData
import androidx.room.*


/**
 * Defines methods for using the SleepNight class with Room.
 */
@Dao
interface MovieDatabaseDao {

    /**
     * When insert a new Movie
     * @param Movie new value to write
     */
    @Insert
    fun insert(movie: Movie)

    /**
     * When updating all table with replace strategy,
     * replaces the old values with the new ones.
     *
     * @param List_Movie new value to write
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg movie: Movie)

    /**
     * When updating a raw with a value already set in a columns,
     * replaces the old value with the new one.
     *
     * @param Movie new value to write
     */
    @Update
    fun update(night: Movie)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from movieLand_table WHERE movieId = :key")
    fun get(key: Long): Movie?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM movieLand_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM movieLand_table ORDER BY movieId DESC")
    fun getAllMovies(): LiveData<List<Movie>>

    /**
     * Selects and returns the latest Movie.
     */
    @Query("SELECT * FROM movieLand_table ORDER BY movieId DESC LIMIT 1")
    fun getMovie(): Movie?

    /**
     * Selects and returns the Movie with given MovieId.
     */
    @Query("SELECT * from movieLand_table WHERE movieId = :key")
    fun getMovieWithId(key: Long): LiveData<Movie>


}
