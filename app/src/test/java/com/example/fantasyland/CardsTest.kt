package com.example.fantasyland

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly

class CardsTest : StringSpec({
    "my first test" {
//        1 + 2 shouldBe 3
        val deck = Deck()
        deck.loadFull()
        deck.cards.count() shouldBeExactly 52
        deck.cards.distinct().count() shouldBeExactly 52
    }
})