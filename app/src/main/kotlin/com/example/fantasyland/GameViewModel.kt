package com.example.fantasyland

import androidx.lifecycle.ViewModel
import timber.log.Timber

class GameViewModel: ViewModel() {
    var sortToggle = true

    init {
        Timber.i("GameViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("GameViewModel destroyed")
    }
}