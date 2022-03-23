package com.example.fantasyland

import com.example.fantasyland.Card.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CardsPokerCombinationsTest: StringSpec ({
    "royal flush" {
        listOf(CARD_AS, CARD_KS, CARD_QS, CARD_JS, CARD_TS).shuffled().isRoyalFlush shouldBe true
    }

    "straight flush" {
        listOf(CARD_AS, CARD_2S, CARD_3S, CARD_4S, CARD_5S).shuffled().isStraightFlush shouldBe true
    }

    "flush" {
        listOf(CARD_AS, CARD_2S, CARD_3S, CARD_4S, CARD_6S).shuffled().isFlush shouldBe true
    }

    "straight 1" {
        listOf(CARD_AD, CARD_2S, CARD_3S, CARD_4S, CARD_5S).shuffled().isStraight shouldBe true
    }

    "straight 2" {
        listOf(CARD_KD, CARD_AS, CARD_2S, CARD_3S, CARD_4S).shuffled().isStraight shouldBe false
    }

    "quads" {
        listOf(CARD_KD, CARD_KS, CARD_KC, CARD_KH, CARD_4S).shuffled().isQuads shouldBe true
    }

    "full house" {
        listOf(CARD_KD, CARD_KS, CARD_KC, CARD_4H, CARD_4S).shuffled().isFullHouse shouldBe true
    }

    "trips" {
        listOf(CARD_KD, CARD_KS, CARD_KC, CARD_4H, CARD_5S).shuffled().isTrips shouldBe true
    }

    "two pairs" {
        listOf(CARD_KD, CARD_KS, CARD_4C, CARD_4H, CARD_5S).shuffled().isTwoPairs shouldBe true
    }

    "pair" {
        listOf(CARD_KD, CARD_KS, CARD_4C, CARD_6H, CARD_5S).shuffled().isPair shouldBe true
    }

    "high card" {
        listOf(CARD_KD, CARD_JS, CARD_4C, CARD_6H, CARD_5S).shuffled().isHighCard shouldBe true
    }
})