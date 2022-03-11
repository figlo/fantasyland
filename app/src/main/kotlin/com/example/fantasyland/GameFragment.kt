package com.example.fantasyland

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.example.fantasyland.databinding.FragmentGameBinding
import timber.log.Timber
import kotlin.math.max

class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("onCreateView called")

        binding = FragmentGameBinding.inflate(inflater)

        Timber.i("ViewModelProvider.get called")
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.i("onViewCreated called")

        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val numberOfCardsInFantasyLand: Int = preferences.getString("number_of_cards_in_fantasy_land", "14")?.toInt()!!

        val displayWidth: Int = Resources.getSystem().displayMetrics.widthPixels
        val displayHeight: Int = Resources.getSystem().displayMetrics.heightPixels
        val cardWidth: Int = max(displayWidth, displayHeight) / 21
        val cardHeight: Int = (cardWidth * 1.4).toInt()

        var imageViews = listOf<ImageView>()

        val imageViewMargin = 8
        val imageViewPadding = 1
        val imageViewBackgroundColor: Int = ContextCompat.getColor(requireContext(), R.color.cardViewBackground)

        fun cardImageResource(card: Card?): Int = resources.getIdentifier(fileName(card), "drawable", requireContext().packageName)

        // new game - reset values
        var selectedView: ImageView? = null
        Card.values().forEach { it.cardState = CardState.DECK }

        // image views
        val layoutBottomRow: LinearLayout = binding.linearLayoutBottomRow
        val layoutMiddleRow: LinearLayout = binding.linearLayoutMiddleRow
        val layoutTopRow: LinearLayout = binding.linearLayoutTopRow
        val layoutDealtCards: LinearLayout = binding.linearLayoutDealtCards

        for (i in 1..(13 + numberOfCardsInFantasyLand)) {
            val layoutRow: LinearLayout = when (i) {
                in 1..5   -> layoutBottomRow
                in 6..10  -> layoutMiddleRow
                in 11..13 -> layoutTopRow
                else      -> layoutDealtCards
            }

            val card = if (i in 1..13) null else dealCard()

            ImageView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(cardWidth, cardHeight)
                setImageResource(cardImageResource(card))
                tag = card
                updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(imageViewMargin) }
                setPadding(imageViewPadding)
                setBackgroundColor(imageViewBackgroundColor)

                layoutRow.addView(this)
                imageViews += this
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
            val selectedCard: Card? = selectedView!!.tag as Card?
            val card: Card = imageView.tag as Card
            selectedView?.setImageResource(cardImageResource(selectedCard))
            imageView.setImageResource(cardImageResource(card))

            deSelect(selectedView!!)

            val isMovingPhaseDone: Boolean = imageViews.take(13).all { it.tag != null }
            binding.buttonDone.visibility = if (isMovingPhaseDone) View.VISIBLE else View.INVISIBLE
        }

        fun onClickHandler(imageView: ImageView) {
            val isSomeTileSelected: Boolean = selectedView != null

            if (isSomeTileSelected) {
                val isThisTileSelected: Boolean = selectedView == imageView
                if (isThisTileSelected) {
                    deSelect(imageView)
                } else {
                    makeMove(imageView)
                }
            } else {
                val isCardOnThisTile: Boolean = imageView.tag != null
                if (isCardOnThisTile) select(imageView)
            }
        }

        // set onClickListener on imageViews
        imageViews.forEach { imageView -> imageView.setOnClickListener { onClickHandler(imageView) } }

        // sort button
        sortSwitch = true

        binding.buttonSort.setOnClickListener {
            selectedView?.let { deSelect(selectedView!!) }

            val dealtCards: List<Card> = imageViews
                .drop(13)
                .mapNotNull { it.tag as Card? }
                .sort()

            for (i in 1..numberOfCardsInFantasyLand) {
                val setCardToView: Boolean = i <= dealtCards.size
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

            val emptyBoardViews: MutableList<ImageView> = imageViews.take(13).filter { it.tag == null }.toMutableList()

            if (emptyBoardViews.size > 0) {
                for (i in 1..numberOfCardsInFantasyLand) {
                    val viewHasCard: Boolean = imageViews[i + 12].tag != null

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

            var bottomRowCards: List<Card> = imageViews.subList(0, 5).map { it.tag as Card }
            bottomRowCards =
                if (bottomRowCards.isAnyWheel)
                    bottomRowCards.sortByRankAndColorAceLow()
                else
                    bottomRowCards.sortByCountAndRank()

            var middleRowCards: List<Card> = imageViews.subList(5, 10).map { it.tag as Card }
            middleRowCards =
                if (middleRowCards.isAnyWheel)
                    middleRowCards.sortByRankAndColorAceLow()
                else
                    middleRowCards.sortByCountAndRank()

            var topRowCards: List<Card> = imageViews.subList(10, 13).map { it.tag as Card }
            topRowCards = topRowCards.sortByCountAndRank()

            val bottomRow = BottomRow(bottomRowCards)
            val middleRow = MiddleRow(middleRowCards)
            val topRow = TopRow(topRowCards)

            for (i in 1..13) {
                val card: Card = when (i) {
                    in 1..5  -> bottomRowCards[i - 1]
                    in 6..10 -> middleRowCards[i - 6]
                    else     -> topRowCards[i - 11]
                }

                imageViews[i - 1].apply {
                    setImageResource(cardImageResource(card))
                    tag = card
                }
            }

            val resultOKColor: Int = ContextCompat.getColor(requireContext(), R.color.resultOK)
            val resultXColor: Int = ContextCompat.getColor(requireContext(), R.color.resultX)
            val newFantasyLandColor: Int = ContextCompat.getColor(requireContext(), R.color.newFantasyLand)

            if (isValidResult(bottomRow, middleRow, topRow)) {
                val result: Int = bottomRow.value() + middleRow.value() + topRow.value()
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
            val navController = it.findNavController()
            val id: Int? = navController.currentDestination?.id
            navController.popBackStack(id!!,true)
            navController.navigate(id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("onDestroyView called")

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("onAttach called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop called")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.i("onDetach called")
    }
}

fun fileName(card: Card?): String =
    if (card == null)
        "empty_card"
    else
        "card_" + card.name.takeLast(2).lowercase()

fun dealCard(): Card = Card.values()
    .filter { it.cardState == CardState.DECK }
    .random()
    .apply { cardState = CardState.DEALT }

fun isValidResult(bottomRow: BottomRow, middleRow: MiddleRow, topRow: TopRow): Boolean =
    when {
        middleRow isHigherThan bottomRow -> false
        topRow isHigherThan middleRow    -> false
        else                             -> true
    }

fun isRepeatedFantasy(bottomRow: BottomRow, middleRow: MiddleRow, topRow: TopRow): Boolean =
    isValidResult(bottomRow, middleRow, topRow) &&
            (bottomRow.pokerCombination >= PokerCombination.QUADS ||
                    topRow.pokerCombination == PokerCombination.TRIPS)