package com.example.fantasyland.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "games_table")
data class Game(
    @PrimaryKey(autoGenerate = true)
    var gameId: Long = 0L,

    @ColumnInfo(name = "date_time")
    val dateTime: Instant,

    @ColumnInfo(name = "nick_name")
    val nickName: String
)