package com.example.fantasyland

class Result {
    lateinit var topRowCards: TopRow
    lateinit var middleRowCards: MiddleRow
    lateinit var bottomRowCards: BottomRow

    fun isValidResult(): Boolean {
        return when {
            middleRowCards isHigherThan bottomRowCards -> false
            topRowCards isHigherThan middleRowCards    -> false
            else                                       -> true
        }
    }

    fun isRepeatedFantasy() = isValidResult() &&
            (bottomRowCards.pokerCombination >= PokerCombination.QUADS ||
                    topRowCards.pokerCombination == PokerCombination.TRIPS)
}