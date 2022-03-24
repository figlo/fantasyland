package com.example.fantasyland

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class IsRepeatedFantasyTest: StringSpec ({
    "bottom row full house" {
        val bottomRow = BottomRow(listOf(Card.CARD_QH, Card.CARD_QC, Card.CARD_QS, Card.CARD_3D, Card.CARD_3S).shuffled())
        val middleRow = MiddleRow(listOf(Card.CARD_2S, Card.CARD_2D, Card.CARD_TS, Card.CARD_8D, Card.CARD_3C).shuffled())
        val topRow = TopRow(listOf(Card.CARD_2H, Card.CARD_4H, Card.CARD_5C).shuffled())
        isRepeatedFantasy(bottomRow, middleRow, topRow) shouldBe false
    }

    "bottom row quads" {
        val bottomRow = BottomRow(listOf(Card.CARD_QH, Card.CARD_QC, Card.CARD_QS, Card.CARD_QD, Card.CARD_3S).shuffled())
        val middleRow = MiddleRow(listOf(Card.CARD_2S, Card.CARD_2D, Card.CARD_TS, Card.CARD_8D, Card.CARD_3C).shuffled())
        val topRow = TopRow(listOf(Card.CARD_2H, Card.CARD_4H, Card.CARD_5C).shuffled())
        isRepeatedFantasy(bottomRow, middleRow, topRow) shouldBe true
    }

    "top row pair of aces" {
        val bottomRow = BottomRow(listOf(Card.CARD_QH, Card.CARD_QC, Card.CARD_QS, Card.CARD_7D, Card.CARD_3S).shuffled())
        val middleRow = MiddleRow(listOf(Card.CARD_2S, Card.CARD_2D, Card.CARD_TS, Card.CARD_TD, Card.CARD_3C).shuffled())
        val topRow = TopRow(listOf(Card.CARD_AH, Card.CARD_AS, Card.CARD_5C).shuffled())
        isRepeatedFantasy(bottomRow, middleRow, topRow) shouldBe false
    }

    "top row trips" {
        val bottomRow = BottomRow(listOf(Card.CARD_QH, Card.CARD_QC, Card.CARD_QS, Card.CARD_7D, Card.CARD_3S).shuffled())
        val middleRow = MiddleRow(listOf(Card.CARD_2S, Card.CARD_6D, Card.CARD_TS, Card.CARD_TD, Card.CARD_TC).shuffled())
        val topRow = TopRow(listOf(Card.CARD_5H, Card.CARD_5S, Card.CARD_5C).shuffled())
        isRepeatedFantasy(bottomRow, middleRow, topRow) shouldBe true
    }
})