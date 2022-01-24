package com.example.fantasyland

import android.graphics.Color
import android.widget.ImageView
import androidx.core.view.setPadding

class Tile(val id: String, val imageView: ImageView) {
    var card: Card? = null
    var isSelected = false

    fun select() {
        selectedTile?.deSelect()

        imageView.setBackgroundColor(Color.parseColor("#ff0000"))
        imageView.setPadding(4)

        selectedTile = this
    }

    fun deSelect() {
        imageView.setBackgroundColor(Color.parseColor("#000000"))
        imageView.setPadding(1)
    }

    fun addCard(card: NewCard) {
        // TODO
    }

    fun remove(card: NewCard) {
        // TODO
    }

    companion object {
        var selectedTile: Tile? = null
    }
}