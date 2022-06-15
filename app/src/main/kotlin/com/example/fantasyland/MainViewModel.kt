package com.example.fantasyland

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fantasyland.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

//    val initialSetupEvent = liveData {
//        emit(userPreferencesRepository.fetchInitialPreferences())
//    }

//    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

//    val userPreferencesLiveData = userPreferencesFlow.asLiveData()

//    init {
//        var numberOfCardsInFantasyLand: Int
//        viewModelScope.launch {
//            userPreferencesFlow.collect { userPreferences ->
//                numberOfCardsInFantasyLand = userPreferences.numberOfCardsInFantasyLand
//            }
//        }
//    }

//    val numberOfCardsInFantasyLand = userPreferencesFlow.asLiveData()

    fun setNumberOfCardsInFantasyLand(numberOfCardsInFantasyLand: Int) {
        viewModelScope.launch {
            userPreferencesRepository.setNumberOfCardsInFantasyLand(numberOfCardsInFantasyLand)
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