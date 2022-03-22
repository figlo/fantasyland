package com.example.fantasyland

import com.example.fantasyland.Card.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CompareMiddleAndBottomRowsTest: StringSpec ({
    // different poker combinations
    "royal flush vs high card" {
        val middleRowCards= listOf(CARD_AD, CARD_KD, CARD_QD, CARD_JD, CARD_TD).shuffled()
        val bottomRowCards= listOf(CARD_AS, CARD_KS, CARD_QS, CARD_JS, CARD_9H).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe true
    }

    "straight vs flush" {
        val middleRowCards= listOf(CARD_KD, CARD_QD, CARD_JD, CARD_TD, CARD_9H).shuffled()
        val bottomRowCards= listOf(CARD_2S, CARD_3S, CARD_4S, CARD_5S, CARD_7S).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe false
    }

    // the same poker combinations
    "royal flush vs royal flush" {
        val middleRowCards= listOf(CARD_AD, CARD_KD, CARD_QD, CARD_JD, CARD_TD).shuffled()
        val bottomRowCards= listOf(CARD_AS, CARD_KS, CARD_QS, CARD_JS, CARD_TS).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe false
    }

    "straight flush vs straight flush" {
        val middleRowCards= listOf(CARD_KD, CARD_QD, CARD_JD, CARD_TD, CARD_9D).shuffled()
        val bottomRowCards= listOf(CARD_KS, CARD_QS, CARD_JS, CARD_TS, CARD_9S).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe false
    }

    "straight flush vs straight flush with ace" {
        val middleRowCards= listOf(CARD_KD, CARD_QD, CARD_JD, CARD_TD, CARD_9D).shuffled()
        val bottomRowCards= listOf(CARD_AS, CARD_2S, CARD_3S, CARD_4S, CARD_5S).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe true
    }

    "quads vs quads" {
        val middleRowCards= listOf(CARD_AD, CARD_AS, CARD_AS, CARD_AC, CARD_TD).shuffled()
        val bottomRowCards= listOf(CARD_2D, CARD_2S, CARD_2S, CARD_2C, CARD_TS).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe true
    }

    "full house vs full house" {
        val middleRowCards= listOf(CARD_2D, CARD_2S, CARD_2C, CARD_AC, CARD_AS).shuffled()
        val bottomRowCards= listOf(CARD_3D, CARD_3S, CARD_3C, CARD_4C, CARD_4S).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe false
    }

    "flush vs flush" {
        val middleRowCards= listOf(CARD_KD, CARD_QD, CARD_JD, CARD_TD, CARD_8D).shuffled()
        val bottomRowCards= listOf(CARD_KS, CARD_QS, CARD_JS, CARD_TS, CARD_8S).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe false
    }

    "flush vs flush with ace" {
        val middleRowCards= listOf(CARD_KD, CARD_QD, CARD_JD, CARD_TD, CARD_8D).shuffled()
        val bottomRowCards= listOf(CARD_AS, CARD_2S, CARD_3S, CARD_4S, CARD_6S).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe false
    }

    "straight vs straight" {
        val middleRowCards= listOf(CARD_KD, CARD_QD, CARD_JD, CARD_TD, CARD_9C).shuffled()
        val bottomRowCards= listOf(CARD_KS, CARD_QS, CARD_JS, CARD_TS, CARD_9H).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe false
    }

    "straight vs straight with ace" {
        val middleRowCards= listOf(CARD_KD, CARD_QD, CARD_JD, CARD_TD, CARD_9C).shuffled()
        val bottomRowCards= listOf(CARD_AS, CARD_2S, CARD_3S, CARD_4S, CARD_5C).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe true
    }

    "trips vs trips" {
        val middleRowCards= listOf(CARD_AD, CARD_AS, CARD_AS, CARD_JC, CARD_TD).shuffled()
        val bottomRowCards= listOf(CARD_2D, CARD_2S, CARD_2S, CARD_3C, CARD_TS).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe true
    }

    "two pairs vs two pairs" {
        val middleRowCards= listOf(CARD_AD, CARD_AS, CARD_JS, CARD_JC, CARD_TD).shuffled()
        val bottomRowCards= listOf(CARD_KD, CARD_KS, CARD_QS, CARD_QC, CARD_TS).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe true
    }

    "two pairs vs two pairs the same kicker" {
        val middleRowCards= listOf(CARD_AD, CARD_AS, CARD_JS, CARD_JC, CARD_TD).shuffled()
        val bottomRowCards= listOf(CARD_AC, CARD_AH, CARD_JH, CARD_JD, CARD_TS).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe false
    }

    "two pairs vs two pairs different kicker" {
        val middleRowCards= listOf(CARD_AD, CARD_AS, CARD_JS, CARD_JC, CARD_TD).shuffled()
        val bottomRowCards= listOf(CARD_AC, CARD_AH, CARD_JH, CARD_JD, CARD_9S).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe true
    }

    "pair vs pair" {
        val middleRowCards= listOf(CARD_AD, CARD_AS, CARD_3S, CARD_4C, CARD_5D).shuffled()
        val bottomRowCards= listOf(CARD_2D, CARD_2S, CARD_8S, CARD_9C, CARD_TS).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe true
    }

    "pair vs pair the same kickers" {
        val middleRowCards= listOf(CARD_AD, CARD_AS, CARD_3S, CARD_9S, CARD_TD).shuffled()
        val bottomRowCards= listOf(CARD_AH, CARD_AC, CARD_3D, CARD_9C, CARD_TS).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe false
    }

    "pair vs pair different kickers" {
        val middleRowCards= listOf(CARD_AD, CARD_AS, CARD_3S, CARD_4C, CARD_5D).shuffled()
        val bottomRowCards= listOf(CARD_AH, CARD_AC, CARD_8S, CARD_9C, CARD_TS).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe false
    }

    "high card vs high card" {
        val middleRowCards= listOf(CARD_AD, CARD_JS, CARD_3S, CARD_4C, CARD_5D).shuffled()
        val bottomRowCards= listOf(CARD_KH, CARD_QC, CARD_8S, CARD_9C, CARD_TS).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe true
    }

    "high card vs the same high card" {
        val middleRowCards= listOf(CARD_AD, CARD_KS, CARD_3S, CARD_4C, CARD_5D).shuffled()
        val bottomRowCards= listOf(CARD_AH, CARD_KC, CARD_8S, CARD_9C, CARD_TS).shuffled()
        MiddleRow(middleRowCards) isHigherThan BottomRow(bottomRowCards) shouldBe false
    }
})

