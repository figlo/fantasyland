package com.example.fantasyland


// some basic properties, not poker combinations

val MutableList<Card>.numberOfFaces
    get() = map { it.face }.distinct().count()

val MutableList<Card>.isOfDifferentFaces: Boolean
    get() = numberOfFaces == size

val MutableList<Card>.isOfOneColor: Boolean
    get() {
        val numberOfSuits = map { it.suit }.distinct().count()
        return numberOfSuits == 1
    }

val MutableList<Card>.isProgression: Boolean
    get() {
        if (!isOfDifferentFaces) return false

        val minFaceAceHigh = map { it.face.rankAceHigh }.minOrNull()
        val maxFaceAceHigh = map { it.face.rankAceHigh }.maxOrNull()
        if (maxFaceAceHigh!! - minFaceAceHigh!! == size - 1) return true

        val minFace = map { it.face.ordinal }.minOrNull()
        val maxFace = map { it.face.ordinal }.maxOrNull()
        if (maxFace!! - minFace!! == size - 1) return true

        return false
    }


// poker combinations properties

val MutableList<Card>.isRoyalFlush: Boolean
    get() = size == 5 &&
            isOfOneColor &&
            isProgression &&
            any { it.face == CardFace.ACE } &&
            any { it.face == CardFace.KING }

val MutableList<Card>.isStraightFlush: Boolean
    get() = size == 5 &&
            isOfOneColor &&
            isProgression &&
            !isRoyalFlush

val MutableList<Card>.isFlush: Boolean
    get() = size == 5 &&
            isOfOneColor &&
            !isProgression

val MutableList<Card>.isStraight: Boolean
    get() = size == 5 &&
            !isOfOneColor &&
            isProgression

val MutableList<Card>.isHighCard: Boolean
    get() = size == 5 &&
            !isOfOneColor &&
            !isProgression

//val MutableList<Card>.isQuads: Boolean
//    get() = size == 5 &&
//            numberOfFaces == 2 &&
//            maxOf { goupBy { it.face }}


// sorting functions

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

fun MutableList<Card>.sortByRankAndColorAceLow() =
    sortWith(
        compareBy(
            { -it.face.ordinal },
            { it.suit }
        )
    )


// other

fun dealCard(): Card {
    val dealtCard =
        Card.values()
            .filter { it.cardState == CardState.DECK }
            .random()
    dealtCard.cardState = CardState.DEALT
    return dealtCard
}