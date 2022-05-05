package com.example.fantasyland

import android.content.Context
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
import androidx.preference.PreferenceManager
import androidx.window.layout.WindowMetricsCalculator
import com.example.fantasyland.databinding.FragmentGameBinding
import timber.log.Timber

class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("onCreateView called")

        binding = FragmentGameBinding.inflate(inflater)

        fun cardImageResource(card: Card?): Int = resources.getIdentifier(fileName(card), "drawable", requireContext().packageName)

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

        viewModel.cards.observe(viewLifecycleOwner) { newCards ->
            var i = 0
            binding.cardView1.tag = i++
            binding.cardView2.tag = i++
            binding.cardView3.tag = i++
            binding.cardView4.tag = i++
            binding.cardView5.tag = i++
            binding.cardView6.tag = i++
            binding.cardView7.tag = i++
            binding.cardView8.tag = i++
            binding.cardView9.tag = i++
            binding.cardView10.tag = i++
            binding.cardView11.tag = i++
            binding.cardView12.tag = i++
            binding.cardView13.tag = i++
            binding.cardView14.tag = i++
            binding.cardView15.tag = i++
            binding.cardView16.tag = i++
            binding.cardView17.tag = i++
            binding.cardView18.tag = i++
            binding.cardView19.tag = i++
            binding.cardView20.tag = i++
            binding.cardView21.tag = i++
            binding.cardView22.tag = i++
            binding.cardView23.tag = i++
            binding.cardView24.tag = i++
            binding.cardView25.tag = i++
            binding.cardView26.tag = i++
            binding.cardView27.tag = i++
            binding.cardView28.tag = i++
            binding.cardView29.tag = i++
            binding.cardView30.tag = i

            binding.cardView1.setImageResource(cardImageResource(newCards[0]))
            binding.cardView2.setImageResource(cardImageResource(newCards[1]))
            binding.cardView3.setImageResource(cardImageResource(newCards[2]))
            binding.cardView4.setImageResource(cardImageResource(newCards[3]))
            binding.cardView5.setImageResource(cardImageResource(newCards[4]))
            binding.cardView6.setImageResource(cardImageResource(newCards[5]))
            binding.cardView7.setImageResource(cardImageResource(newCards[6]))
            binding.cardView8.setImageResource(cardImageResource(newCards[7]))
            binding.cardView9.setImageResource(cardImageResource(newCards[8]))
            binding.cardView10.setImageResource(cardImageResource(newCards[9]))
            binding.cardView11.setImageResource(cardImageResource(newCards[10]))
            binding.cardView12.setImageResource(cardImageResource(newCards[11]))
            binding.cardView13.setImageResource(cardImageResource(newCards[12]))
            binding.cardView14.setImageResource(cardImageResource(newCards[13]))
            binding.cardView15.setImageResource(cardImageResource(newCards[14]))
            binding.cardView16.setImageResource(cardImageResource(newCards[15]))
            binding.cardView17.setImageResource(cardImageResource(newCards[16]))
            binding.cardView18.setImageResource(cardImageResource(newCards[17]))
            binding.cardView19.setImageResource(cardImageResource(newCards[18]))
            binding.cardView20.setImageResource(cardImageResource(newCards[19]))
            binding.cardView21.setImageResource(cardImageResource(newCards[20]))
            binding.cardView22.setImageResource(cardImageResource(newCards[21]))
            binding.cardView23.setImageResource(cardImageResource(newCards[22]))
            binding.cardView24.setImageResource(cardImageResource(newCards[23]))
            binding.cardView25.setImageResource(cardImageResource(newCards[24]))
            binding.cardView26.setImageResource(cardImageResource(newCards[25]))
            binding.cardView27.setImageResource(cardImageResource(newCards[26]))
            binding.cardView28.setImageResource(cardImageResource(newCards[27]))
            binding.cardView29.setImageResource(cardImageResource(newCards[28]))
            binding.cardView30.setImageResource(cardImageResource(newCards[29]))
        }

        viewModel.isGameFinished.observe(viewLifecycleOwner) { newIsGameFinished ->
            if (newIsGameFinished) {
                binding.apply {
                    buttonSort.visibility = View.GONE
                    buttonSetAllCards.visibility = View.GONE
                    buttonDone.visibility = View.GONE
                    buttonNewGame.visibility = View.VISIBLE
                    buttonShare.visibility = View.VISIBLE
                    if (viewModel.isValidResult) {
                        val resultOKColor: Int = ContextCompat.getColor(requireContext(), R.color.resultOK)
                        topRowResult.visibility = View.VISIBLE
                        middleRowResult.visibility = View.VISIBLE
                        bottomRowResult.visibility = View.VISIBLE
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
                    finalResult.visibility = View.VISIBLE
                }

                if (viewModel.isRepeatedFantasy) {
                    binding.apply {
                        newFantasyLand.text = resources.getString(R.string.new_fantasyland)
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.i("onViewCreated called")

        // card views settings
        val bounds = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(requireActivity())
            .bounds
        val displayWidth = bounds.width()
        val cardViewWidth: Int = displayWidth / 22
        val cardViewHeight: Int = (cardViewWidth * 1.4).toInt()

        val cardViewMargin = 8
        val cardViewPadding = 1
        val cardViewBackgroundColor: Int = ContextCompat.getColor(requireContext(), R.color.cardViewBackground)

        val topRowCardViews = listOf(
            binding.cardView11,
            binding.cardView12,
            binding.cardView13
        )
        val middleRowCardViews = listOf(
            binding.cardView6,
            binding.cardView7,
            binding.cardView8,
            binding.cardView9,
            binding.cardView10
        )
        val bottomRowCardViews = listOf(
            binding.cardView1,
            binding.cardView2,
            binding.cardView3,
            binding.cardView4,
            binding.cardView5
        )
        val dealtCardViews = listOf(
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
            binding.cardView29,
            binding.cardView30
        )

        val allCardViews = listOf(topRowCardViews, middleRowCardViews, bottomRowCardViews, dealtCardViews).flatten()
        allCardViews.forEach { cardView ->
            with(cardView) {
                layoutParams = LinearLayout.LayoutParams(cardViewWidth, cardViewHeight)
                updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(cardViewMargin) }
                setPadding(cardViewPadding)
                setBackgroundColor(cardViewBackgroundColor)
            }
        }

        // setting visibility of unused cardviews to GONE
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val numberOfCardsInFantasyLand: Int = preferences.getString("number_of_cards_in_fantasy_land", "14")?.toInt()!!
        dealtCardViews.drop(numberOfCardsInFantasyLand).forEach { it.visibility = View.GONE }

        // cardView functions
        var selectedView: ImageView? = null

        fun select(cardView: ImageView) {
            with(cardView) {
                setBackgroundColor(ContextCompat.getColor(context, R.color.cardViewSelected))
                setPadding(4)
            }

            selectedView = cardView
        }

        fun deSelect(cardView: ImageView) {
            with(cardView) {
                setBackgroundColor(ContextCompat.getColor(context, R.color.cardViewBackground))
                setPadding(1)
            }

            selectedView = null
        }

        fun makeMove(cardView: ImageView) {
            // swap cards
            val indexOfCard1 = selectedView!!.tag as Int
            val indexOfCard2 = cardView.tag as Int
            viewModel.swapCards(indexOfCard1, indexOfCard2)

            deSelect(selectedView!!)
        }

        fun onClickHandler(cardView: ImageView) {
            val isSomeTileSelected: Boolean = selectedView != null

            if (isSomeTileSelected) {
                val isThisTileSelected: Boolean = selectedView == cardView
                if (isThisTileSelected) {
                    deSelect(cardView)
                } else {
                    makeMove(cardView)
                }
            } else {
                val indexOfCard = cardView.tag as Int
                val isCardOnThisTile: Boolean = viewModel.cards.value?.get(indexOfCard) != null
                if (isCardOnThisTile) select(cardView)
            }
        }

        // set onClickListener on cardViews
        allCardViews.forEach { cardView -> cardView.setOnClickListener { onClickHandler(cardView) } }

        // sort button
        binding.buttonSort.setOnClickListener {
            selectedView?.let { deSelect(selectedView!!) }
            viewModel.sortCards()
        }

        // set all cards button
        binding.buttonSetAllCards.setOnClickListener {
            selectedView?.let { deSelect(selectedView!!) }
            viewModel.setAllCards()
        }

        // done button
        binding.buttonDone.setOnClickListener {
            selectedView?.let { deSelect(selectedView!!) }

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
            shareIntent.setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, binding.finalResult.text)
            startActivity(shareIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("onDestroyView called")
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