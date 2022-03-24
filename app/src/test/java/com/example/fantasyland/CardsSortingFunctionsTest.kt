package com.example.fantasyland

import com.example.fantasyland.Card.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CardsSortingFunctionsTest: StringSpec ({
    "sort by rank and color 1" {
        listOf(CARD_8D, CARD_8S, CARD_8H, CARD_8C, CARD_TC).shuffled().sortByRankAndColor() shouldBe listOf(CARD_TC, CARD_8S, CARD_8H, CARD_8D, CARD_8C)
    }

    "sort by rank and color 2" {
        listOf(CARD_8D, CARD_8S, CARD_QH, CARD_QS, CARD_TC).shuffled().sortByRankAndColor() shouldBe listOf(CARD_QS, CARD_QH, CARD_TC, CARD_8S, CARD_8D)
    }

    "sort by color and rank 1" {
        listOf(CARD_8D, CARD_8S, CARD_8H, CARD_8C, CARD_TC).shuffled().sortByColorAndRank() shouldBe listOf(CARD_8S, CARD_8H, CARD_8D, CARD_TC, CARD_8C)
    }

    "sort by color and rank 2" {
        listOf(CARD_8D, CARD_8S, CARD_QH, CARD_QS, CARD_TC).shuffled().sortByColorAndRank() shouldBe listOf(CARD_QS, CARD_8S, CARD_QH, CARD_8D, CARD_TC)
    }

    "sort by count rank and color 1" {
        listOf(CARD_8D, CARD_8S, CARD_8H, CARD_8C, CARD_TC).shuffled().sortByCountRankAndColor() shouldBe listOf(CARD_8S, CARD_8H, CARD_8D, CARD_8C, CARD_TC)
    }

    "sort by count rank and color 2" {
        listOf(CARD_8D, CARD_8S, CARD_QH, CARD_QS, CARD_TC).shuffled().sortByCountRankAndColor() shouldBe listOf(CARD_QS, CARD_QH, CARD_8S, CARD_8D, CARD_TC)
    }

    "sort by rank and color ace low 1" {
        listOf(CARD_8D, CARD_8S, CARD_8H, CARD_8C, CARD_AC).shuffled().sortByRankAndColorAceLow() shouldBe listOf(CARD_8S, CARD_8H, CARD_8D, CARD_8C, CARD_AC)
    }

    "sort by rank and color ace low 2" {
        listOf(CARD_8D, CARD_8S, CARD_AH, CARD_AC, CARD_9C).shuffled().sortByRankAndColorAceLow() shouldBe listOf(CARD_9C, CARD_8S, CARD_8D, CARD_AH, CARD_AC)
    }
})