class CompareTopAndMiddleRowsTest: StringSpec ({
    // different poker combinations
    "trips vs straight" {
        val topRowCards= listOf(CARD_AD, CARD_AS, CARD_AS).shuffled()
        val middleRowCards= listOf(CARD_2D, CARD_3S, CARD_4S, CARD_5C, CARD_6S).shuffled()
        TopRow(topRowCards) isHigherThan MiddleRow(middleRowCards) shouldBe false
    }

    "pair vs two pairs" {
        val topRowCards= listOf(CARD_AD, CARD_AS, CARD_KS).shuffled()
        val middleRowCards= listOf(CARD_2D, CARD_2S, CARD_3S, CARD_3C, CARD_6S).shuffled()
        TopRow(topRowCards) isHigherThan MiddleRow(middleRowCards) shouldBe false
    }

    "high card vs pair" {
        val topRowCards= listOf(CARD_AD, CARD_KS, CARD_QS).shuffled()
        val middleRowCards= listOf(CARD_2D, CARD_2S, CARD_3S, CARD_4C, CARD_6S).shuffled()
        TopRow(topRowCards) isHigherThan MiddleRow(middleRowCards) shouldBe false
    }

    // the same poker combinations
    "trips vs trips" {
        val topRowCards= listOf(CARD_AD, CARD_AS, CARD_AS).shuffled()
        val middleRowCards= listOf(CARD_2D, CARD_2S, CARD_2S, CARD_3C, CARD_TS).shuffled()
        TopRow(topRowCards) isHigherThan MiddleRow(middleRowCards) shouldBe true
    }

    "pair vs pair" {
        val topRowCards= listOf(CARD_KD, CARD_KS, CARD_QS).shuffled()
        val middleRowCards= listOf(CARD_AC, CARD_AS, CARD_3S, CARD_4C, CARD_6S).shuffled()
        TopRow(topRowCards) isHigherThan MiddleRow(middleRowCards) shouldBe false
    }

    "pair vs pair the same kicker" {
        val topRowCards= listOf(CARD_AD, CARD_AH, CARD_TS).shuffled()
        val middleRowCards= listOf(CARD_AC, CARD_AS, CARD_TH, CARD_4C, CARD_6S).shuffled()
        TopRow(topRowCards) isHigherThan MiddleRow(middleRowCards) shouldBe false
    }

    "pair vs pair different kicker" {
        val topRowCards= listOf(CARD_AD, CARD_AH, CARD_TS).shuffled()
        val middleRowCards= listOf(CARD_AC, CARD_AS, CARD_JH, CARD_TC, CARD_6S).shuffled()
        TopRow(topRowCards) isHigherThan MiddleRow(middleRowCards) shouldBe false
    }

    "pair vs pair different kicker 2" {
        val topRowCards= listOf(CARD_AD, CARD_AH, CARD_TS).shuffled()
        val middleRowCards= listOf(CARD_AC, CARD_AS, CARD_9H, CARD_8C, CARD_6S).shuffled()
        TopRow(topRowCards) isHigherThan MiddleRow(middleRowCards) shouldBe true
    }

    "high card vs high card" {
        val topRowCards= listOf(CARD_AD, CARD_3S, CARD_2S).shuffled()
        val middleRowCards= listOf(CARD_KC, CARD_QD, CARD_JH, CARD_4C, CARD_6S).shuffled()
        TopRow(topRowCards) isHigherThan MiddleRow(middleRowCards) shouldBe true
    }

    "high card vs high card the same kicker" {
        val topRowCards= listOf(CARD_AD, CARD_KS, CARD_QS).shuffled()
        val middleRowCards= listOf(CARD_AC, CARD_KD, CARD_QH, CARD_4C, CARD_6S).shuffled()
        TopRow(topRowCards) isHigherThan MiddleRow(middleRowCards) shouldBe false
    }

    "high card vs high card the same kicker 2" {
        val topRowCards= listOf(CARD_AD, CARD_KS, CARD_QS).shuffled()
        val middleRowCards= listOf(CARD_AC, CARD_KD, CARD_JH, CARD_4C, CARD_6S).shuffled()
        TopRow(topRowCards) isHigherThan MiddleRow(middleRowCards) shouldBe true
    }

    "high card vs high card the same kicker 3" {
        val topRowCards= listOf(CARD_AD, CARD_KS, CARD_QS).shuffled()
        val middleRowCards= listOf(CARD_AC, CARD_QD, CARD_JH, CARD_4C, CARD_6S).shuffled()
        TopRow(topRowCards) isHigherThan MiddleRow(middleRowCards) shouldBe true
    }
})