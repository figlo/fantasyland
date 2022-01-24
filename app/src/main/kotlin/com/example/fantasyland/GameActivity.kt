package com.example.fantasyland

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import androidx.preference.PreferenceManager
import com.example.fantasyland.CardState.DECK
import com.example.fantasyland.databinding.ActivityGameBinding
import com.google.android.material.snackbar.Snackbar

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.includedLayout.toolbar
        setSupportActionBar(toolbar)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val numberOfCardsInFantasyLand = preferences.getString("number_of_cards_in_fantasy_land", "14")?.toInt()!!

        val displayWidth = Resources.getSystem().displayMetrics.widthPixels
        val cardWidth = displayWidth / 21
        val cardHeight = (cardWidth * 1.4).toInt()
        val emptyCardImage = resources.getIdentifier("empty_card", "drawable", packageName)

        val layoutBottomRow = binding.linearLayoutBottomRow
        val layoutMiddleRow = binding.linearLayoutMiddleRow
        val layoutTopRow = binding.linearLayoutTopRow

        val boardCardViews = mutableListOf<ImageView>()

        val tiles = mutableListOf<Tile>()

        Tile.selectedTile = null

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
                updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(8) }
                setPadding(1)
                setBackgroundColor(Color.parseColor("#000000"))

                boardCardViews.add(this)
                layoutRow.addView(this)

                tiles.add(Tile(i, this))
            }

        }

        val layoutDealtCards = binding.linearLayoutDealtCards

        NewCard.values().forEach { it.cardState = DECK }

        var dealtCards = mutableListOf<NewCard>()

        val dealtCardViews = mutableListOf<ImageView>()
        for (i in 1..numberOfCardsInFantasyLand) {
            val card = NewCard.dealCard()
            dealtCards.add(card)
            val cardImage = resources.getIdentifier(card.file, "drawable", packageName)

            ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(cardWidth, cardHeight)
                setImageResource(cardImage)
                tag = cardImage
                updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(8) }
                setPadding(1)
                setBackgroundColor(Color.parseColor("#000000"))

                dealtCardViews.add(this)
                layoutDealtCards.addView(this)

                tiles.add(Tile(i + 13, this, card))
            }
        }

//        Snackbar.make(this, i.toString(), Snackbar.LENGTH_LONG).show()

        for (tile in tiles) {
            tile.imageView.setOnClickListener {
                tile.onClickHandler()
            }
        }

        var sortSwitch = true

        binding.buttonSort.setOnClickListener {
            Tile.selectedTile?.deSelect()
            Tile.selectedTile = null

            dealtCards = when (sortSwitch) {
                true  -> NewCard.sortByColorAndRank().toMutableList()
                false -> NewCard.sortByRankAndColor().toMutableList()
            }
            sortSwitch = !sortSwitch

            for (i in 1..numberOfCardsInFantasyLand) {
                val cardImage: Int
                var tempCard: NewCard? = null
                if (i <= dealtCards.size) {
                    tempCard = dealtCards[i - 1]
                    cardImage = resources.getIdentifier(tempCard.file, "drawable", packageName)
                } else {
                    cardImage = resources.getIdentifier("empty_card", "drawable", packageName)
                }
                dealtCardViews[i - 1].setImageResource(cardImage)
                dealtCardViews[i - 1].tag = cardImage
                if (tempCard == null) {
                    tiles[i + 12].card = null
                } else {
                    tiles[i + 12].card = tempCard
                }
            }
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
