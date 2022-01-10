package com.example.fantasyland

import com.example.fantasyland.CardFace.*
import com.example.fantasyland.CardState.DECK
import com.example.fantasyland.CardSuit.*

enum class CardFace(val abbr: Char, val rankAceHigh: Int) {
    /*
        !!! Do not change order or rankAceHigh values !!!
        !!! Row values are based on it !!!
     */

    ACE('A', 14),
    TWO('2', 2),
    THREE('3', 3),
    FOUR('4', 4),
    FIVE('5', 5),
    SIX('6', 6),
    SEVEN('7', 7),
    EIGHT('8', 8),
    NINE('9', 9),
    TEN('T', 10),
    JACK('J', 11),
    QUEEN('Q', 12),
    KING('K', 13);
}

enum class CardSuit(val abbr: Char, val hexColor: String) {
    SPADES('s', "#000000"),         // black
    HEARTS('h', "#ff0000"),         // red
    DIAMONDS('d', "#0000ff"),       // blue
    CLUBS('c', "#00bb00");          // green
}

enum class NewCard(val face: CardFace, val suit: CardSuit, val file: String, var cardState: CardState = DECK) {
    CARD_AS(ACE, SPADES, "card_as.png"),
    CARD_2S(TWO, SPADES, "card_2s.png"),
    CARD_3S(THREE, SPADES, "card_3s.png"),
    CARD_4S(FOUR, SPADES, "card_4s.png"),
    CARD_5S(FIVE, SPADES, "card_5s.png"),
    CARD_6S(SIX, SPADES, "card_6s.png"),
    CARD_7S(SEVEN, SPADES, "card_7s.png"),
    CARD_8S(EIGHT, SPADES, "card_8s.png"),
    CARD_9S(NINE, SPADES, "card_9s.png"),
    CARD_TS(TEN, SPADES, "card_ts.png"),
    CARD_JS(JACK, SPADES, "card_js.png"),
    CARD_QS(QUEEN, SPADES, "card_qs.png"),
    CARD_KS(KING, SPADES, "card_ks.png"),

    CARD_AH(ACE, HEARTS, "card_ah.png"),
    CARD_2H(TWO, HEARTS, "card_2h.png"),
    CARD_3H(THREE, HEARTS, "card_3h.png"),
    CARD_4H(FOUR, HEARTS, "card_4h.png"),
    CARD_5H(FIVE, HEARTS, "card_5h.png"),
    CARD_6H(SIX, HEARTS, "card_6h.png"),
    CARD_7H(SEVEN, HEARTS, "card_7h.png"),
    CARD_8H(EIGHT, HEARTS, "card_8h.png"),
    CARD_9H(NINE, HEARTS, "card_9h.png"),
    CARD_TH(TEN, HEARTS, "card_th.png"),
    CARD_JH(JACK, HEARTS, "card_jh.png"),
    CARD_QH(QUEEN, HEARTS, "card_qh.png"),
    CARD_KH(KING, HEARTS, "card_kh.png"),

    CARD_AD(ACE, DIAMONDS, "card_ad.png"),
    CARD_2D(TWO, DIAMONDS, "card_2d.png"),
    CARD_3D(THREE, DIAMONDS, "card_3d.png"),
    CARD_4D(FOUR, DIAMONDS, "card_4d.png"),
    CARD_5D(FIVE, DIAMONDS, "card_5d.png"),
    CARD_6D(SIX, DIAMONDS, "card_6d.png"),
    CARD_7D(SEVEN, DIAMONDS, "card_7d.png"),
    CARD_8D(EIGHT, DIAMONDS, "card_8d.png"),
    CARD_9D(NINE, DIAMONDS, "card_9d.png"),
    CARD_TD(TEN, DIAMONDS, "card_td.png"),
    CARD_JD(JACK, DIAMONDS, "card_jd.png"),
    CARD_QD(QUEEN, DIAMONDS, "card_qd.png"),
    CARD_KD(KING, DIAMONDS, "card_kd.png"),

    CARD_AC(ACE, CLUBS, "card_ac.png"),
    CARD_2C(TWO, CLUBS, "card_2c.png"),
    CARD_3C(THREE, CLUBS, "card_3c.png"),
    CARD_4C(FOUR, CLUBS, "card_4c.png"),
    CARD_5C(FIVE, CLUBS, "card_5c.png"),
    CARD_6C(SIX, CLUBS, "card_6c.png"),
    CARD_7C(SEVEN, CLUBS, "card_7c.png"),
    CARD_8C(EIGHT, CLUBS, "card_8c.png"),
    CARD_9C(NINE, CLUBS, "card_9c.png"),
    CARD_TC(TEN, CLUBS, "card_tc.png"),
    CARD_JC(JACK, CLUBS, "card_jc.png"),
    CARD_QC(QUEEN, CLUBS, "card_qc.png"),
    CARD_KC(KING, CLUBS, "card_kc.png");
}

enum class CardState {
    DECK,
    DEALT,
    TOP_ROW,
    MIDDLE_ROW,
    BOTTOM_ROW;
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