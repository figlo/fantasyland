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

        val imageViews = mutableListOf<ImageView>()

        val imageViewMargin = 8
        val imageViewPadding = 1
        val imageViewBackgroundColor = ContextCompat.getColor(this, R.color.cardViewBackground)

        fun cardImageResource(card: Card?) = resources.getIdentifier(fileName(card), "drawable", packageName)

        // new game - reset values
        var selectedView: ImageView? = null
        Card.values().forEach { it.cardState = DECK }

        // image views
        val layoutBottomRow = binding.linearLayoutBottomRow
        val layoutMiddleRow = binding.linearLayoutMiddleRow
        val layoutTopRow = binding.linearLayoutTopRow
        val layoutDealtCards = binding.linearLayoutDealtCards

        for (i in 1..(13 + numberOfCardsInFantasyLand)) {
            val layoutRow = when (i) {
                in 1..5   -> layoutBottomRow
                in 6..10  -> layoutMiddleRow
                in 11..13 -> layoutTopRow
                else      -> layoutDealtCards
            }

            val card = if (i in 1..13) null else dealCard()

            ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(cardWidth, cardHeight)
                setImageResource(cardImageResource(card))
                tag = card
                updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(imageViewMargin) }
                setPadding(imageViewPadding)
                setBackgroundColor(imageViewBackgroundColor)

                layoutRow.addView(this)
                imageViews.add(this)
            }
        }

        // imageView functions
        fun select(imageView: ImageView) {
            with(imageView) {
                setBackgroundColor(ContextCompat.getColor(context, R.color.cardViewSelected))
                setPadding(4)
            }

            selectedView = imageView
        }

        fun deSelect(imageView: ImageView) {
            with(imageView) {
                setBackgroundColor(ContextCompat.getColor(context, R.color.cardViewBackground))
                setPadding(1)
            }

            selectedView = null
        }

        fun makeMove(imageView: ImageView) {
            // swap tags (cards)
            selectedView?.tag = imageView.tag.also { imageView.tag = selectedView?.tag }

            // set new imageResources from already swapped tags (cards)
            val selectedCard = selectedView!!.tag as Card?
            val card = imageView.tag as Card?
            selectedView?.setImageResource(cardImageResource(selectedCard))
            imageView.setImageResource(cardImageResource(card))

            deSelect(selectedView!!)

            val isMovingPhaseDone = imageViews.take(13).all { it.tag != null }
            binding.buttonDone.visibility = if (isMovingPhaseDone) View.VISIBLE else View.INVISIBLE
        }

        fun onClickHandler(imageView: ImageView) {
            val isSomeTileSelected = selectedView != null

            if (isSomeTileSelected) {
                val isThisTileSelected = selectedView == imageView
                if (isThisTileSelected) {
                    deSelect(imageView)
                } else {
                    makeMove(imageView)
                }
            } else {
                val isCardOnThisTile = imageView.tag != null
                if (isCardOnThisTile) select(imageView)
            }
        }

        // set onClickListener on imageViews
        imageViews.forEach { imageView -> imageView.setOnClickListener { onClickHandler(imageView) } }

        // home button
        binding.buttonHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // sort button
        sortSwitch = true

        binding.buttonSort.setOnClickListener {
            selectedView?.let { deSelect(selectedView!!) }

            val dealtCards = imageViews.drop(13).mapNotNull { it.tag as Card? }.toMutableList()
            dealtCards.sort()

            for (i in 1..numberOfCardsInFantasyLand) {
                val setCardToView = i <= dealtCards.size
                val dealtCard: Card? = if (setCardToView) dealtCards[i - 1] else null

                imageViews[i + 12].apply {
                    setImageResource(cardImageResource(dealtCard))
                    tag = dealtCard
                }
            }
        }

        // set all cards button
        binding.buttonSetAllCards.setOnClickListener {
            selectedView?.let { deSelect(selectedView!!) }

            val emptyBoardViews = imageViews.take(13).filter { it.tag == null }.toMutableList()

            if (emptyBoardViews.size > 0) {
                for (i in 1..numberOfCardsInFantasyLand) {
                    val viewHasCard = imageViews[i + 12].tag != null

                    if (viewHasCard) {
                        selectedView = imageViews[i + 12]
                        makeMove(emptyBoardViews[0])
                        emptyBoardViews.removeAt(0)
                        if (emptyBoardViews.size == 0) break
                    }
                }
            }
        }

        // done button
        binding.buttonDone.setOnClickListener {
            selectedView?.let { deSelect(selectedView!!) }

            imageViews.forEach { it.setOnClickListener(null) }

            binding.apply {
                buttonSort.visibility = View.GONE
                buttonSetAllCards.visibility = View.GONE
                buttonDone.visibility = View.GONE
                buttonNewGame.visibility = View.VISIBLE
            }

            val bottomRowCards = imageViews.subList(0, 5).map { it.tag as Card }.toMutableList()
            val middleRowCards = imageViews.subList(5, 10).map { it.tag as Card }.toMutableList()
            val topRowCards = imageViews.subList(10, 13).map { it.tag as Card }.toMutableList()

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

                imageViews[i - 1].apply {
                    setImageResource(cardImageResource(card))
                    tag = card
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

fun fileName(card: Card?) = if (card == null) "empty_card" else "card_" + card.name.takeLast(2).lowercase()

fun dealCard() = Card.values()
    .filter { it.cardState == DECK }
    .random()
    .apply { cardState = DEALT }

fun isValidResult(bottomRow: BottomRow, middleRow: MiddleRow, topRow: TopRow) =
    when {
        middleRow isHigherThan bottomRow -> false
        topRow isHigherThan middleRow    -> false
        else                             -> true
    }

fun isRepeatedFantasy(bottomRow: BottomRow, middleRow: MiddleRow, topRow: TopRow) =
    isValidResult(bottomRow, middleRow, topRow) &&
            (bottomRow.pokerCombination >= PokerCombination.QUADS ||
                    topRow.pokerCombination == PokerCombination.TRIPS)