package com.example.fantasyland

import com.example.fantasyland.Card.*
import com.example.fantasyland.PokerCombination.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

// five card rows - bottom and middle row
class FiveCardRowsPokerCombinationsTest : StringSpec({
    "royal flush" {
        BottomRow(listOf(CARD_KS, CARD_AS, CARD_TS, CARD_QS, CARD_JS)).pokerCombination shouldBe ROYAL_FLUSH
    }

    "straight flush" {
        MiddleRow(listOf(CARD_KS, CARD_9S, CARD_TS, CARD_QS, CARD_JS)).pokerCombination shouldBe STRAIGHT_FLUSH
        BottomRow(listOf(CARD_4C, CARD_2C, CARD_AC, CARD_3C, CARD_5C)).pokerCombination shouldBe STRAIGHT_FLUSH
    }

    "quads" {
        MiddleRow(listOf(CARD_2S, CARD_AD, CARD_AH, CARD_AC, CARD_AS)).pokerCombination shouldBe QUADS
        BottomRow(listOf(CARD_4S, CARD_4C, CARD_9H, CARD_4H, CARD_4D)).pokerCombination shouldBe QUADS
    }

    "fullhouse" {
        MiddleRow(listOf(CARD_2S, CARD_AD, CARD_AH, CARD_2H, CARD_AS)).pokerCombination shouldBe FULL_HOUSE
    }

    "flush" {
        BottomRow(listOf(CARD_QS, CARD_KS, CARD_AS, CARD_2S, CARD_3S)).pokerCombination shouldBe FLUSH
    }

    "straigh" {
        MiddleRow(listOf(CARD_AD, CARD_JD, CARD_QD, CARD_KD, CARD_TC)).pokerCombination shouldBe STRAIGHT
        BottomRow(listOf(CARD_4S, CARD_2S, CARD_3S, CARD_AS, CARD_5C)).pokerCombination shouldBe STRAIGHT
    }

    "trips" {
        MiddleRow(listOf(CARD_9D, CARD_AS, CARD_AD, CARD_JD, CARD_AC)).pokerCombination shouldBe TRIPS
    }

    "two pairs" {
        BottomRow(listOf(CARD_9D, CARD_AS, CARD_JS, CARD_JD, CARD_AC)).pokerCombination shouldBe TWO_PAIRS
    }

    "pair" {
        MiddleRow(listOf(CARD_JS, CARD_AS, CARD_3S, CARD_4D, CARD_3C)).pokerCombination shouldBe PAIR
    }

    "high card" {
        BottomRow(listOf(CARD_4D, CARD_2S, CARD_3S, CARD_6D, CARD_AC)).pokerCombination shouldBe HIGH_CARD
        MiddleRow(listOf(CARD_7S, CARD_4S, CARD_3S, CARD_5D, CARD_2C)).pokerCombination shouldBe HIGH_CARD
    }
})

class TopRowPokerCombinationsTest : StringSpec({
    "trips" {
        TopRow(listOf(CARD_AS, CARD_AD, CARD_AH)).pokerCombination shouldBe TRIPS
        TopRow(listOf(CARD_TS, CARD_TD, CARD_TH)).pokerCombination shouldBe TRIPS
        TopRow(listOf(CARD_2S, CARD_2D, CARD_2H)).pokerCombination shouldBe TRIPS
    }

    "pair" {
        TopRow(listOf(CARD_AS, CARD_AD, CARD_3S)).pokerCombination shouldBe PAIR
        TopRow(listOf(CARD_AS, CARD_KD, CARD_KS)).pokerCombination shouldBe PAIR
        TopRow(listOf(CARD_2S, CARD_KD, CARD_2H)).pokerCombination shouldBe PAIR
    }

    "highcard" {
        TopRow(listOf(CARD_AS, CARD_2S, CARD_3S)).pokerCombination shouldBe HIGH_CARD
        TopRow(listOf(CARD_AD, CARD_KD, CARD_QD)).pokerCombination shouldBe HIGH_CARD
    }
})