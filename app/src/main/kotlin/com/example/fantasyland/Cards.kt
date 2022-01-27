package com.example.fantasyland

open class Cards(val cards: MutableList<Card>) {
    protected fun sortByCountAndRank() =
        cards.sortWith(
            compareBy(
                { card -> -cards.count { otherCard -> card.face == otherCard.face } },
                { -it.face.rankAceHigh }
            )
        )
}