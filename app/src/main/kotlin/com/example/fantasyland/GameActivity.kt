package com.example.fantasyland

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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

        for (i in 1..13) {
            val layoutRow = when (i) {
                in 1..5  -> layoutBottomRow
                in 6..10  -> layoutMiddleRow
                else -> layoutTopRow
            }

            ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(cardWidth, cardHeight)
                setImageResource(emptyCardImage)
                updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(8) }
                setPadding(1)
                setBackgroundColor(Color.parseColor("#000000"))
                layoutRow.addView(this)
            }
        }

        val layoutDealtCards = binding.linearLayoutDealtCards

//        Snackbar.make(layout, cardWidth.toString(), Snackbar.LENGTH_LONG).show()

        NewCard.values().forEach { it.cardState = DECK }

        var dealtCards = mutableListOf<NewCard>()
        for (i in 1..numberOfCardsInFantasyLand) {
            dealtCards.add(NewCard.dealCard())
        }

        for (card in dealtCards) {
            val dealtCardView = ImageView(this)
            val id = View.generateViewId()
            dealtCardView.id = id

            dealtCardView.layoutParams = LinearLayout.LayoutParams(cardWidth, cardHeight)

            val cardImage = resources.getIdentifier(card.file, "drawable", packageName)
            dealtCardView.setImageResource(cardImage)
            dealtCardView.updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(8) }
            dealtCardView.setPadding(1)
            dealtCardView.setBackgroundColor(Color.parseColor("#000000"))
//            Log.d("asdfjkl", dealtCardView.id.toString())

            layoutDealtCards.addView(dealtCardView)
        }

        var sortSwitch = true
        binding.buttonSort.setOnClickListener {
            layoutDealtCards.removeAllViews()

            dealtCards = if (sortSwitch) {
                NewCard.sortByColorAndRank().toMutableList()
            } else {
                NewCard.sortByRankAndColor().toMutableList()
            }
            sortSwitch = !sortSwitch

            for (card in dealtCards) {
                val dealtCardView = ImageView(this)
                dealtCardView.layoutParams = LinearLayout.LayoutParams(cardWidth, cardHeight)

                val cardImage = resources.getIdentifier(card.file, "drawable", packageName)
                dealtCardView.setImageResource(cardImage)
                dealtCardView.updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(8) }
                dealtCardView.setPadding(1)
                dealtCardView.setBackgroundColor(Color.parseColor("#000000"))

                layoutDealtCards.addView(dealtCardView)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.right_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {               // TODO get rid of redundant code (MainActivity)
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
