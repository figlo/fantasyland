package com.example.fantasyland

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fantasyland.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataStore: DataStore<Preferences>, private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    private var _numberOfCardsInFantasyLand = 0
    val numberOfCardsInFantasyLand: Int
        get() = _numberOfCardsInFantasyLand

    init {
        _numberOfCardsInFantasyLand = runBlocking { dataStore.data.first()[UserPreferencesRepository.PreferencesKeys.NUMBER_OF_CARDS_IN_FANTASY_LAND] } ?: 0
        viewModelScope.launch {
            userPreferencesFlow.collect { userPreferences ->
                _numberOfCardsInFantasyLand = userPreferences.numberOfCardsInFantasyLand
            }
        }
    }

    fun setNumberOfCardsInFantasyLand(numberOfCardsInFantasyLand: Int) {
        viewModelScope.launch {
            userPreferencesRepository.setNumberOfCardsInFantasyLand(numberOfCardsInFantasyLand)
        }
    }
}