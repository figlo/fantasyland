package com.example.fantasyland

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Game2ViewModel: ViewModel() {
    var cards = MutableLiveData<List<Card>>()

    init {
        cards.value = mutableListOf()
    }
}