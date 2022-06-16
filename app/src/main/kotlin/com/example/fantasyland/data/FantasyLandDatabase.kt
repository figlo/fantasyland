package com.example.fantasyland.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Game::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FantasyLandDatabase : RoomDatabase() {

    abstract val fantasyLandDao: FantasyLandDao

    companion object {

        @Volatile
        private var INSTANCE: FantasyLandDatabase? = null

        fun getInstance(context: Context): FantasyLandDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FantasyLandDatabase::class.java,
                        "fantasy_land_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}