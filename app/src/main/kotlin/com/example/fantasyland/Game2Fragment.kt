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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGame2Binding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // card views settings
        val bounds = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(requireActivity()).bounds
        val displayWidth = bounds.width()
        val cardWidth: Int = displayWidth / 22
        val cardHeight: Int = (cardWidth * 1.4).toInt()

        val imageViewMargin = 8
        val imageViewPadding = 1
        val imageViewBackgroundColor: Int = ContextCompat.getColor(requireContext(), R.color.cardViewBackground)

        val topRowViews = listOf(
            binding.cardView11,
            binding.cardView12,
            binding.cardView13
        )
        val middleRowViews = listOf(
            binding.cardView6,
            binding.cardView7,
            binding.cardView8,
            binding.cardView9,
            binding.cardView10
        )
        val bottomRowViews = listOf(
            binding.cardView1,
            binding.cardView2,
            binding.cardView3,
            binding.cardView4,
            binding.cardView5
        )
        val dealtCardsViews = listOf(
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
        val allCardsViews = listOf(topRowViews, middleRowViews, bottomRowViews, dealtCardsViews).flatten()

        allCardsViews.forEach {
            it.layoutParams = LinearLayout.LayoutParams(cardWidth, cardHeight)
            it.updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(imageViewMargin) }
            it.setPadding(imageViewPadding)
            it.setBackgroundColor(imageViewBackgroundColor)
        }

        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val numberOfCardsInFantasyLand: Int = preferences.getString("number_of_cards_in_fantasy_land", "14")?.toInt()!!

        dealtCardsViews.drop(numberOfCardsInFantasyLand).forEach { it.visibility = View.GONE }
    }
}