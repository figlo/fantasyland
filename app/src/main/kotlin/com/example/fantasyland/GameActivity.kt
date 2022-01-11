package com.example.fantasyland

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
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

        val layout = binding.linearLayoutDealtCards

        NewCard.values().forEach { it.cardState = DECK  }

        var dealtCards = mutableListOf<NewCard>()
        for (i in 1..numberOfCardsInFantasyLand) {
            dealtCards.add(NewCard.dealCard())
        }

        for (card in dealtCards) {
            val cardView = ImageView(this)
            cardView.layoutParams = LinearLayout.LayoutParams(100, 140)

            val cardImage = resources.getIdentifier(card.file, "drawable", packageName)
            cardView.setImageResource(cardImage)

            layout.addView(cardView)
        }

        var sortSwitch = true
        binding.buttonSort.setOnClickListener {
            layout.removeAllViews()

            dealtCards = if (sortSwitch) {
                NewCard.sortByRankAndColor().toMutableList()
            } else {
                NewCard.sortByColorAndRank().toMutableList()
            }
            sortSwitch = !sortSwitch

            for (card in dealtCards) {
                val cardView = ImageView(this)
                cardView.layoutParams = LinearLayout.LayoutParams(100, 140)

                val cardImage = resources.getIdentifier(card.file, "drawable", packageName)
                cardView.setImageResource(cardImage)

                layout.addView(cardView)
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
