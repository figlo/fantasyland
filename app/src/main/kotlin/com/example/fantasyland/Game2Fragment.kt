package com.example.fantasyland

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.window.layout.WindowMetricsCalculator
import com.example.fantasyland.databinding.FragmentGame2Binding


class Game2Fragment : Fragment() {
    private lateinit var binding: FragmentGame2Binding
    private lateinit var viewModel: Game2ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGame2Binding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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

        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val numberOfCardsInFantasyLand: Int = preferences.getString("number_of_cards_in_fantasy_land", "14")?.toInt()!!

        dealtCardViews.drop(numberOfCardsInFantasyLand).forEach { it.visibility = View.GONE }
    }
}