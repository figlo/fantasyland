package com.example.fantasyland

import com.example.fantasyland.Card.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class IsValidResultTest: StringSpec ({
    "misset middle row" {
        val bottomRow = BottomRow(listOf(CARD_AH, CARD_QC, CARD_TC, CARD_8S, CARD_3S).shuffled())
        val middleRow = MiddleRow(listOf(CARD_2S, CARD_2D, CARD_TS, CARD_8D, CARD_3C).shuffled())
        val topRow = TopRow(listOf(CARD_2H, CARD_4H, CARD_5C).shuffled())
        isValidResult(bottomRow, middleRow, topRow) shouldBe false
    }

    "misset top row" {
        val bottomRow = BottomRow(listOf(CARD_AH, CARD_AC, CARD_TC, CARD_8S, CARD_3S).shuffled())
        val middleRow = MiddleRow(listOf(CARD_2S, CARD_2D, CARD_TS, CARD_8D, CARD_3C).shuffled())
        val topRow = TopRow(listOf(CARD_2H, CARD_5H, CARD_5C).shuffled())
        isValidResult(bottomRow, middleRow, topRow) shouldBe false
    }

    "the same bottom and middle row" {
        val bottomRow = BottomRow(listOf(CARD_AH, CARD_AC, CARD_TC, CARD_8S, CARD_3S).shuffled())
        val middleRow = MiddleRow(listOf(CARD_AS, CARD_AH, CARD_TS, CARD_8D, CARD_3C).shuffled())
        val topRow = TopRow(listOf(CARD_2H, CARD_5H, CARD_5C).shuffled())
        isValidResult(bottomRow, middleRow, topRow) shouldBe true
    }

    "the same middle and top row" {
        val bottomRow = BottomRow(listOf(CARD_AH, CARD_AC, CARD_TC, CARD_8S, CARD_3S).shuffled())
        val middleRow = MiddleRow(listOf(CARD_5S, CARD_5D, CARD_KS, CARD_8D, CARD_3C).shuffled())
        val topRow = TopRow(listOf(CARD_KH, CARD_5H, CARD_5C).shuffled())
        isValidResult(bottomRow, middleRow, topRow) shouldBe true
    }

    "middle row better kicker vs bottom row" {
        val bottomRow = BottomRow(listOf(CARD_AH, CARD_AC, CARD_TC, CARD_8S, CARD_3S).shuffled())
        val middleRow = MiddleRow(listOf(CARD_AS, CARD_AH, CARD_JS, CARD_8D, CARD_3C).shuffled())
        val topRow = TopRow(listOf(CARD_2H, CARD_5H, CARD_5C).shuffled())
        isValidResult(bottomRow, middleRow, topRow) shouldBe false
    }

    "top row better kicker vs middle row" {
        val bottomRow = BottomRow(listOf(CARD_AH, CARD_AC, CARD_TC, CARD_8S, CARD_3S).shuffled())
        val middleRow = MiddleRow(listOf(CARD_5S, CARD_5D, CARD_QS, CARD_8D, CARD_3C).shuffled())
        val topRow = TopRow(listOf(CARD_KH, CARD_5H, CARD_5C).shuffled())
        isValidResult(bottomRow, middleRow, topRow) shouldBe false
    }
})