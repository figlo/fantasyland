package com.example.fantasyland

import com.example.fantasyland.Card.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TopRowValuesTest: StringSpec({
    "trips" {
        TopRow(listOf(CARD_AS, CARD_AD, CARD_AH)).value() shouldBe 22
        TopRow(listOf(CARD_2S, CARD_2D, CARD_2H)).value() shouldBe 10
    }

    "pair" {
        TopRow(listOf(CARD_AD, CARD_AS, CARD_3S).shuffled()).value() shouldBe 9
        TopRow(listOf(CARD_6S, CARD_6D, CARD_KS).shuffled()).value() shouldBe 1
        TopRow(listOf(CARD_5S, CARD_5D, CARD_AD).shuffled()).value() shouldBe 0
    }
})