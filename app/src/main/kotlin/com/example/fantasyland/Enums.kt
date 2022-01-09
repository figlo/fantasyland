package com.example.fantasyland

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