package com.example.fantasyland

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.window.layout.WindowMetricsCalculator
import com.example.fantasyland.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private lateinit var buttons: List<Button>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpButtonsList()
        setUpButtonsParams()
        setUpNavigationToGameFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpButtonsList() {
        binding.apply {
            buttons = listOf(
                button13,
                button14,
                button15,
                button16,
                button17,
            )
        }
    }

    private fun setUpButtonsParams() {
        // calculating button width and height
        val bounds = WindowMetricsCalculator
            .getOrCreate()
            .computeCurrentWindowMetrics(requireActivity())
            .bounds
        val displayWidth = bounds.width()
        val buttonWidth: Int = displayWidth / 6                  // empirically found out
        val buttonHeight: Int = buttonWidth

        // margin must be set again programmatically (allready set in xml)
        val buttonMargin = 20

        for (button in buttons) {
            with(button) {
                // setup button width and height
                layoutParams = android.widget.LinearLayout.LayoutParams(buttonWidth, buttonHeight)

                // setup button margins
                val params = button.layoutParams as ViewGroup.MarginLayoutParams
                params.setMargins(buttonMargin)
                layoutParams = params
            }
        }
    }

    private fun setUpNavigationToGameFragment() {
        fun navigateToGameFragment(numberOfCards: Int) {
            this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToGameFragment(numberOfCards))
        }

        for ((index, button) in buttons.withIndex()) {
            button.setOnClickListener {
                navigateToGameFragment(index + 13)
            }
        }
    }
}