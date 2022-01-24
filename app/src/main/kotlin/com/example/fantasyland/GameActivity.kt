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
import com.example.fantasyland.CardState.*
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
        for (i in 1..13) {
            val layoutRow = when (i) {
                in 1..5  -> layoutBottomRow
                in 6..10 -> layoutMiddleRow
                else     -> layoutTopRow
            }

            ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(cardWidth, cardHeight)
                setImageResource(emptyCardImage)
                updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(8) }
                setPadding(1)
                setBackgroundColor(Color.parseColor("#000000"))

                boardCardViews.add(this)
                layoutRow.addView(this)
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
                updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(8) }
                setPadding(1)
                setBackgroundColor(Color.parseColor("#000000"))

                dealtCardViews.add(this)
                layoutDealtCards.addView(this)
            }
        }

        fun allCardsDeselect() {
            for (view in boardCardViews) {
                view.setBackgroundColor(Color.parseColor("#000000"))
                view.setPadding(1)
            }
            for (view in dealtCardViews) {
                view.setBackgroundColor(Color.parseColor("#000000"))
                view.setPadding(1)
            }
            for (card in dealtCards) {
                card.cardState = DEALT
            }
        }

        fun select(view: ImageView) {
            allCardsDeselect()

            view.setBackgroundColor(Color.parseColor("#ff0000"))
            view.setPadding(4)

            val index = dealtCardViews.indexOf(view)
            dealtCards[index].cardState = CLICKED
//                    Snackbar.make(this, i.toString(), Snackbar.LENGTH_LONG).show()
        }

        for (view in boardCardViews) {
            view.setOnClickListener {
//                select(view)
            }
        }

        for (view in dealtCardViews) {
            view.setOnClickListener {
                select(view)
            }
        }

        var sortSwitch = true

        binding.buttonSort.setOnClickListener {
            allCardsDeselect()
            dealtCards = when (sortSwitch) {
                true  -> NewCard.sortByColorAndRank().toMutableList()
                false -> NewCard.sortByRankAndColor().toMutableList()
            }
            sortSwitch = !sortSwitch

            for ((i, view) in dealtCardViews.withIndex()) {
                val card = dealtCards[i]
                val cardImage = resources.getIdentifier(card.file, "drawable", packageName)
                view.setImageResource(cardImage)
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
