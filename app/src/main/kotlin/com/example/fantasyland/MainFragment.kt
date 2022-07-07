package com.example.fantasyland

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fantasyland.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fun navigateToGameFragment(numberOfCards: Int) {
            this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToGameFragment(numberOfCards))
        }

        binding.button13.setOnClickListener {
            navigateToGameFragment(13)
        }

        binding.button14.setOnClickListener {
            navigateToGameFragment(14)
        }

        binding.button15.setOnClickListener {
            navigateToGameFragment(15)
        }

        binding.button16.setOnClickListener {
            navigateToGameFragment(16)
        }

        binding.button17.setOnClickListener {
            navigateToGameFragment(17)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}