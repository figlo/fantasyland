package com.example.fantasyland

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import timber.log.Timber

class GameViewModel(application: Application) : AndroidViewModel(application) {
    var cards = MutableLiveData<MutableList<Card?>>()
    var isMovingPhaseDone = MutableLiveData<Boolean>()

    private var sortToggle = true

    var topRowResult: Int = 0
    var middleRowResult: Int = 0
    var bottomRowResult: Int = 0
    var finalResult: Int = 0
    var isValidResult: Boolean = false
    var isRepeatedFantasy: Boolean = false

    init {
        Card.values().forEach { it.cardState = CardState.DECK }

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

        Timber.i("GameViewModel created")
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
        require(cards.value?.take(13)!!.all { it != null }) { "All rows cards must be not null" }

        var bottomRowCards: List<Card> = cards.value!!.subList(0, 5).filterNotNull()
        bottomRowCards =
            if (bottomRowCards.isAnyWheel)
                bottomRowCards.sortByRankAndColorAceLow()
            else
                bottomRowCards.sortByCountRankAndColor()

        var middleRowCards: List<Card> = cards.value!!.subList(5, 10).filterNotNull()
        middleRowCards =
            if (middleRowCards.isAnyWheel)
                middleRowCards.sortByRankAndColorAceLow()
            else
                middleRowCards.sortByCountRankAndColor()

        var topRowCards: List<Card> = cards.value!!.subList(10, 13).filterNotNull()
        topRowCards = topRowCards.sortByCountRankAndColor()

        cards.value = (bottomRowCards + middleRowCards + topRowCards + cards.value!!.drop(13)).toMutableList()

        val bottomRow = BottomRow(bottomRowCards)
        val middleRow = MiddleRow(middleRowCards)
        val topRow = TopRow(topRowCards)

        isValidResult = isValidResult(bottomRow, middleRow, topRow)
        isRepeatedFantasy = isRepeatedFantasy(bottomRow, middleRow, topRow)

        bottomRowResult = bottomRow.value()
        middleRowResult = middleRow.value()
        topRowResult = topRow.value()
        finalResult = bottomRow.value() + middleRow.value() + topRow.value()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("GameViewModel destroyed")
    }
}

fun fileName(card: Card?): String =
    if (card == null)
        "empty_card"
    else
        "card_" + card.name.takeLast(2).lowercase()

fun dealCard(): Card = Card.values()
    .filter { it.cardState == CardState.DECK }
    .random(random)
    .apply { cardState = CardState.DEALT }

fun isValidResult(bottomRow: BottomRow, middleRow: MiddleRow, topRow: TopRow): Boolean =
    when {
        middleRow isHigherThan bottomRow -> false
        topRow isHigherThan middleRow    -> false
        else                             -> true
    }

fun isRepeatedFantasy(bottomRow: BottomRow, middleRow: MiddleRow, topRow: TopRow): Boolean =
    isValidResult(bottomRow, middleRow, topRow) &&
            (bottomRow.pokerCombination >= PokerCombination.QUADS ||
                    topRow.pokerCombination == PokerCombination.TRIPS)
