package com.example.fantasyland

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Game2ViewModel : ViewModel() {
    var cards = MutableLiveData<MutableList<Card?>>()

    init {
        val _cards: MutableList<Card?> = MutableList(30) { null }

        for (i in _cards.indices) {
            if ( i in 14..27)
            _cards[i - 1] = dealCard()
        }

        cards.value = _cards
    }
}