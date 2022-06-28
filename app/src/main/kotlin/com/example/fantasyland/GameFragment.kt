package com.example.fantasyland

import android.content.Intent
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
import androidx.fragment.app.viewModels
import androidx.window.layout.WindowMetricsCalculator
import com.example.fantasyland.GameState.*
import com.example.fantasyland.databinding.FragmentGameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var cardViews: List<ImageView>
    private var selectedCardView: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpCardViews()
        setUpListeners()
        setUpObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    * Set up card views
    */

    private fun setUpCardViews() {
        setUpCardViewsList()
        setUpCardViewsParams()
        setUpVisibilityOfUnusedCardViews()
    }

    private fun setUpCardViewsList() {
        binding.apply {
            cardViews = listOf(
                cardView0,
                cardView1,
                cardView2,
                cardView3,
                cardView4,
                cardView5,
                cardView6,
                cardView7,
                cardView8,
                cardView9,
                cardView10,
                cardView11,
                cardView12,
                cardView13,
                cardView14,
                cardView15,
                cardView16,
                cardView17,
                cardView18,
                cardView19,
                cardView20,
                cardView21,
                cardView22,
                cardView23,
                cardView24,
                cardView25,
                cardView26,
                cardView27,
                cardView28,
                cardView29,
            )
        }
    }

    private fun setUpCardViewsParams() {
        // calculating card view width and height
        val bounds = WindowMetricsCalculator
            .getOrCreate()
            .computeCurrentWindowMetrics(requireActivity())
            .bounds
        val displayWidth = bounds.width()
        val cardViewWidth: Int = displayWidth / 22                  // empirically found out
        val cardViewHeight: Int = (cardViewWidth * 1.4).toInt()     // official poker card ratio

        // margin and padding must be set again programmatically (allready set in xml)
        val cardViewMargin = 8
        val cardViewPadding = 1

        for (cardView in cardViews) {
            with(cardView) {
                layoutParams = LinearLayout.LayoutParams(cardViewWidth, cardViewHeight)
                updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(cardViewMargin) }
                setPadding(cardViewPadding)
            }
        }
    }

    private fun setUpVisibilityOfUnusedCardViews() {
        val numberOfCardsInFantasyLand = viewModel.numberOfCardsInFantasyLand
        val dealtCardViews = cardViews.drop(13)
        dealtCardViews.drop(numberOfCardsInFantasyLand).forEach { it.visibility = View.GONE }
    }

    /*
    * Set up listeners
    */

    private fun setUpListeners() {
        setUpCardViewsListeners()
        setUpSortButtonListener()
        setUpSetAllCardsButtonListener()
        setUpDoneButtonListener()
        setUpNewGameButtonListener()
        setUpShareButtonListener()
    }

    private fun setUpCardViewsListeners() {
        cardViews.forEach { cardView -> cardView.setOnClickListener { onClickHandler(cardView) } }
    }

    private fun setUpSortButtonListener() {
        binding.buttonSort.setOnClickListener {
            selectedCardView?.let { deSelect(selectedCardView!!) }
            viewModel.sortCards()
        }
    }

    private fun setUpSetAllCardsButtonListener() {
        binding.buttonSetAllCards.setOnClickListener {
            selectedCardView?.let { deSelect(selectedCardView!!) }
            viewModel.setAllCards()
        }
    }

    private fun setUpDoneButtonListener() {
        binding.buttonDone.setOnClickListener {
            selectedCardView?.let { deSelect(selectedCardView!!) }
            viewModel.evaluateGame()
        }
    }

    private fun setUpNewGameButtonListener() {
        binding.buttonNewGame.setOnClickListener {
            setUpCardViewsListeners()
            viewModel.newGame()
        }
    }

    private fun setUpShareButtonListener() {
        binding.buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
                .putExtra(Intent.EXTRA_TEXT, binding.finalResult.text)
            startActivity(shareIntent)
        }
    }

    /*
    * Set up observers
    */

    private fun setUpObservers() {
        setUpCardsObservers()
        setUpGameStateObserver()
    }

    private fun setUpCardsObservers() {
        viewModel.cards.observe(viewLifecycleOwner) { newCards ->
            fun cardFileName(card: Card?): String =
                if (card == null)
                    "empty_card"
                else
                    "card_" + card.name.takeLast(2).lowercase()

            for ((index, cardView) in cardViews.withIndex()) {
                val imageResource = resources.getIdentifier(
                    cardFileName(newCards[index]),
                    "drawable",
                    requireContext().packageName,
                )
                cardView.setImageResource(imageResource)
            }
        }
    }

    private fun setUpGameStateObserver() {
        viewModel.gameState.observe(viewLifecycleOwner) { newGameState ->
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
            when (newGameState) {
                STARTED       -> gameStateStart()
                CARDS_ARE_SET -> gameStateCardsAreSet()
                DONE          -> gameStateDone()
            }
        }
    }

    private fun gameStateStart() {
        binding.apply {
            buttonSort.visibility = View.VISIBLE
            buttonSetAllCards.visibility = View.VISIBLE
            buttonDone.visibility = View.GONE
            buttonNewGame.visibility = View.GONE
            buttonShare.visibility = View.GONE
            bottomRowResult.visibility = View.GONE
            middleRowResult.visibility = View.GONE
            topRowResult.visibility = View.GONE
            finalResult.visibility = View.GONE
            newFantasyLand.visibility = View.GONE
        }
    }

    private fun gameStateCardsAreSet() {
        binding.apply {
            buttonSort.visibility = View.GONE
            buttonSetAllCards.visibility = View.GONE
            buttonDone.visibility = View.VISIBLE
            buttonNewGame.visibility = View.GONE
            buttonShare.visibility = View.GONE
            bottomRowResult.visibility = View.GONE
            middleRowResult.visibility = View.GONE
            topRowResult.visibility = View.GONE
            finalResult.visibility = View.GONE
            newFantasyLand.visibility = View.GONE
        }
    }

    private fun gameStateDone() {
        // displaying result of the game
        binding.apply {
            buttonSort.visibility = View.GONE
            buttonSetAllCards.visibility = View.GONE
            buttonDone.visibility = View.GONE
            buttonNewGame.visibility = View.VISIBLE
            buttonShare.visibility = View.VISIBLE
            finalResult.visibility = View.VISIBLE

            if (viewModel.isValidResult) {
                bottomRowResult.visibility = View.VISIBLE
                middleRowResult.visibility = View.VISIBLE
                topRowResult.visibility = View.VISIBLE

                bottomRowResult.text = viewModel.bottomRowResult.toString()
                middleRowResult.text = viewModel.middleRowResult.toString()
                topRowResult.text = viewModel.topRowResult.toString()
                finalResult.text = viewModel.finalResult.toString()

                val resultOKColor: Int = ContextCompat.getColor(requireContext(), R.color.resultOK)
                finalResult.setTextColor(resultOKColor)
            } else {
                bottomRowResult.visibility = View.GONE
                middleRowResult.visibility = View.GONE
                topRowResult.visibility = View.GONE
                finalResult.text = resources.getString(R.string.result_x)

                val resultXColor: Int = ContextCompat.getColor(requireContext(), R.color.resultX)
                finalResult.setTextColor(resultXColor)
            }

            if (viewModel.isRepeatedFantasy) {
                newFantasyLand.visibility = View.VISIBLE
                newFantasyLand.text = resources.getString(R.string.new_fantasyland)
            } else {
                newFantasyLand.visibility = View.GONE
            }
        }
        cardViews.forEach { it.setOnClickListener(null) }
    }

    /*
    * Card views functions
    */

    private fun select(cardView: ImageView) {
        with(cardView) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.cardViewSelected))
            setPadding(4)
        }

        selectedCardView = cardView
    }

    private fun deSelect(cardView: ImageView) {
        with(cardView) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.cardViewBackground))
            setPadding(1)
        }

        selectedCardView = null
    }

    private fun makeMove(cardView1: ImageView, cardView2: ImageView) {
        require(cardView1 != cardView2) { "Card view 1 must be different from card view 2." }

        // swap cards
        val indexOfCard1 = cardViews.indexOf(cardView1)
        val indexOfCard2 = cardViews.indexOf(cardView2)
        viewModel.swapCards(indexOfCard1, indexOfCard2)

        deSelect(cardView1)
    }

    private fun onClickHandler(cardView: ImageView) {
        val isSomeCardViewSelected: Boolean = selectedCardView != null

        if (isSomeCardViewSelected) {
            val isThisCardViewSelected: Boolean = selectedCardView == cardView
            if (isThisCardViewSelected) {
                deSelect(cardView)
            } else {
                makeMove(selectedCardView!!, cardView)
            }
        } else {
            val indexOfCardView = cardViews.indexOf(cardView)
            val isCardOnThisCardView: Boolean = viewModel.cards.value?.get(indexOfCardView) != null
            if (isCardOnThisCardView) select(cardView)
        }
    }
}