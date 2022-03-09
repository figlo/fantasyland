package com.example.fantasyland

import com.example.fantasyland.PokerCombination.*

abstract class Row(val cards: List<Card>) {
    val pokerCombination: PokerCombination = when {
        cards.isHighCard      -> HIGH_CARD
        cards.isPair          -> PAIR
        cards.isTwoPairs      -> TWO_PAIRS
        cards.isTrips         -> TRIPS
        cards.isStraight      -> STRAIGHT
        cards.isFlush         -> FLUSH
        cards.isFullHouse     -> FULL_HOUSE
        cards.isQuads         -> QUADS
        cards.isStraightFlush -> STRAIGHT_FLUSH
        cards.isRoyalFlush    -> ROYAL_FLUSH
        else                  -> throw IllegalStateException("Unknow poker combination!")
    }

    infix fun isHigherThan(otherRowCards: Row): Boolean {
        // different poker combinations
        if (pokerCombination > otherRowCards.pokerCombination) return true
        if (pokerCombination < otherRowCards.pokerCombination) return false

        // the same poker combinations
        if (
            pokerCombination == STRAIGHT
        ) {
            if (cards.isWheel) return false
            if (otherRowCards.cards.isWheel) return true
        }

        if (
            pokerCombination == STRAIGHT_FLUSH
        ) {
            if (cards.isSteelWheel) return false
            if (otherRowCards.cards.isSteelWheel) return true
        }

        val sortedCards = cards.sortByCountAndRank()
        val otherSortedCards = otherRowCards.cards.sortByCountAndRank()
        sortedCards.forEach { card ->
            val rank = card.face.rankAceHigh
            val otherRank = otherSortedCards.elementAt(sortedCards.indexOf(card)).face.rankAceHigh
            if (rank > otherRank) return true
            if (rank < otherRank) return false
        }

        return false
    }
}

class BottomRow(cards: List<Card>) : Row(cards) {
    init {
        require(cards.size == 5) { "Number of bottom row cards (must be 5): ${cards.size}" }
    }

    fun value(): Int {
        return when (pokerCombination) {
            HIGH_CARD      -> 0
            PAIR           -> 0
            TWO_PAIRS      -> 0
            TRIPS          -> 0
            STRAIGHT       -> 2
            FLUSH          -> 4
            FULL_HOUSE     -> 6
            QUADS          -> 10
            STRAIGHT_FLUSH -> 15
            ROYAL_FLUSH    -> 25
        }
    }
}

class MiddleRow(cards: List<Card>) : Row(cards) {
    init {
        require(cards.size == 5) { "Number of middle row cards (must be 5): ${cards.size}" }
    }

    fun value(): Int {
        return when (pokerCombination) {
            HIGH_CARD      -> 0
            PAIR           -> 0
            TWO_PAIRS      -> 0
            TRIPS          -> 2
            STRAIGHT       -> 4
            FLUSH          -> 8
            FULL_HOUSE     -> 12
            QUADS          -> 20
            STRAIGHT_FLUSH -> 30
            ROYAL_FLUSH    -> 50
        }
    }
}

class TopRow(cards: List<Card>) : Row(cards) {
    init {
        require(cards.size == 3) { "Number of top row cards (must be 3): ${cards.size}" }
    }

    fun value(): Int {
        val firstCardRank = cards[0].face.rankAceHigh

        return when (pokerCombination) {
            HIGH_CARD -> 0
            PAIR      -> if (firstCardRank >= 6) firstCardRank - 5 else 0
            TRIPS     -> firstCardRank + 8
            else      -> throw IllegalArgumentException("Poker combination out of range for top row")
        }
    }
}