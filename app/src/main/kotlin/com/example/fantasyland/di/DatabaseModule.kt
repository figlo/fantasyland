package com.example.fantasyland.di

import android.content.Context
import androidx.room.Room
import com.example.fantasyland.data.FantasyLandDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideFantasyLandDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            FantasyLandDatabase::class.java,
            "fantasy_land_database"
        ).build()

    @Singleton
    @Provides
    fun provideFantasyLandDao(db: FantasyLandDatabase) = db.fantasyLandDao
}