package com.example.fantasyland

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding

class Tile(id: Int, val imageView: ImageView) {
    private val isOnBottomRow = id in 1..5
    private val isOnMiddleRow = id in 6..10
    private val isOnTopRow = id in 11..13

    var tag : MutableMap<Any?, Any?>
    get() = imageView.tag as MutableMap<Any?, Any?>
    set(value) {
        imageView.tag = mutableMapOf("card" to value["card"], "imageResource" to value["imageResource"])
    }

    var card: Card?
        get() = tag["card"] as Card?
        set(value) {
            var tag = imageView.tag as MutableMap<Any?, Any?>
            val imageResource = tag["imageResource"] as Int
            imageView.tag = mutableMapOf("card" to value, "imageResource" to imageResource)
        }

    var imageResource: Int
        get() = tag["imageResource"] as Int
        set(value) {
            var tag = imageView.tag as MutableMap<Any?, Any?>
            val card = tag["card"] as Card?
            imageView.tag = mutableMapOf("card" to card, "imageResource" to value)
        }

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
        // swap tags
        selectedTile?.imageView?.tag = imageView.tag.also { imageView.tag = selectedTile?.imageView?.tag }

        // set new imageResources
        selectedTile?.imageView?.setImageResource(selectedTile!!.imageResource).also { imageView.setImageResource(imageResource) }

        // update cards state
        card?.cardState = when {
            isOnBottomRow -> CardState.BOTTOM_ROW
            isOnMiddleRow -> CardState.MIDDLE_ROW
            isOnTopRow    -> CardState.TOP_ROW
            else          -> CardState.DEALT
        }

        selectedTile?.card?.cardState = when {
            selectedTile!!.isOnBottomRow -> CardState.BOTTOM_ROW
            selectedTile!!.isOnMiddleRow -> CardState.MIDDLE_ROW
            selectedTile!!.isOnTopRow    -> CardState.TOP_ROW
            else                         -> CardState.DEALT
        }

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

        val isMovingPhaseDone: Boolean
            get() = Card.values().count { it.cardState == CardState.BOTTOM_ROW } == 5 &&
                    Card.values().count { it.cardState == CardState.MIDDLE_ROW } == 5 &&
                    Card.values().count { it.cardState == CardState.TOP_ROW } == 3
    }
}