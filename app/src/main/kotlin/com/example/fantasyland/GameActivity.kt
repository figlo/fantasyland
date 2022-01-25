package com.example.fantasyland

import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        // board views
        val layoutBottomRow = binding.linearLayoutBottomRow
        val layoutMiddleRow = binding.linearLayoutMiddleRow
        val layoutTopRow = binding.linearLayoutTopRow

        val tiles = mutableListOf<Tile>()

        val imageViewMargin = 8
        val imageViewPadding = 1
        val imageViewBackgroundColor = ContextCompat.getColor(this, R.color.cardViewBackground)

        Tile.selectedTile = null

        for (i in 1..13) {
            val layoutRow = when (i) {
                in 1..5  -> layoutBottomRow
                in 6..10 -> layoutMiddleRow
                else     -> layoutTopRow
            }

            val emptyCardImage = resources.getIdentifier("empty_card", "drawable", packageName)

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

        var dealtCards = mutableListOf<NewCard>()

        NewCard.values().forEach { it.cardState = DECK }

        for (i in 1..numberOfCardsInFantasyLand) {
            val card = NewCard.dealCard()
            dealtCards.add(card)

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

//        Snackbar.make(this, i.toString(), Snackbar.LENGTH_LONG).show()

        for (tile in tiles) {
            tile.imageView.setOnClickListener {
                tile.onClickHandler()
            }
        }

        // sort button
        var sortSwitch = true

        binding.buttonSort.setOnClickListener {
            Tile.selectedTile?.deSelect()

            dealtCards = when (sortSwitch) {
                true  -> NewCard.sortByColorAndRank().toMutableList()
                false -> NewCard.sortByRankAndColor().toMutableList()
            }
            sortSwitch = !sortSwitch

            for (i in 1..numberOfCardsInFantasyLand) {
                val isDealt = i <= dealtCards.size

                val dealtCard: NewCard? = if (isDealt) dealtCards[i - 1] else null
                tiles[i + 12].card = dealtCard

                val cardFile = if (isDealt) dealtCards[i - 1].file else "empty_card"
                val cardImage = resources.getIdentifier(cardFile, "drawable", packageName)

                tiles[i + 12].imageView.apply {
                    setImageResource(cardImage)
                    tag = cardImage
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