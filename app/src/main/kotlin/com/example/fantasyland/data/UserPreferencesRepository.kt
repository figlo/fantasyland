package com.example.fantasyland.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

private const val USER_PREFERENCES_NAME = "user_preferences"

private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

data class UserPreferences(
    val numberOfCardsInFantasyLand: Int
)

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val NUMBER_OF_CARDS_IN_FANTASY_LAND = intPreferencesKey("number_of_cards")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e("Error reading preferences")
                emit(emptyPreferences())
            } else {
                throw(exception)
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val numberOfCardsInFantasyLand = preferences[PreferencesKeys.NUMBER_OF_CARDS_IN_FANTASY_LAND] ?: 14
        return UserPreferences(numberOfCardsInFantasyLand)

    }

    suspend fun updateNumberOfCardsInFantasyLand(numberOfCardsInFantasyLand: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NUMBER_OF_CARDS_IN_FANTASY_LAND] = numberOfCardsInFantasyLand
        }
    }
}