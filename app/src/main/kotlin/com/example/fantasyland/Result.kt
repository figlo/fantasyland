package com.example.fantasyland

class Result {
    lateinit var topRowCards: TopRowCards
    lateinit var middleRowCards: MiddleRowCards
    lateinit var bottomRowCards: BottomRowCards

    val resultValue by lazy { topRowCards.value() + middleRowCards.value() + bottomRowCards.value() }
    var rowsValuesText = ""

    fun isValidResult(): Boolean {
        return when {
            middleRowCards.pokerCombination() > bottomRowCards.pokerCombination() -> false
            topRowCards.pokerCombination() > middleRowCards.pokerCombination()    -> false
            middleRowCards.pokerCombination() == bottomRowCards.pokerCombination() &&
                    middleRowCards isHigherThan bottomRowCards                    -> false
            topRowCards.pokerCombination() == middleRowCards.pokerCombination() &&
                    topRowCards isHigherThan middleRowCards                       -> false
            else                                                                  -> true
        }
    }

    fun isGameFantasy() = isValidResult() && topRowCards.value() >= 7

    fun isRepeatedFantasy() = isValidResult() && (bottomRowCards.pokerCombination() >= PokerCombination.QUADS || topRowCards.pokerCombination() == PokerCombination.TRIPS)
}