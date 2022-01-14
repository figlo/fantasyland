package com.example.fantasyland

data class Card(val face: CardFace, val suit: CardSuit, val file: String) {
}

open class Cards(val cards: MutableList<Card>) {
    protected fun sortByCountAndRank() =
        cards.sortWith(
            compareBy(
                { card -> -cards.count { otherCard -> card.face == otherCard.face } },
                { -it.face.rankAceHigh }
            )
        )
}