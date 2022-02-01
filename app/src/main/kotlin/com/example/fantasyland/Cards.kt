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

var sortSwitch = true

fun MutableList<Card>.sort() {
    when (sortSwitch) {
        true -> sortByColorAndRank()
        false -> sortByRankAndColor()
    }
    sortSwitch = !sortSwitch
}

fun MutableList<Card>.sortByRankAndColor() =
        sortWith(
            compareBy(
                { -it.face.rankAceHigh },
                { it.suit }
            )
        )

fun MutableList<Card>.sortByColorAndRank() =
        sortWith(
            compareBy(
                { it.suit },
                { -it.face.rankAceHigh }
            )
        )

fun dealCard(): Card {
    val dealtCard =
        Card.values()
            .filter { it.cardState == CardState.DECK }
            .random()
    dealtCard.cardState = CardState.DEALT
    return dealtCard
}