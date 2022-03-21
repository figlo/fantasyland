package com.example.fantasyland

import com.example.fantasyland.Card.*
import com.example.fantasyland.PokerCombination.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TopRowTest : StringSpec({
    "highcard poker combinations" {
        TopRow(listOf(CARD_AS, CARD_2S, CARD_3S)).pokerCombination shouldBe HIGH_CARD
        TopRow(listOf(CARD_AS, CARD_KS, CARD_QS)).pokerCombination shouldBe HIGH_CARD
        TopRow(listOf(CARD_2C, CARD_3S, CARD_AH)).pokerCombination shouldBe HIGH_CARD
    }

    "pair poker combinations" {
        TopRow(listOf(CARD_AS, CARD_AD, CARD_3S)).pokerCombination shouldBe PAIR
        TopRow(listOf(CARD_AS, CARD_KD, CARD_KS)).pokerCombination shouldBe PAIR
        TopRow(listOf(CARD_2S, CARD_KD, CARD_2H)).pokerCombination shouldBe PAIR
    }

    "trips poker combinations" {
        TopRow(listOf(CARD_AS, CARD_AD, CARD_AH)).pokerCombination shouldBe TRIPS
        TopRow(listOf(CARD_TS, CARD_TD, CARD_TH)).pokerCombination shouldBe TRIPS
        TopRow(listOf(CARD_2S, CARD_2D, CARD_2H)).pokerCombination shouldBe TRIPS
    }
})

class BottomRowTest : StringSpec({
    "fullhouse poker combinations" {
        BottomRow(listOf(CARD_AS, CARD_AD, CARD_AH, CARD_2H, CARD_2S)).pokerCombination shouldBe FULL_HOUSE
        BottomRow(listOf(CARD_AS, CARD_AD, CARD_AH, CARD_2H, CARD_2D)).pokerCombination shouldBe FULL_HOUSE
    }
})