package com.dicoding.movie21.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.movie21.core.data.source.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao{

    @Query("SELECT * FROM favorite")
    fun getAllMovie(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM favorite where isFavorite = 1")
    fun getFavoriteMovie(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(popular: List<MovieEntity>)

    @Update
    fun updateFavoriteMovie(popular: MovieEntity)

}