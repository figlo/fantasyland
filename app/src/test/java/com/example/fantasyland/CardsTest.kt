package com.example.fantasyland

import com.example.fantasyland.Card.*
import com.example.fantasyland.PokerCombination.FULL_HOUSE
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CardsTest : StringSpec({
    "poker combination fullhouse" {
            BottomRow(listOf(CARD_AS, CARD_AD, CARD_AH, CARD_2H, CARD_2S)).pokerCombination shouldBe FULL_HOUSE
    }
})