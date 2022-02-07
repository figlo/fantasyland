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

        Tile.selectedTile = null

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
                tag = mutableMapOf<String, Any?>("card" to null, "imageResource" to emptyCardImage)
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
            val cardImage = resources.getIdentifier(fileName(card), "drawable", packageName)

            ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(cardWidth, cardHeight)
                setImageResource(cardImage)
                tag = mutableMapOf<String, Any?>("card" to card, "imageResource" to cardImage)
                updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(imageViewMargin) }
                setPadding(imageViewPadding)
                setBackgroundColor(imageViewBackgroundColor)

                layoutDealtCards.addView(this)
                tiles.add(Tile(i + 13, this))
            }
        }

        for (tile in tiles) {
            tile.imageView.setOnClickListener {
                tile.onClickHandler()
                binding.buttonDone.visibility = if (Tile.isMovingPhaseDone) View.VISIBLE else View.INVISIBLE
            }
        }

        // home button
        binding.buttonHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // sort button
        sortSwitch = true

        binding.buttonSort.setOnClickListener {
            Tile.selectedTile?.deSelect()

            val dealtCards = tiles.mapNotNull { it.card }.filter { it.cardState == DEALT }.toMutableList()
            dealtCards.sort()

            for (i in 1..numberOfCardsInFantasyLand) {
                val setCardToView = i <= dealtCards.size
                val dealtCard: Card? = if (setCardToView) dealtCards[i - 1] else null
                val cardFile = if (setCardToView) fileName(dealtCards[i - 1]) else "empty_card"
                val cardImage = resources.getIdentifier(cardFile, "drawable", packageName)

                tiles[i + 12].imageView.apply {
                    setImageResource(cardImage)
                    tag = mutableMapOf<String, Any?>("card" to dealtCard, "imageResource" to cardImage)
                }
            }
        }

        // set all cards button
        binding.buttonSetAllCards.setOnClickListener {
            Tile.selectedTile?.deSelect()

            val emptyBoardTiles = tiles.take(13).filter { it.card == null }.toMutableList()

            if (emptyBoardTiles.size > 0) {
                for (i in 1..numberOfCardsInFantasyLand) {
                    val viewHasCard = tiles[i + 12].card != null

                    if (viewHasCard) {
                        Tile.selectedTile = tiles[i + 12]
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
            Tile.selectedTile?.deSelect()

            for (tile in tiles) {
                tile.imageView.setOnClickListener(null)
            }

            binding.apply {
                buttonSort.visibility = View.GONE
                buttonSetAllCards.visibility = View.GONE
                buttonDone.visibility = View.GONE
                buttonNewGame.visibility = View.VISIBLE
            }

            val bottomRowCards = tiles.subList(0, 5).mapNotNull { it.card }.toMutableList()
            val middleRowCards = tiles.subList(5, 10).mapNotNull { it.card }.toMutableList()
            val topRowCards = tiles.subList(10, 13).mapNotNull { it.card }.toMutableList()

            val bottomRow = BottomRow(bottomRowCards)
            val middleRow = MiddleRow(middleRowCards)
            val topRow = TopRow(topRowCards)

            bottomRow.cards.apply {
                if (isAnyWheel)
                    sortByRankAndColorAceLow()
                else
                    sortByCountAndRank()
            }
            middleRow.cards.apply {
                if (isAnyWheel)
                    sortByRankAndColorAceLow()
                else
                    sortByCountAndRank()
            }
            topRow.cards.sortByCountAndRank()

            for (i in 1..13) {
                val card = when (i) {
                    in 1..5  -> bottomRowCards[i - 1]
                    in 6..10 -> middleRowCards[i - 6]
                    else     -> topRowCards[i - 11]
                }

                val cardImage = resources.getIdentifier(fileName(card), "drawable", packageName)

                tiles[i - 1].imageView.apply {
                    setImageResource(cardImage)
                    tag = mutableMapOf<String, Any?>("card" to card, "imageResource" to cardImage)
                }
            }

            val resultOKColor = ContextCompat.getColor(this, R.color.resultOK)
            val resultXColor = ContextCompat.getColor(this, R.color.resultX)
            val newFantasyLandColor = ContextCompat.getColor(this, R.color.newFantasyLand)

            if (isValidResult(bottomRow, middleRow, topRow)) {
                val result = bottomRow.value() + middleRow.value() + topRow.value()
                binding.apply {
                    bottomRowResult.text = bottomRow.value().toString()
                    middleRowResult.text = middleRow.value().toString()
                    topRowResult.text = topRow.value().toString()
                    finalResult.text = result.toString()
                    finalResult.setTextColor(resultOKColor)
                }
            } else {
                binding.apply {
                    finalResult.text = resources.getString(R.string.result_x)
                    finalResult.setTextColor(resultXColor)
                }
            }

            if (isRepeatedFantasy(bottomRow, middleRow, topRow)) {
                binding.apply {
                    newFantasyLand.text = resources.getString(R.string.new_fantasyland)
                    newFantasyLand.setTextColor(newFantasyLandColor)
                }
            }
        }

        // new game button
        binding.buttonNewGame.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
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

fun fileName(card: Card) = "card_" + card.name.takeLast(2).lowercase()

fun dealCard(): Card {
    val dealtCard =
        Card.values()
            .filter { it.cardState == DECK }
            .random()
    dealtCard.cardState = DEALT
    return dealtCard
}

fun isValidResult(bottomRow: BottomRow, middleRow: MiddleRow, topRow: TopRow): Boolean {
    return when {
        middleRow isHigherThan bottomRow -> false
        topRow isHigherThan middleRow    -> false
        else                             -> true
    }
}

fun isRepeatedFantasy(bottomRow: BottomRow, middleRow: MiddleRow, topRow: TopRow) = isValidResult(bottomRow, middleRow, topRow) &&
        (bottomRow.pokerCombination >= PokerCombination.QUADS ||
                topRow.pokerCombination == PokerCombination.TRIPS)