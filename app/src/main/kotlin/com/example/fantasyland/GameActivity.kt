package com.example.fantasyland

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import androidx.preference.PreferenceManager
import com.example.fantasyland.CardState.DEALT
import com.example.fantasyland.CardState.DECK
import com.example.fantasyland.databinding.ActivityGameBinding
import com.google.android.material.snackbar.Snackbar

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val numberOfCardsInFantasyLand = preferences.getString("number_of_cards_in_fantasy_land", "14")?.toInt()!!

        val displayWidth = Resources.getSystem().displayMetrics.widthPixels
        val cardWidth = displayWidth / 21
        val cardHeight = (cardWidth * 1.4).toInt()

        // board views
        val layoutBottomRow = binding.linearLayoutBottomRow
        val layoutMiddleRow = binding.linearLayoutMiddleRow
        val layoutTopRow = binding.linearLayoutTopRow

        val tiles = mutableListOf<Tile>()

        val imageViewMargin = 8
        val imageViewPadding = 1
        val imageViewBackgroundColor = ContextCompat.getColor(this, R.color.cardViewBackground)

        selectedTile = null

        val emptyCardImage = resources.getIdentifier("empty_card", "drawable", packageName)

        for (i in 1..13) {
            val layoutRow = when (i) {
                in 1..5  -> layoutBottomRow
                in 6..10 -> layoutMiddleRow
                else     -> layoutTopRow
            }

            ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(cardWidth, cardHeight)
                setImageResource(emptyCardImage)
                tag = emptyCardImage
                updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(imageViewMargin) }
                setPadding(imageViewPadding)
                setBackgroundColor(imageViewBackgroundColor)

                layoutRow.addView(this)
                tiles.add(Tile(i, this))
            }
        }

        // dealt cards views
        val layoutDealtCards = binding.linearLayoutDealtCards

        Card.values().forEach { it.cardState = DECK }

        for (i in 1..numberOfCardsInFantasyLand) {
            val card = dealCard()
            val cardImage = resources.getIdentifier(card.file, "drawable", packageName)

            ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(cardWidth, cardHeight)
                setImageResource(cardImage)
                tag = cardImage
                updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(imageViewMargin) }
                setPadding(imageViewPadding)
                setBackgroundColor(imageViewBackgroundColor)

                layoutDealtCards.addView(this)
                tiles.add(Tile(i + 13, this, card))
            }
        }

        for (tile in tiles) {
            tile.imageView.setOnClickListener {
                tile.onClickHandler()
                binding.buttonDone.visibility = if (isFullBoard) View.VISIBLE else View.INVISIBLE
            }
        }

        // home button
        binding.buttonHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // sort button
        sortSwitch = true

        binding.buttonSort.setOnClickListener {
            selectedTile?.deSelect()

            val dealtCards = tiles.mapNotNull { it.card }.filter { it.cardState == DEALT }.toMutableList()
            dealtCards.sort()

            for (i in 1..numberOfCardsInFantasyLand) {
                val setCardToView = i <= dealtCards.size

                val dealtCard: Card? = if (setCardToView) dealtCards[i - 1] else null
                tiles[i + 12].card = dealtCard

                val cardFile = if (setCardToView) dealtCards[i - 1].file else "empty_card"
                val cardImage = resources.getIdentifier(cardFile, "drawable", packageName)

                tiles[i + 12].imageView.apply {
                    setImageResource(cardImage)
                    tag = cardImage
                }
            }
        }

        // set all cards button
        binding.buttonSetAllCards.setOnClickListener {
            selectedTile?.deSelect()

            val emptyBoardTiles = tiles.take(13).filter { it.card == null }.toMutableList()

            if (emptyBoardTiles.size > 0) {
                for (i in 1..numberOfCardsInFantasyLand) {
                    val viewHasCard = tiles[i + 12].card != null

                    if (viewHasCard) {
                        selectedTile = tiles[i + 12]
                        emptyBoardTiles[0].makeMove()
                        emptyBoardTiles.removeAt(0)
                        if (emptyBoardTiles.size == 0) break
                    }
                }
            }

            binding.buttonDone.visibility = View.VISIBLE
        }

        // done button
        binding.buttonDone.setOnClickListener {
            selectedTile?.deSelect()

            for (tile in tiles) {
                tile.imageView.setOnClickListener(null)
            }

            binding.apply {
                buttonSort.visibility = View.GONE
                buttonSetAllCards.visibility = View.GONE
                buttonDone.visibility = View.GONE
                buttonNewGame.visibility = View.VISIBLE
            }

            val bottomRowCardsList = tiles.subList(0, 5).mapNotNull { it.card }.toMutableList()
            val middleRowCardsList = tiles.subList(5, 10).mapNotNull { it.card }.toMutableList()
            val topRowCardsList = tiles.subList(10, 13).mapNotNull { it.card }.toMutableList()

            val bottomRowCards = BottomRowCards(bottomRowCardsList)
            val middleRowCards = MiddleRowCards(middleRowCardsList)
            val topRowCards = TopRowCards(topRowCardsList)

            bottomRowCards.apply {
                if (isWheel)
                    cards.sortByRankAndColorAceLow()
                else
                    cards.sortByCountAndRank()
            }
            middleRowCards.apply {
                if (isWheel)
                    cards.sortByRankAndColorAceLow()
                else
                    cards.sortByCountAndRank()
            }
            topRowCardsList.sortByCountAndRank()

            for (i in 1..13) {
                val card = when (i) {
                    in 1..5  -> bottomRowCardsList[i - 1]
                    in 6..10 -> middleRowCardsList[i - 6]
                    else     -> topRowCardsList[i - 11]
                }

                tiles[i - 1].card = card
                val cardImage = resources.getIdentifier(card.file, "drawable", packageName)

                tiles[i - 1].imageView.apply {
                    setImageResource(cardImage)
                    tag = cardImage
                }
            }

            val resultOKColor = ContextCompat.getColor(this, R.color.resultOK)
            val resultXColor = ContextCompat.getColor(this, R.color.resultX)
            val newFantasyLandColor = ContextCompat.getColor(this, R.color.newFantasyLand)

            Result().apply {
                this.bottomRowCards = bottomRowCards
                this.middleRowCards = middleRowCards
                this.topRowCards = topRowCards

                if (isValidResult()) {
                    binding.apply {
                        bottomRowResult.text = bottomRowCards.value().toString()
                        middleRowResult.text = middleRowCards.value().toString()
                        topRowResult.text = topRowCards.value().toString()
                        finalResult.text = (bottomRowCards.value() + middleRowCards.value() + topRowCards.value()).toString()
                        finalResult.setTextColor(resultOKColor)
                    }
                } else {
                    binding.apply {
                        finalResult.text = resources.getString(R.string.result_x)
                        finalResult.setTextColor(resultXColor)
                    }
                }

                if (isRepeatedFantasy()) {
                    binding.apply {
                        newFantasyLand.text = resources.getString(R.string.new_fantasyland)
                        newFantasyLand.setTextColor(newFantasyLandColor)
                    }
                }
            }
        }


    // new game button
    binding.buttonNewGame.setOnClickListener {
        isFullBoard = false
        startActivity(Intent(this, GameActivity::class.java))
    }
}

override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.right_menu, menu)
    return true
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
        R.id.settings -> {
            this.startActivity(intent)
            true
        }
        R.id.about    -> {
            Snackbar.make(binding.root, "About", Snackbar.LENGTH_SHORT).show()
            true
        }
        else          -> super.onOptionsItemSelected(item)
    }
}
}