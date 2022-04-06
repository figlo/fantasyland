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

fun fileName(card: Card?): String =
    if (card == null)
        "empty_card"
    else
        "card_" + card.name.takeLast(2).lowercase()

fun dealCard(): Card = Card.values()
    .filter { it.cardState == CardState.DECK }
    .random()
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