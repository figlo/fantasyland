package com.example.fantasyland

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fantasyland.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    init {
        var numberOfCardsInFantasyLand = 14
        viewModelScope.launch {
            userPreferencesFlow.collect { userPreferences ->
                numberOfCardsInFantasyLand = userPreferences.numberOfCardsInFantasyLand
            }
        }
    }
}

class MainViewModelFactory(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}