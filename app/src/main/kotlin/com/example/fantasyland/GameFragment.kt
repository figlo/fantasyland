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
import androidx.navigation.findNavController
import androidx.window.layout.WindowMetricsCalculator
import com.example.fantasyland.databinding.FragmentGameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*
        * Setup card views
        */

        val bottomRowCardViews = listOf(
            binding.cardView0,
            binding.cardView1,
            binding.cardView2,
            binding.cardView3,
            binding.cardView4
        )
        val middleRowCardViews = listOf(
            binding.cardView5,
            binding.cardView6,
            binding.cardView7,
            binding.cardView8,
            binding.cardView9
        )
        val topRowCardViews = listOf(
            binding.cardView10,
            binding.cardView11,
            binding.cardView12
        )
        val dealtCardViews = listOf(
            binding.cardView13,
            binding.cardView14,
            binding.cardView15,
            binding.cardView16,
            binding.cardView17,
            binding.cardView18,
            binding.cardView19,
            binding.cardView20,
            binding.cardView21,
            binding.cardView22,
            binding.cardView23,
            binding.cardView24,
            binding.cardView25,
            binding.cardView26,
            binding.cardView27,
            binding.cardView28,
            binding.cardView29
        )
        val allCardViews = listOf(
            bottomRowCardViews,
            middleRowCardViews,
            topRowCardViews,
            dealtCardViews
        ).flatten()

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

        for (cardView in allCardViews) {
            with(cardView) {
                layoutParams = LinearLayout.LayoutParams(cardViewWidth, cardViewHeight)
                updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(cardViewMargin) }
                setPadding(cardViewPadding)
            }
        }

        // setting visibility of unused card views to GONE
        val numberOfCardsInFantasyLand = viewModel.numberOfCardsInFantasyLand
        dealtCardViews.drop(numberOfCardsInFantasyLand).forEach { it.visibility = View.GONE }

        /*
        * Card views functions
        */

        var selectedCardView: ImageView? = null

        fun select(cardView: ImageView) {
            with(cardView) {
                setBackgroundColor(ContextCompat.getColor(context, R.color.cardViewSelected))
                setPadding(4)
            }

            selectedCardView = cardView
        }

        fun deSelect(cardView: ImageView) {
            with(cardView) {
                setBackgroundColor(ContextCompat.getColor(context, R.color.cardViewBackground))
                setPadding(1)
            }

            selectedCardView = null
        }

        fun makeMoveTo(cardView: ImageView) {
            // swap cards
            val indexOfCard1 = allCardViews.indexOf(selectedCardView!!)
            val indexOfCard2 = allCardViews.indexOf(cardView)
            viewModel.swapCards(indexOfCard1, indexOfCard2)

            deSelect(selectedCardView!!)
        }

        fun onClickHandler(cardView: ImageView) {
            val isSomeCardViewSelected: Boolean = selectedCardView != null

            if (isSomeCardViewSelected) {
                val isThisCardViewSelected: Boolean = selectedCardView == cardView
                if (isThisCardViewSelected) {
                    deSelect(cardView)
                } else {
                    makeMoveTo(cardView)
                }
            } else {
                val indexOfCardView = allCardViews.indexOf(cardView)
                val isCardOnThisCardView: Boolean = viewModel.cards.value?.get(indexOfCardView) != null
                if (isCardOnThisCardView) select(cardView)
            }
        }

        /*
        * Setup listeners
        */

        // cardViews
        allCardViews.forEach { cardView -> cardView.setOnClickListener { onClickHandler(cardView) } }

        // sort button
        binding.buttonSort.setOnClickListener {
            selectedCardView?.let { deSelect(selectedCardView!!) }
            viewModel.sortCards()
        }

        // set all cards button
        binding.buttonSetAllCards.setOnClickListener {
            selectedCardView?.let { deSelect(selectedCardView!!) }
            viewModel.setAllCards()
        }

        // done button
        binding.buttonDone.setOnClickListener {
            selectedCardView?.let { deSelect(selectedCardView!!) }
            allCardViews.forEach { it.setOnClickListener(null) }
            viewModel.evaluateGame()
        }

        // new game button
        binding.buttonNewGame.setOnClickListener {
            val navController = it.findNavController()
            val id: Int? = navController.currentDestination?.id
            navController.popBackStack(id!!, true)
            navController.navigate(id)
        }

        // share button
        binding.buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
                .putExtra(Intent.EXTRA_TEXT, binding.finalResult.text)
            startActivity(shareIntent)
        }

        /*
        * Setup observers
        */

        // setting card images in card views
        viewModel.cards.observe(viewLifecycleOwner) { newCards ->
            fun fileName(card: Card?): String =
                if (card == null)
                    "empty_card"
                else
                    "card_" + card.name.takeLast(2).lowercase()

            for ((index, cardView) in allCardViews.withIndex()) {
                val imageResource = resources.getIdentifier(fileName(newCards[index]), "drawable", requireContext().packageName)
                cardView.setImageResource(imageResource)
            }
        }

        // managing visibility of buttons after all cards are set to rows
        viewModel.isMovingPhaseDone.observe(viewLifecycleOwner) { newIsMovingPhaseDone ->
            if (newIsMovingPhaseDone) {
                binding.buttonSort.visibility = View.GONE
                binding.buttonSetAllCards.visibility = View.GONE
                binding.buttonDone.visibility = View.VISIBLE
            } else {
                binding.buttonSort.visibility = View.VISIBLE
                binding.buttonSetAllCards.visibility = View.VISIBLE
                binding.buttonDone.visibility = View.GONE
            }
        }

        // displaying result of the game
        viewModel.isGameFinished.observe(viewLifecycleOwner) { newIsGameFinished ->
            if (newIsGameFinished) {
                binding.apply {
                    buttonSort.visibility = View.GONE
                    buttonSetAllCards.visibility = View.GONE
                    buttonDone.visibility = View.GONE
                    buttonNewGame.visibility = View.VISIBLE
                    buttonShare.visibility = View.VISIBLE
                    finalResult.visibility = View.VISIBLE

                    if (viewModel.isValidResult) {
                        val resultOKColor: Int = ContextCompat.getColor(requireContext(), R.color.resultOK)
                        bottomRowResult.visibility = View.VISIBLE
                        middleRowResult.visibility = View.VISIBLE
                        topRowResult.visibility = View.VISIBLE
                        bottomRowResult.text = viewModel.bottomRowResult.toString()
                        middleRowResult.text = viewModel.middleRowResult.toString()
                        topRowResult.text = viewModel.topRowResult.toString()
                        finalResult.text = viewModel.finalResult.toString()
                        finalResult.setTextColor(resultOKColor)
                    } else {
                        val resultXColor: Int = ContextCompat.getColor(requireContext(), R.color.resultX)
                        finalResult.text = resources.getString(R.string.result_x)
                        finalResult.setTextColor(resultXColor)
                    }

                    if (viewModel.isRepeatedFantasy) {
                        newFantasyLand.text = resources.getString(R.string.new_fantasyland)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}