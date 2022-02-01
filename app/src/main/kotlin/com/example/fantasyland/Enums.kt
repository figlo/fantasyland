package com.example.fantasyland

import com.example.fantasyland.CardFace.*
import com.example.fantasyland.CardSuit.*

enum class CardFace {
    ACE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING;

    val rankAceHigh = if (name == "ACE") 14 else ordinal + 1
}

enum class CardSuit {
    SPADES,
    HEARTS,
    DIAMONDS,
    CLUBS;
}

enum class Card(val face: CardFace, val suit: CardSuit, var cardState: CardState = CardState.DECK) {
    CARD_AS(ACE, SPADES),
    CARD_2S(TWO, SPADES),
    CARD_3S(THREE, SPADES),
    CARD_4S(FOUR, SPADES),
    CARD_5S(FIVE, SPADES),
    CARD_6S(SIX, SPADES),
    CARD_7S(SEVEN, SPADES),
    CARD_8S(EIGHT, SPADES),
    CARD_9S(NINE, SPADES),
    CARD_TS(TEN, SPADES),
    CARD_JS(JACK, SPADES),
    CARD_QS(QUEEN, SPADES),
    CARD_KS(KING, SPADES),

    CARD_AH(ACE, HEARTS),
    CARD_2H(TWO, HEARTS),
    CARD_3H(THREE, HEARTS),
    CARD_4H(FOUR, HEARTS),
    CARD_5H(FIVE, HEARTS),
    CARD_6H(SIX, HEARTS),
    CARD_7H(SEVEN, HEARTS),
    CARD_8H(EIGHT, HEARTS),
    CARD_9H(NINE, HEARTS),
    CARD_TH(TEN, HEARTS),
    CARD_JH(JACK, HEARTS),
    CARD_QH(QUEEN, HEARTS),
    CARD_KH(KING, HEARTS),

    CARD_AD(ACE, DIAMONDS),
    CARD_2D(TWO, DIAMONDS),
    CARD_3D(THREE, DIAMONDS),
    CARD_4D(FOUR, DIAMONDS),
    CARD_5D(FIVE, DIAMONDS),
    CARD_6D(SIX, DIAMONDS),
    CARD_7D(SEVEN, DIAMONDS),
    CARD_8D(EIGHT, DIAMONDS),
    CARD_9D(NINE, DIAMONDS),
    CARD_TD(TEN, DIAMONDS),
    CARD_JD(JACK, DIAMONDS),
    CARD_QD(QUEEN, DIAMONDS),
    CARD_KD(KING, DIAMONDS),

    CARD_AC(ACE, CLUBS),
    CARD_2C(TWO, CLUBS),
    CARD_3C(THREE, CLUBS),
    CARD_4C(FOUR, CLUBS),
    CARD_5C(FIVE, CLUBS),
    CARD_6C(SIX, CLUBS),
    CARD_7C(SEVEN, CLUBS),
    CARD_8C(EIGHT, CLUBS),
    CARD_9C(NINE, CLUBS),
    CARD_TC(TEN, CLUBS),
    CARD_JC(JACK, CLUBS),
    CARD_QC(QUEEN, CLUBS),
    CARD_KC(KING, CLUBS);

    val file = "card_" + name.takeLast(2).lowercase()
}

enum class CardState {
    DECK,
    DEALT,
    BOTTOM_ROW,
    MIDDLE_ROW,
    TOP_ROW;
}

enum class PokerCombination {
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