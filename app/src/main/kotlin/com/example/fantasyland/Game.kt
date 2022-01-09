package com.example.fantasyland

object Game {
    val deck = Deck()

    fun start() {
        deck.loadFull()
        deck.cards.shuffle()
    }
}