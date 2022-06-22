package com.example.fantasyland

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.fantasyland.databinding.FragmentGamesArchiveBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamesArchiveFragment : Fragment() {

    private val viewModel: GamesArchiveViewModel by viewModels()
    private var _binding: FragmentGamesArchiveBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGamesArchiveBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.gamesString.observe(viewLifecycleOwner) { newGamesString ->
            binding.gamesList.text = newGamesString.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}