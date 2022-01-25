package com.example.fantasyland

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding

class Tile(private val id: Int, val imageView: ImageView, var card: NewCard? = null) {
    private val isOnBoard = id <= 13

    fun onClickHandler() {
        if (selectedTile == null) select()
        else makeMove()
    }

    private fun select() {
        with(imageView) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.cardViewSelected))
            setPadding(4)
        }

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
        with(imageView) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.cardViewBackground))
            setPadding(1)
        }

        selectedTile = null
    }

    companion object {
        var selectedTile: Tile? = null
    }
}