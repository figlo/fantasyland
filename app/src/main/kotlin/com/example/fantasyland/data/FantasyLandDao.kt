package com.example.fantasyland.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FantasyLandDao {

    @Insert
    fun insert(game: Game)

    @Query("SELECT * FROM games_table WHERE gameId = :key")
    fun get(key: Long): Game?

    @Query("DELETE FROM games_table")
    fun clear()

    @Query("SELECT * FROM games_table ORDER BY gameId DESC")
    fun getAllGames(): LiveData<List<Game>>
}