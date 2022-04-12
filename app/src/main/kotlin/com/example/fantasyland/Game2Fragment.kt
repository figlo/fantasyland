package com.example.fantasyland

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

        viewModel = ViewModelProvider(this)[Game2ViewModel::class.java]

        fun cardImageResource(card: Card?): Int = resources.getIdentifier(fileName(card), "drawable", requireContext().packageName)

        viewModel.cards.observe(viewLifecycleOwner) { newCards ->
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

        // unused cardviews gone
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val numberOfCardsInFantasyLand: Int = preferences.getString("number_of_cards_in_fantasy_land", "14")?.toInt()!!
        dealtCardViews.drop(numberOfCardsInFantasyLand).forEach { it.visibility = View.GONE }

        // imageView functions
        var selectedView: ImageView? = null

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
    }
}