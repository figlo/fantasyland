package com.example.fantasyland

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

fun MutableList<Card>.sortByCountAndRank() =
    sortWith(
        compareBy(
            { card -> -this.count { otherCard -> card.face == otherCard.face } },
            { -it.face.rankAceHigh }
        )
    )

fun MutableList<Card>.sortByRankAceLow() =
    sortWith(
        compareBy(
            { -it.face.ordinal },
            { it.suit }
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