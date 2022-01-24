package com.example.fantasyland

import android.graphics.Color
import android.widget.ImageView
import androidx.core.view.setPadding

class Tile(val id: Int, val imageView: ImageView, var card: NewCard? = null) {
    val isBoard = id <= 13

    fun onClickHandler() {
        if (selectedTile == null) {
            if (card != null) select()
        } else {
            if (card != null) {
                if (card == selectedTile!!.card) {
                    deSelect()
                    selectedTile = null
                } else {
                    swap()
                }
            } else {
                move()
            }
        }
    }

    fun move() {
        card = selectedTile?.card
        selectedTile?.card = null
        card?.cardState = if (isBoard) CardState.BOARD else CardState.DEALT

        val selectedTileTag = selectedTile?.imageView?.tag.toString().toInt()
        selectedTile?.imageView?.setImageResource(imageView.tag.toString().toInt())
        imageView.setImageResource(selectedTileTag)

        selectedTile?.imageView?.tag = imageView.tag
        imageView.tag = selectedTileTag

        selectedTile?.deSelect()
        selectedTile = null
    }

    fun swap() {
        val selectedCard = selectedTile?.card
        selectedTile?.card = card
        selectedTile?.card?.cardState = if (selectedTile!!.isBoard) CardState.BOARD else CardState.DEALT
        card = selectedCard
        card?.cardState = if (isBoard) CardState.BOARD else CardState.DEALT

        val selectedTileTag = selectedTile?.imageView?.tag.toString().toInt()
        selectedTile?.imageView?.setImageResource(imageView.tag.toString().toInt())
        imageView.setImageResource(selectedTileTag)

        selectedTile?.imageView?.tag = imageView.tag
        imageView.tag = selectedTileTag

        selectedTile?.deSelect()
        selectedTile = null
    }

    fun select() {
        imageView.setBackgroundColor(Color.parseColor("#ff0000"))
        imageView.setPadding(4)

        selectedTile = this
    }

    fun deSelect() {
        imageView.setBackgroundColor(Color.parseColor("#000000"))
        imageView.setPadding(1)
    }

    companion object {
        var selectedTile: Tile? = null
    }
}