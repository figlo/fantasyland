package com.example.fantasyland

import android.graphics.Color
import android.widget.ImageView
import androidx.core.view.setPadding

class Tile(private val id: Int, val imageView: ImageView, var card: NewCard? = null) {
    private val isOnBoard = id <= 13

    fun onClickHandler() {
        if (selectedTile == null) select()
        else makeMove()
    }

    private fun select() {
        imageView.setBackgroundColor(Color.parseColor("#ff0000"))
        imageView.setPadding(4)

        selectedTile = this
    }

    private fun makeMove() {
        val selectedCard = selectedTile?.card
        selectedTile?.card = card
        selectedTile?.card?.cardState = if (selectedTile!!.isOnBoard) CardState.BOARD else CardState.DEALT
        card = selectedCard
        card?.cardState = if (isOnBoard) CardState.BOARD else CardState.DEALT

        val selectedTileTag = selectedTile?.imageView?.tag.toString().toInt()
        selectedTile?.imageView?.setImageResource(imageView.tag.toString().toInt())
        imageView.setImageResource(selectedTileTag)

        selectedTile?.imageView?.tag = imageView.tag
        imageView.tag = selectedTileTag

        selectedTile?.deSelect()
    }

    fun deSelect() {
        imageView.setBackgroundColor(Color.parseColor("#000000"))
        imageView.setPadding(1)

        selectedTile = null
    }

    companion object {
        var selectedTile: Tile? = null
    }
}