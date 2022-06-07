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
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.window.layout.WindowMetricsCalculator
import com.example.fantasyland.data.UserPreferencesRepository
import com.example.fantasyland.databinding.FragmentGameBinding
import timber.log.Timber

private const val USER_PREFERENCES_NAME = "user_preferences"

private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class GameFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("onCreateView called")

        viewModel = ViewModelProvider(
            this,
            GameViewModelFactory(
                UserPreferencesRepository(requireContext().dataStore)
            )
        )[GameViewModel::class.java]

        _binding = FragmentGameBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.i("onViewCreated called")

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
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val numberOfCardsInFantasyLand: Int = preferences.getString("number_of_cards_in_fantasy_land", "14")?.toInt()!!
        dealtCardViews.drop(numberOfCardsInFantasyLand).forEach { it.visibility = View.GONE }

        /*
        * Card views functions
        */

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

        fun makeMoveTo(cardView: ImageView) {
            // swap cards
            val indexOfCard1 = allCardViews.indexOf(selectedView!!)
            val indexOfCard2 = allCardViews.indexOf(cardView)
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
                    makeMoveTo(cardView)
                }
            } else {
                val indexOfCard = allCardViews.indexOf(cardView)
                val isCardOnThisTile: Boolean = viewModel.cards.value?.get(indexOfCard) != null
                if (isCardOnThisTile) select(cardView)
            }
        }

        /*
        * Setup listeners
        */

        // cardViews
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