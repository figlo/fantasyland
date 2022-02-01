package com.example.fantasyland

import com.example.fantasyland.CardFace.ACE
import com.example.fantasyland.CardFace.FIVE
import com.example.fantasyland.PokerCombination.*

open class RowCards(protected val cards: MutableList<Card>) {
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

    protected val numberOfFaces = cards.map { it.face }.distinct().count()

    open fun pokerCombination(): PokerCombination {
        fun numberOfFacesIsFive(): PokerCombination {
            val minFace = cards.map { it.face.rankAceHigh }.minOrNull() ?: throw IllegalArgumentException("minFace must be > 0")
            val maxFace = cards.map { it.face.rankAceHigh }.maxOrNull() ?: throw IllegalArgumentException("maxFace must be > 0")
            val numberOfSuits = cards.map { it.suit }.distinct().count()

            fun isFlush() = numberOfSuits == 1
            fun isStraight() = maxFace - minFace == 4 ||
                    (cards[0].face == ACE && cards[1].face == FIVE)

            return if (isFlush()) {
                if (isStraight()) {
                    if (cards[0].face == ACE) ROYAL_FLUSH else STRAIGHT_FLUSH
                } else {
                    FLUSH
                }
            } else {
                if (isStraight()) STRAIGHT else HIGH_CARD
            }
        }

        return when (numberOfFaces) {
            2    -> if (cards[2].face == cards[3].face) QUADS else FULL_HOUSE
            3    -> if (cards[1].face == cards[2].face) TRIPS else TWO_PAIRS
            4    -> PAIR
            5    -> numberOfFacesIsFive()
            else -> throw IllegalArgumentException("Number of faces (must be 2, 3, 4 or 5 for BottomRowCards and MiddleRowCards): $numberOfFaces")
        }
    }
}

class BottomRowCards(cards: MutableList<Card>) : RowCards(cards) {
    init {
        require(cards.size == 5) { "Number of bottom row cards (must be 5): ${cards.size}" }
    }

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

    override fun pokerCombination(): PokerCombination {
        return when (numberOfFaces) {
            1    -> TRIPS
            2    -> PAIR
            3    -> HIGH_CARD
            else -> throw IllegalArgumentException("Number of faces (must be 1, 2, or 3 for TopRowCards): $numberOfFaces")
        }
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