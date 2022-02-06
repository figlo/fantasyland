package com.example.fantasyland

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding

var selectedTile: Tile? = null
var isFullBoard = false

class Tile(id: Int, val imageView: ImageView, var card: Card? = null) {
    private val isOnBottomRow = id in 1..5
    private val isOnMiddleRow = id in 6..10
    private val isOnTopRow = id in 11..13

    fun onClickHandler() {
        val isSomeTileSelected = selectedTile != null

        if (isSomeTileSelected) {
            val isThisTileSelected = selectedTile == this
            if (isThisTileSelected) {
                deSelect()
            } else {
                makeMove()
            }
        } else {
            val isCardOnThisTile = card != null
            if (isCardOnThisTile) select()
        }
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

        selectedTile?.card?.cardState = when {
            selectedTile!!.isOnBottomRow -> CardState.BOTTOM_ROW
            selectedTile!!.isOnMiddleRow -> CardState.MIDDLE_ROW
            selectedTile!!.isOnTopRow    -> CardState.TOP_ROW
            else                         -> CardState.DEALT
        }

        card?.cardState = when {
            isOnBottomRow -> CardState.BOTTOM_ROW
            isOnMiddleRow -> CardState.MIDDLE_ROW
            isOnTopRow    -> CardState.TOP_ROW
            else          -> CardState.DEALT
        }

        // swap card images (including tags)
        selectedTile?.imageView?.tag = imageView.tag.also { imageView.tag = selectedTile?.imageView?.tag }

        selectedTile?.imageView?.setImageResource(selectedTile?.imageView?.tag.toString().toInt())
        imageView.setImageResource(imageView.tag.toString().toInt())

        selectedTile?.deSelect()

        isFullBoard = Card.values().count { it.cardState == CardState.BOTTOM_ROW } == 5 &&
                Card.values().count { it.cardState == CardState.MIDDLE_ROW } == 5 &&
                Card.values().count { it.cardState == CardState.TOP_ROW } == 3
    }

    fun deSelect() {
        with(imageView) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.cardViewBackground))
            setPadding(1)
        }

        selectedTile = null
    }
}