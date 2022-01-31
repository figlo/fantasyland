package com.example.fantasyland

import com.example.fantasyland.CardFace.*
import com.example.fantasyland.CardSuit.*

enum class CardFace(val rankAceHigh: Int) {
    /*
        !!! Do not change order or rankAceHigh values !!!
        !!! Row values are based on it !!!
     */

    ACE(14),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13);
}

enum class CardSuit {
    SPADES,         // black
    HEARTS,         // red
    DIAMONDS,       // blue
    CLUBS;          // green
}

enum class Card(val face: CardFace, val suit: CardSuit, val file: String, var cardState: CardState = CardState.DECK) {
    CARD_AS(ACE, SPADES, "card_as"),
    CARD_2S(TWO, SPADES, "card_2s"),
    CARD_3S(THREE, SPADES, "card_3s"),
    CARD_4S(FOUR, SPADES, "card_4s"),
    CARD_5S(FIVE, SPADES, "card_5s"),
    CARD_6S(SIX, SPADES, "card_6s"),
    CARD_7S(SEVEN, SPADES, "card_7s"),
    CARD_8S(EIGHT, SPADES, "card_8s"),
    CARD_9S(NINE, SPADES, "card_9s"),
    CARD_TS(TEN, SPADES, "card_ts"),
    CARD_JS(JACK, SPADES, "card_js"),
    CARD_QS(QUEEN, SPADES, "card_qs"),
    CARD_KS(KING, SPADES, "card_ks"),

    CARD_AH(ACE, HEARTS, "card_ah"),
    CARD_2H(TWO, HEARTS, "card_2h"),
    CARD_3H(THREE, HEARTS, "card_3h"),
    CARD_4H(FOUR, HEARTS, "card_4h"),
    CARD_5H(FIVE, HEARTS, "card_5h"),
    CARD_6H(SIX, HEARTS, "card_6h"),
    CARD_7H(SEVEN, HEARTS, "card_7h"),
    CARD_8H(EIGHT, HEARTS, "card_8h"),
    CARD_9H(NINE, HEARTS, "card_9h"),
    CARD_TH(TEN, HEARTS, "card_th"),
    CARD_JH(JACK, HEARTS, "card_jh"),
    CARD_QH(QUEEN, HEARTS, "card_qh"),
    CARD_KH(KING, HEARTS, "card_kh"),

    CARD_AD(ACE, DIAMONDS, "card_ad"),
    CARD_2D(TWO, DIAMONDS, "card_2d"),
    CARD_3D(THREE, DIAMONDS, "card_3d"),
    CARD_4D(FOUR, DIAMONDS, "card_4d"),
    CARD_5D(FIVE, DIAMONDS, "card_5d"),
    CARD_6D(SIX, DIAMONDS, "card_6d"),
    CARD_7D(SEVEN, DIAMONDS, "card_7d"),
    CARD_8D(EIGHT, DIAMONDS, "card_8d"),
    CARD_9D(NINE, DIAMONDS, "card_9d"),
    CARD_TD(TEN, DIAMONDS, "card_td"),
    CARD_JD(JACK, DIAMONDS, "card_jd"),
    CARD_QD(QUEEN, DIAMONDS, "card_qd"),
    CARD_KD(KING, DIAMONDS, "card_kd"),

    CARD_AC(ACE, CLUBS, "card_ac"),
    CARD_2C(TWO, CLUBS, "card_2c"),
    CARD_3C(THREE, CLUBS, "card_3c"),
    CARD_4C(FOUR, CLUBS, "card_4c"),
    CARD_5C(FIVE, CLUBS, "card_5c"),
    CARD_6C(SIX, CLUBS, "card_6c"),
    CARD_7C(SEVEN, CLUBS, "card_7c"),
    CARD_8C(EIGHT, CLUBS, "card_8c"),
    CARD_9C(NINE, CLUBS, "card_9c"),
    CARD_TC(TEN, CLUBS, "card_tc"),
    CARD_JC(JACK, CLUBS, "card_jc"),
    CARD_QC(QUEEN, CLUBS, "card_qc"),
    CARD_KC(KING, CLUBS, "card_kc");

    companion object {
        var sortSwitch = false

        private fun sortByRankAndColor() =
           values()
                .filter { it.cardState == CardState.DEALT }
                .sortedWith(
                    compareBy(
                        { -it.face.rankAceHigh },
                        { it.suit }
                    )
                )

        private fun sortByColorAndRank() =
            values()
                .filter { it.cardState == CardState.DEALT }
                .sortedWith(
                    compareBy(
                        { it.suit },
                        { -it.face.rankAceHigh }
                    )
                )

        fun sort(): List<Card> {
            sortSwitch = !sortSwitch

            return when (sortSwitch) {
                true  -> sortByColorAndRank()
                false -> sortByRankAndColor()
            }
    }

    protected fun sortByCountAndRank() =
            values()
                .filter { it.cardState == CardState.DEALT }
                .sortedWith(
                    compareBy(
                        { card -> -values().count { otherCard -> card.face == otherCard.face } },
                        { -it.face.rankAceHigh }
                    )
                )

        fun dealCard(): Card {
            val dealtCard =
                values()
                    .filter { it.cardState == CardState.DECK }
                    .random()
            dealtCard.cardState = CardState.DEALT
            return dealtCard
        }
    }
}

enum class CardState {
    DECK,
    DEALT,
    BOARD;
}

enum class PokerCombination {
    /*
        !!! Do not change order !!!
        !!! Row values are based on it !!!
     */

    HIGH_CARD,
    PAIR,
    TWO_PAIRS,
    TRIPS,
    STRAIGHT,
    FLUSH,
    FULL_HOUSE,
    QUADS,
    STRAIGHT_FLUSH,
    ROYAL_FLUSH;
}