package com.example.fantasyland.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FantasyLandDao {

    @Insert
    suspend fun insert(game: Game)

    @Query("SELECT COUNT(*) FROM games_table")
    suspend fun count(): Long

    @Query("SELECT * FROM games_table WHERE gameId = :key")
    suspend fun get(key: Long): Game?

    @Query("DELETE FROM games_table")
    suspend fun clear()

    @Query("SELECT * FROM games_table ORDER BY gameId DESC")
    fun getAllGames(): Flow<List<Game>>
}