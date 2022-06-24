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
    val dateTime: Instant = Instant.now(),

    @ColumnInfo(name = "nick_name")
    val nickName: String = "Guest",

    @ColumnInfo(name = "number_of_cards_in_fantasy_land", defaultValue = "13")
    val numberOfCardsInFantasyLand: Int = 13,

    @ColumnInfo(name = "result", defaultValue = "-1")
    val result: Int = -1
)