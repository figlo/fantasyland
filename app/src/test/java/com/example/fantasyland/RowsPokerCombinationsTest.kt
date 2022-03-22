package com.example.fantasyland

import com.example.fantasyland.Card.*
import com.example.fantasyland.PokerCombination.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

// five card rows - bottom and middle row
class FiveCardRowsPokerCombinationsTest : StringSpec({
    "royal flush" {
        BottomRow(listOf(CARD_AS, CARD_KS, CARD_QS, CARD_JS, CARD_TS).shuffled()).pokerCombination shouldBe ROYAL_FLUSH
    }

    "straight flush" {
        MiddleRow(listOf(CARD_9S, CARD_TS, CARD_JS, CARD_QS, CARD_KS).shuffled()).pokerCombination shouldBe STRAIGHT_FLUSH
        BottomRow(listOf(CARD_AC, CARD_2C, CARD_3C, CARD_4C, CARD_5C).shuffled()).pokerCombination shouldBe STRAIGHT_FLUSH
    }

    "quads" {
        MiddleRow(listOf(CARD_AS, CARD_AD, CARD_AH, CARD_AC, CARD_2S).shuffled()).pokerCombination shouldBe QUADS
        BottomRow(listOf(CARD_4S, CARD_4C, CARD_4D, CARD_4H, CARD_9D).shuffled()).pokerCombination shouldBe QUADS
    }

    "fullhouse" {
        MiddleRow(listOf(CARD_AS, CARD_AD, CARD_AH, CARD_2H, CARD_2S).shuffled()).pokerCombination shouldBe FULL_HOUSE
    }

    "flush" {
        BottomRow(listOf(CARD_QS, CARD_KS, CARD_AS, CARD_2S, CARD_3S).shuffled()).pokerCombination shouldBe FLUSH
    }

    "straigh" {
        MiddleRow(listOf(CARD_AD, CARD_KD, CARD_QD, CARD_JD, CARD_TC).shuffled()).pokerCombination shouldBe STRAIGHT
        BottomRow(listOf(CARD_AS, CARD_2S, CARD_3S, CARD_4S, CARD_5C).shuffled()).pokerCombination shouldBe STRAIGHT
    }

    "trips" {
        MiddleRow(listOf(CARD_AD, CARD_AC, CARD_AS, CARD_JD, CARD_9C).shuffled()).pokerCombination shouldBe TRIPS
    }

    "two pairs" {
        BottomRow(listOf(CARD_JD, CARD_JS, CARD_9S, CARD_9D, CARD_AC).shuffled()).pokerCombination shouldBe TWO_PAIRS
    }

    "pair" {
        MiddleRow(listOf(CARD_JS, CARD_JD, CARD_8S, CARD_4D, CARD_3C).shuffled()).pokerCombination shouldBe PAIR
    }

    "high card" {
        BottomRow(listOf(CARD_AC, CARD_2S, CARD_3S, CARD_6D, CARD_4D).shuffled()).pokerCombination shouldBe HIGH_CARD
        MiddleRow(listOf(CARD_7S, CARD_5S, CARD_4S, CARD_3D, CARD_2C).shuffled()).pokerCombination shouldBe HIGH_CARD
    }
})

class TopRowPokerCombinationsTest : StringSpec({
    "trips" {
        TopRow(listOf(CARD_AS, CARD_AD, CARD_AH)).pokerCombination shouldBe TRIPS
        TopRow(listOf(CARD_TS, CARD_TD, CARD_TH)).pokerCombination shouldBe TRIPS
        TopRow(listOf(CARD_2S, CARD_2D, CARD_2H)).pokerCombination shouldBe TRIPS
    }

    "pair" {
        TopRow(listOf(CARD_AS, CARD_AD, CARD_3S).shuffled()).pokerCombination shouldBe PAIR
        TopRow(listOf(CARD_2S, CARD_2D, CARD_KH).shuffled()).pokerCombination shouldBe PAIR
    }

    "highcard" {
        TopRow(listOf(CARD_AS, CARD_2S, CARD_3S).shuffled()).pokerCombination shouldBe HIGH_CARD
        TopRow(listOf(CARD_AD, CARD_KD, CARD_QD).shuffled()).pokerCombination shouldBe HIGH_CARD
    }
})