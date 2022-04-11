package com.example.fantasyland

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Game2ViewModel: ViewModel() {
    var cards = MutableLiveData<MutableList<Card?>>()

    init {
        cards.value = MutableList(30) { null }
    }
}