package com.example.fantasyland

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager

class Game2ViewModel(application: Application) : AndroidViewModel(application) {
    var cards = MutableLiveData<MutableList<Card?>>()

    init {
        val preferences = PreferenceManager.getDefaultSharedPreferences(application)
        val numberOfCardsInFantasyLand: Int = preferences.getString("number_of_cards_in_fantasy_land", "14")?.toInt()!!

        val _cards: MutableList<Card?> = MutableList(30) { null }

        for (i in _cards.indices) {
            if ( i in 13..(12 + numberOfCardsInFantasyLand))
            _cards[i] = dealCard()
        }

        cards.value = _cards
    }
}