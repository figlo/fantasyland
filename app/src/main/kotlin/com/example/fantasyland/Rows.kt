package com.example.fantasyland

import com.example.fantasyland.PokerCombination.*

open class RowCards(val cards: MutableList<Card>) {
    init {
        require(cards.size == 3 || cards.size == 5) { "Number of row cards (must be 3 or 5): ${cards.size}" }
        cards.sortByCountAndRank()
    }

    infix fun isHigherThan(otherRowCards: RowCards): Boolean {
        if (
            pokerCombination() == STRAIGHT && otherRowCards.pokerCombination() == STRAIGHT ||
            pokerCombination() == STRAIGHT_FLUSH && otherRowCards.pokerCombination() == STRAIGHT_FLUSH
        ) {
            if (cards[4].face.rankAceHigh == 2) return false
            if (otherRowCards.cards[4].face.rankAceHigh == 2) return true
        }

        cards.forEach { card ->
            val rank = card.face.rankAceHigh
            val otherRank = otherRowCards.cards.elementAt(cards.indexOf(card)).face.rankAceHigh
            if (rank > otherRank) return true
            if (rank < otherRank) return false
        }

        return false
    }

    open fun pokerCombination(): PokerCombination {
        return when {
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
    }
}

class BottomRowCards(cards: MutableList<Card>) : RowCards(cards) {
    init {
        require(cards.size == 5) { "Number of bottom row cards (must be 5): ${cards.size}" }
    }

    val isWheel = (pokerCombination() == STRAIGHT || pokerCombination() == STRAIGHT_FLUSH) &&
                cards[4].face.rankAceHigh == 2

    fun value(): Int {
        return when (pokerCombination()) {
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

class MiddleRowCards(cards: MutableList<Card>) : RowCards(cards) {
    init {
        require(cards.size == 5) { "Number of middle row cards (must be 5): ${cards.size}" }
    }

    val isWheel = (pokerCombination() == STRAIGHT || pokerCombination() == STRAIGHT_FLUSH) &&
            cards[4].face.rankAceHigh == 2

    fun value(): Int {
        return when (pokerCombination()) {
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

class TopRowCards(cards: MutableList<Card>) : RowCards(cards) {
    init {
        require(cards.size == 3) { "Number of top row cards (must be 3): ${cards.size}" }
    }

    fun value(): Int {
        val firstCardRank = cards[0].face.rankAceHigh

        return when (pokerCombination()) {
            HIGH_CARD -> 0
            PAIR      -> if (firstCardRank >= 6) firstCardRank - 5 else 0
            TRIPS     -> firstCardRank + 8
            else      -> throw IllegalArgumentException("Poker combination out of range for top row")
        }
    }
}