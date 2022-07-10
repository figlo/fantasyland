package com.example.fantasyland

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fantasyland.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var buttonViews: List<Button>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpButtonViews()
        setUpNavigationToGameFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpButtonViews() {
        binding.apply {
            buttonViews = listOf(
                button13,
                button14,
                button15,
                button16,
                button17,
            )
        }
    }

    private fun setUpNavigationToGameFragment() {
        fun navigateToGameFragment(numberOfCards: Int) {
            this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToGameFragment(numberOfCards))
        }

        for ((index, button) in buttonViews.withIndex()) {
            button.setOnClickListener {
                navigateToGameFragment(index + 13)
            }
        }
    }
}