package com.example.fantasyland

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager

class Game2ViewModel(application: Application) : AndroidViewModel(application) {
    var cards = MutableLiveData<MutableList<Card?>>()
    var isMovingPhaseDone = MutableLiveData<Boolean>()

    private var sortToggle = true

    init {
        // cards
        val preferences = PreferenceManager.getDefaultSharedPreferences(application)
        val numberOfCardsInFantasyLand: Int = preferences.getString("number_of_cards_in_fantasy_land", "14")?.toInt()!!

        val _cards: MutableList<Card?> = MutableList(30) { null }

        for (i in _cards.indices) {
            if (i in 13..(12 + numberOfCardsInFantasyLand))
                _cards[i] = dealCard()
        }

        cards.value = _cards

        // isMovingPhaseDone
        val _isMovingPhaseDone = false
        isMovingPhaseDone.value = _isMovingPhaseDone
    }

    fun swapCards(indexOfCard1: Int, indexOfCard2: Int) {
        val cardsCopy = cards.value as MutableList<Card?>
        cardsCopy[indexOfCard1] = cardsCopy[indexOfCard2].also { cardsCopy[indexOfCard2] = cardsCopy[indexOfCard1] }
        cards.value = cardsCopy

        // checking if all cards are set
        val rowsCards = cardsCopy.take(13)
        isMovingPhaseDone.value = rowsCards.all { it != null }
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

    fun evaluateGame() {

//        var bottomRowCards: List<Card> = imageViews.subList(0, 5).map { it.tag as Card }
//        bottomRowCards =
//            if (bottomRowCards.isAnyWheel)
//                bottomRowCards.sortByRankAndColorAceLow()
//            else
//                bottomRowCards.sortByCountRankAndColor()
//
//        var middleRowCards: List<Card> = imageViews.subList(5, 10).map { it.tag as Card }
//        middleRowCards =
//            if (middleRowCards.isAnyWheel)
//                middleRowCards.sortByRankAndColorAceLow()
//            else
//                middleRowCards.sortByCountRankAndColor()
//
//        var topRowCards: List<Card> = imageViews.subList(10, 13).map { it.tag as Card }
//        topRowCards = topRowCards.sortByCountRankAndColor()
//
//        val bottomRow = BottomRow(bottomRowCards)
//        val middleRow = MiddleRow(middleRowCards)
//        val topRow = TopRow(topRowCards)
//
//        for (i in 1..13) {
//            val card: Card = when (i) {
//                in 1..5  -> bottomRowCards[i - 1]
//                in 6..10 -> middleRowCards[i - 6]
//                else     -> topRowCards[i - 11]
//            }
//
//            imageViews[i - 1].apply {
//                setImageResource(cardImageResource(card))
//                tag = card
//            }
//        }
//
//        val resultOKColor: Int = ContextCompat.getColor(requireContext(), R.color.resultOK)
//        val resultXColor: Int = ContextCompat.getColor(requireContext(), R.color.resultX)
//        val newFantasyLandColor: Int = ContextCompat.getColor(requireContext(), R.color.newFantasyLand)
//
//        if (isValidResult(bottomRow, middleRow, topRow)) {
//            val result: Int = bottomRow.value() + middleRow.value() + topRow.value()
//            binding.apply {
//                bottomRowResult.text = bottomRow.value().toString()
//                middleRowResult.text = middleRow.value().toString()
//                topRowResult.text = topRow.value().toString()
//                finalResult.text = result.toString()
//                finalResult.setTextColor(resultOKColor)
//            }
//        } else {
//            binding.apply {
//                finalResult.text = resources.getString(R.string.result_x)
//                finalResult.setTextColor(resultXColor)
//            }
//        }
//
//        if (isRepeatedFantasy(bottomRow, middleRow, topRow)) {
//            binding.apply {
//                newFantasyLand.text = resources.getString(R.string.new_fantasyland)
//                newFantasyLand.setTextColor(newFantasyLandColor)
//            }
//        }

    }
}