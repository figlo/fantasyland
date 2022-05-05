package com.example.fantasyland

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import timber.log.Timber

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private var sortToggle = true

    private val _cards = MutableLiveData<MutableList<Card?>>()
    val cards: LiveData<MutableList<Card?>>
        get() = _cards

    private val _isMovingPhaseDone = MutableLiveData(false)
    val isMovingPhaseDone: LiveData<Boolean>
        get() = _isMovingPhaseDone

    private val _isGameFinished = MutableLiveData(false)
    val isGameFinished: LiveData<Boolean>
        get() = _isGameFinished

    private var _topRowResult = 0
    val topRowResult: Int
        get() = _topRowResult

    private var _middleRowResult = 0
    val middleRowResult: Int
        get() = _middleRowResult

    private var _bottomRowResult = 0
    val bottomRowResult: Int
        get() = _bottomRowResult

    private var _finalResult = 0
    val finalResult: Int
        get() = _finalResult

    private var _isValidResult = false
    val isValidResult: Boolean
        get() = _isValidResult

    private var _isRepeatedFantasy = false
    val isRepeatedFantasy: Boolean
        get() = _isRepeatedFantasy

    init {
        Card.values().forEach { it.cardState = CardState.DECK }
        _isGameFinished.value = false

        // cards
        val preferences = PreferenceManager.getDefaultSharedPreferences(application)
        val numberOfCardsInFantasyLand: Int = preferences.getString("number_of_cards_in_fantasy_land", "14")?.toInt()!!

        val cards: MutableList<Card?> = MutableList(30) { null }

        for (i in cards.indices) {
            if (i in 13..(12 + numberOfCardsInFantasyLand))
                cards[i] = dealCard()
        }

        _cards.value = cards

        // isMovingPhaseDone
        _isMovingPhaseDone.value = false

        Timber.i("GameViewModel created")
    }

    fun swapCards(indexOfCard1: Int, indexOfCard2: Int) {
        val cardsCopy = _cards.value as MutableList<Card?>
        cardsCopy[indexOfCard1] = cardsCopy[indexOfCard2].also { cardsCopy[indexOfCard2] = cardsCopy[indexOfCard1] }
        _cards.value = cardsCopy

        // checking if all cards are set
        val rowsCards = cardsCopy.take(13)
        _isMovingPhaseDone.value = rowsCards.all { it != null }
    }

    fun sortCards() {
        val cardsCopy = _cards.value as MutableList<Card?>

        fun List<Card>.sortDealtCards() = if (sortToggle) sortByColorAndRank() else sortByRankAndColor()

        val cardsToSort = cardsCopy
            .drop(13)
            .filterNotNull()
            .sortDealtCards()

        val sizeOfListOfNulls = 17 - cardsToSort.size
        val listOfNulls: List<Card?> = List<Card?>(sizeOfListOfNulls) { null }

        _cards.value = cardsCopy
            .take(13)
            .plus(cardsToSort)
            .plus(listOfNulls)
            .toMutableList()

        sortToggle = !sortToggle
    }

    fun setAllCards() {
        val cardsCopy = _cards.value as MutableList<Card?>
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
        require(_cards.value?.take(13)!!.all { it != null }) { "All rows cards must be not null" }

        var bottomRowCards: List<Card> = _cards.value!!.subList(0, 5).filterNotNull()
        bottomRowCards =
            if (bottomRowCards.isAnyWheel)
                bottomRowCards.sortByRankAndColorAceLow()
            else
                bottomRowCards.sortByCountRankAndColor()

        var middleRowCards: List<Card> = _cards.value!!.subList(5, 10).filterNotNull()
        middleRowCards =
            if (middleRowCards.isAnyWheel)
                middleRowCards.sortByRankAndColorAceLow()
            else
                middleRowCards.sortByCountRankAndColor()

        var topRowCards: List<Card> = _cards.value!!.subList(10, 13).filterNotNull()
        topRowCards = topRowCards.sortByCountRankAndColor()

        _cards.value = (bottomRowCards + middleRowCards + topRowCards + _cards.value!!.drop(13)).toMutableList()

        val bottomRow = BottomRow(bottomRowCards)
        val middleRow = MiddleRow(middleRowCards)
        val topRow = TopRow(topRowCards)

        _isValidResult = isValidResult(bottomRow, middleRow, topRow)
        _isRepeatedFantasy = isRepeatedFantasy(bottomRow, middleRow, topRow)

        _bottomRowResult = bottomRow.value()
        _middleRowResult = middleRow.value()
        _topRowResult = topRow.value()
        _finalResult = bottomRow.value() + middleRow.value() + topRow.value()

        _isGameFinished.value = true
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