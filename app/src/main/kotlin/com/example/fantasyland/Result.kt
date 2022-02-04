package com.example.fantasyland

class Result {
    lateinit var topRowCards: TopRowCards
    lateinit var middleRowCards: MiddleRowCards
    lateinit var bottomRowCards: BottomRowCards

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