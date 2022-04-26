package com.example.fantasyland

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager

class Game2ViewModel(application: Application) : AndroidViewModel(application) {
    var cards = MutableLiveData<MutableList<Card?>>()
    private var sortToggle = true

    init {
        val preferences = PreferenceManager.getDefaultSharedPreferences(application)
        val numberOfCardsInFantasyLand: Int = preferences.getString("number_of_cards_in_fantasy_land", "14")?.toInt()!!

        val _cards: MutableList<Card?> = MutableList(30) { null }

        for (i in _cards.indices) {
            if (i in 13..(12 + numberOfCardsInFantasyLand))
                _cards[i] = dealCard()
        }

        cards.value = _cards
    }

    fun swapCards(indexOfCard1: Int, indexOfCard2: Int) {
        val cardsCopy = cards.value as MutableList<Card?>
        cardsCopy[indexOfCard1] = cardsCopy[indexOfCard2].also { cardsCopy[indexOfCard2] = cardsCopy[indexOfCard1] }
        cards.value = cardsCopy
    }

    fun sortCards() {
        val cardsCopy = cards.value as MutableList<Card?>

        fun List<Card>.sortDealtCards() = if (sortToggle) sortByColorAndRank() else sortByRankAndColor()

        val cardsToSort = cardsCopy
            .drop(13)
            .filterNotNull()
            .sortDealtCards()

        val sizeOfListOfNulls = 17 - cardsToSort.size
        val listOfNulls: List<Card?> = List<Card?>(sizeOfListOfNulls) { null }

        cards.value = cardsCopy
            .take(13)
            .plus(cardsToSort)
            .plus(listOfNulls)
            .toMutableList()

        sortToggle = !sortToggle
    }

    fun setAllCards() {
        val cardsCopy = cards.value as MutableList<Card?>
        val rowsCards = cardsCopy.take(13).toMutableList()
        val cardsToSet = cardsCopy.drop(13).toMutableList()

        for (i in rowsCards.indices) {
            if (rowsCards[i] == null) {
                // moving first card from "cards to set" to the first empty place in rows
                val indexOfFirstCardToSet = cardsToSet.indexOfFirst { it != null }
                swapCards(i, indexOfFirstCardToSet + 13)

                // simulating card move in helper lists to properly iterate to the next step
                val cardToSet = cardsToSet[indexOfFirstCardToSet]
                rowsCards[i] = cardToSet
                cardsToSet[indexOfFirstCardToSet] = null
            }
        }
    }
}