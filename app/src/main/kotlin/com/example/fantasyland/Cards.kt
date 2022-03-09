package com.example.fantasyland


// some helper properties, not poker combinations

val List<Card>.numberOfFaces
    get() = map { it.face }.distinct().count()

val List<Card>.isOfDifferentFaces: Boolean
    get() = numberOfFaces == size

val List<Card>.isOfOneSuit: Boolean
    get() {
        val numberOfSuits = map { it.suit }.distinct().count()
        return numberOfSuits == 1
    }

val List<Card>.facesHistogram: Map<CardFace, Int>
    get() = groupingBy { it.face }.eachCount()

val List<Card>.maxCountOfFaces: Int
    get() = facesHistogram.maxOf { it.value }

val List<Card>.isProgression: Boolean
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

val List<Card>.isWheel: Boolean
    get() = size == 5 &&
            isProgression &&
            !isOfOneSuit &&
            any { it.face == CardFace.ACE } &&
            any { it.face == CardFace.TWO }

val List<Card>.isSteelWheel: Boolean
    get() = size == 5 &&
            isProgression &&
            isOfOneSuit &&
            any { it.face == CardFace.ACE } &&
            any { it.face == CardFace.TWO }

val List<Card>.isAnyWheel: Boolean
    get() = isWheel || isSteelWheel


// poker combinations

val List<Card>.isRoyalFlush: Boolean
    get() = size == 5 &&
            isOfOneSuit &&
            isProgression &&
            any { it.face == CardFace.ACE } &&
            any { it.face == CardFace.KING }

val List<Card>.isStraightFlush: Boolean
    get() = size == 5 &&
            isOfOneSuit &&
            isProgression &&
            !isRoyalFlush

val List<Card>.isFlush: Boolean
    get() = size == 5 &&
            isOfOneSuit &&
            !isProgression

val List<Card>.isStraight: Boolean
    get() = size == 5 &&
            !isOfOneSuit &&
            isProgression

val List<Card>.isQuads: Boolean
    get() = size >= 4 &&
            numberOfFaces == size - 3 &&
            maxCountOfFaces == 4

val List<Card>.isFullHouse: Boolean
    get() = size >= 5 &&
            numberOfFaces == size - 3 &&
            maxCountOfFaces == 3

val List<Card>.isTrips: Boolean
    get() = size >= 3 &&
            numberOfFaces == size - 2 &&
            maxCountOfFaces == 3

val List<Card>.isTwoPairs: Boolean
    get() = size >= 4 &&
            numberOfFaces == size - 2 &&
            maxCountOfFaces == 2

val List<Card>.isPair: Boolean
    get() = size >= 2 &&
            numberOfFaces == size - 1 &&
            maxCountOfFaces == 2

val List<Card>.isHighCard: Boolean
    get() {
        return if (size == 5) {
            isOfDifferentFaces &&
                    !isOfOneSuit &&
                    !isProgression
        } else
            isOfDifferentFaces
    }


// sorting functions

var sortSwitch = true

fun List<Card>.sort(): List<Card> {
    return when (sortSwitch) {
        true -> sortByColorAndRank()
        false -> sortByRankAndColor()
    }.also { sortSwitch = !sortSwitch }
}

fun List<Card>.sortByRankAndColor() =
    sortedWith(
        compareBy(
            { -it.face.rankAceHigh },
            { it.suit }
        )
    )

fun List<Card>.sortByColorAndRank() =
    sortedWith(
        compareBy(
            { it.suit },
            { -it.face.rankAceHigh }
        )
    )

fun List<Card>.sortByCountAndRank() =
    sortedWith(
        compareBy(
            { card -> -this.count { otherCard -> card.face == otherCard.face } },
            { -it.face.rankAceHigh }
        )
    )

fun List<Card>.sortByRankAndColorAceLow() =
    sortedWith(
        compareBy(
            { -it.face.ordinal },
            { it.suit }
        )
    )