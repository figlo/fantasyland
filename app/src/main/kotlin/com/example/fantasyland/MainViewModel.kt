package com.example.fantasyland

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fantasyland.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    fun setNumberOfCardsInFantasyLand(numberOfCardsInFantasyLand: Int) {
        viewModelScope.launch {
            userPreferencesRepository.setNumberOfCardsInFantasyLand(numberOfCardsInFantasyLand)
        }
    }
}