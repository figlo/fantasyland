package com.example.fantasyland

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding

class Tile(private val id: Int, val imageView: ImageView, var card: NewCard? = null) {
    val isOnBoard = id <= 13

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

    fun makeMove() {
        // swap cards
        selectedTile?.card = card.also { card = selectedTile?.card }

        selectedTile?.card?.cardState = if (selectedTile!!.isOnBoard) CardState.BOARD else CardState.DEALT
        card?.cardState = if (isOnBoard) CardState.BOARD else CardState.DEALT

        // swap card images (including tags)
        selectedTile?.imageView?.tag = imageView.tag.also { imageView.tag = selectedTile?.imageView?.tag}

        selectedTile?.imageView?.setImageResource(selectedTile?.imageView?.tag.toString().toInt())
        imageView.setImageResource(imageView.tag.toString().toInt())

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