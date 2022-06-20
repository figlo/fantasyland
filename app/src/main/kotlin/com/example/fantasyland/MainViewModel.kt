package com.example.fantasyland

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fantasyland.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    private var _numberOfCardsInFantasyLand = 0
    val numberOfCardsInFantasyLand: Int
        get() = _numberOfCardsInFantasyLand

    init {
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