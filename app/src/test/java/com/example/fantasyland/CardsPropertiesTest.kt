package com.example.fantasyland

import com.example.fantasyland.Card.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CardsTest: StringSpec ({
    "number of faces 1" {
        listOf(CARD_AS).numberOfFaces shouldBe 1
    }

    "number of faces 2" {
        listOf(CARD_AS, CARD_AD).shuffled().numberOfFaces shouldBe 1
    }

    "number of faces 3" {
        listOf(CARD_AS, CARD_2D).shuffled().numberOfFaces shouldBe 2
    }

    "number of faces 4" {
        listOf(CARD_3S, CARD_3D, CARD_3H).shuffled().numberOfFaces shouldBe 1
    }

    "number of faces 5" {
        listOf(CARD_3S, CARD_3D, CARD_4H).shuffled().numberOfFaces shouldBe 2
    }

    "number of faces 6" {
        listOf(CARD_3S, CARD_4D, CARD_5H).shuffled().numberOfFaces shouldBe 3
    }

    "number of faces 7" {
        listOf(CARD_4S, CARD_4D, CARD_4H, CARD_4C).shuffled().numberOfFaces shouldBe 1
    }

    "number of faces 8" {
        listOf(CARD_AS, CARD_2D, CARD_3H, CARD_4C, CARD_5H).shuffled().numberOfFaces shouldBe 5
    }

    "is of different faces 1" {
        listOf(CARD_AS).shuffled().isOfDifferentFaces shouldBe true
    }

    "is of different faces 2" {
        listOf(CARD_AS, CARD_AD).shuffled().isOfDifferentFaces shouldBe false
    }

    "is of different faces 3" {
        listOf(CARD_AS, CARD_2D, CARD_3H, CARD_4C, CARD_5H).shuffled().isOfDifferentFaces shouldBe true
    }

    "is of one suit 1" {
        listOf(CARD_2S).shuffled().isOfOneSuit shouldBe true
    }

    "is of one suit 2" {
        listOf(CARD_AD, CARD_2D, CARD_3D, CARD_4D, CARD_8D, CARD_KD).shuffled().isOfOneSuit shouldBe true
    }

    "is of one suit 3" {
        listOf(CARD_AS, CARD_2D).shuffled().isOfOneSuit shouldBe false
    }
})