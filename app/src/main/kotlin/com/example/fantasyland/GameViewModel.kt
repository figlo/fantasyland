package com.example.fantasyland

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.fantasyland.GameState.*
import com.example.fantasyland.data.FantasyLandDao
import com.example.fantasyland.data.Game
import com.example.fantasyland.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class GameState {
    START,
    MOVING_PHASE_FINISHED,
    DONE,
}

@HiltViewModel
class GameViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository,
    private val dao: FantasyLandDao,
    application: Application
) : AndroidViewModel(application) {

    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    private var sortToggle = true

    private val _cards = MutableLiveData<List<Card?>>()
    val cards: LiveData<List<Card?>>
        get() = _cards

    private val _gameState = MutableLiveData(START)
    val gameState: LiveData<GameState>
        get() = _gameState

    private var _bottomRowResult = 0
    val bottomRowResult: Int
        get() = _bottomRowResult

    private var _middleRowResult = 0
    val middleRowResult: Int
        get() = _middleRowResult

    private var _topRowResult = 0
    val topRowResult: Int
        get() = _topRowResult

    val finalResult: Int
        get() = bottomRowResult + middleRowResult + topRowResult

    private var _isValidResult = false
    val isValidResult: Boolean
        get() = _isValidResult

    private var _isRepeatedFantasy = false
    val isRepeatedFantasy: Boolean
        get() = _isRepeatedFantasy

    private var _numberOfCardsInFantasyLand = 0
    val numberOfCardsInFantasyLand: Int
        get() = _numberOfCardsInFantasyLand

    init {
        newGame()
    }

    fun newGame() {
        // resetting variables for a new game
        _gameState.value = START
        _bottomRowResult = 0
        _middleRowResult = 0
        _topRowResult = 0
        _isValidResult = false
        _isRepeatedFantasy = false

        // dealing cards
        viewModelScope.launch {
            userPreferencesFlow.collect { userPreferences ->
                _numberOfCardsInFantasyLand = userPreferences.numberOfCardsInFantasyLand
            }
        }

        val dealtCards = Card.values()
            .asSequence()
            .shuffled(random)
            .take(numberOfCardsInFantasyLand).toList()

        val cards = List(13) { null } +
                dealtCards +
                List(17 - numberOfCardsInFantasyLand) { null }

        _cards.value = cards
    }

    fun swapCards(indexOfCard1: Int, indexOfCard2: Int) {
        val cardsCopy = _cards.value as MutableList<Card?>
        cardsCopy[indexOfCard1] = cardsCopy[indexOfCard2].also { cardsCopy[indexOfCard2] = cardsCopy[indexOfCard1] }
        _cards.value = cardsCopy

        // checking if all cards are set
        val rowsCards = cardsCopy.take(13)
        if (rowsCards.all { it != null }) _gameState.value = MOVING_PHASE_FINISHED
    }

    fun sortCards() {
        val cardsCopy = _cards.value as List<Card?>

        fun List<Card>.sortDealtCards() =
            if (sortToggle)
                sortByColorAndRank()
            else
                sortByRankAndColor()

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

        sortToggle = !sortToggle
    }

    fun setAllCards() {
        val cardsCopy = _cards.value as List<Card?>
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
        computeGameResult()
        saveGame()
    }

    private fun computeGameResult() {
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

        _cards.value = (bottomRowCards + middleRowCards + topRowCards + _cards.value!!.drop(13))

        val bottomRow = BottomRow(bottomRowCards)
        val middleRow = MiddleRow(middleRowCards)
        val topRow = TopRow(topRowCards)

        _isValidResult = isValidResult(bottomRow, middleRow, topRow)
        _isRepeatedFantasy = isRepeatedFantasy(bottomRow, middleRow, topRow)

        _bottomRowResult = bottomRow.value()
        _middleRowResult = middleRow.value()
        _topRowResult = topRow.value()

        _gameState.value = DONE
    }

    private fun saveGame() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(getApplication())
        val nickName: String = preferences.getString("nickName", "Guest") ?: "Guest"
        viewModelScope.launch {
            val result = if (isValidResult) finalResult else -1
            val newGame = Game(nickName = nickName, numberOfCardsInFantasyLand = numberOfCardsInFantasyLand, result = result)
            insertGame(newGame)
        }
    }

    private suspend fun insertGame(game: Game) {
        dao.insert(game)
    }
}

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