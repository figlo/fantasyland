package com.example.fantasyland

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.example.fantasyland.CardState.*

class Tile(id: Int, val imageView: ImageView) {
    private val isOnBottomRow = id in 1..5
    private val isOnMiddleRow = id in 6..10
    private val isOnTopRow = id in 11..13

    private var tag: MutableMap<String, Any?>
        @Suppress("UNCHECKED_CAST")
        get() = imageView.tag as MutableMap<String, Any?>
        set(value) {
            imageView.tag = mutableMapOf("card" to value["card"], "imageResource" to value["imageResource"])
        }

    var card: Card?
        get() = tag["card"] as Card?
        @Suppress("UNCHECKED_CAST")
        set(value) {
            val tag = imageView.tag as MutableMap<String, Any?>
            tag["card"] = value
        }

    private var imageResource: Int
        get() = tag["imageResource"] as Int
        @Suppress("UNCHECKED_CAST")
        set(value) {
            val tag = imageView.tag as MutableMap<String, Any?>
            tag["imageResource"] = value
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

        // set new imageResources (from already swapped tags)
        selectedTile?.imageView?.setImageResource(selectedTile!!.imageResource)
        imageView.setImageResource(imageResource)

        // update cards state
        card?.cardState = when {
            isOnBottomRow -> BOTTOM_ROW
            isOnMiddleRow -> MIDDLE_ROW
            isOnTopRow    -> TOP_ROW
            else          -> DEALT
        }

        selectedTile?.card?.cardState = when {
            selectedTile!!.isOnBottomRow -> BOTTOM_ROW
            selectedTile!!.isOnMiddleRow -> MIDDLE_ROW
            selectedTile!!.isOnTopRow    -> TOP_ROW
            else                         -> DEALT
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
            get() = Card.values().count { it.cardState == BOTTOM_ROW } == 5 &&
                    Card.values().count { it.cardState == MIDDLE_ROW } == 5 &&
                    Card.values().count { it.cardState == TOP_ROW } == 3
    }
}