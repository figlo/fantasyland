package com.example.fantasyland.data

import android.content.Context
import androidx.room.*

@Database(
    entities = [Game::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
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