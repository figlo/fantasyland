package com.example.fantasyland

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fantasyland.databinding.FragmentGamesArchiveBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamesArchiveFragment : Fragment() {

    private val viewModel: GamesArchiveViewModel by viewModels()
    private var _binding: FragmentGamesArchiveBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGamesArchiveBinding.inflate(inflater)

        binding.gamesArchiveRecyclerView.layoutManager = LinearLayoutManager(context)

        val games = viewModel.games
//        val adapter = GameListAdapter(games.value)
//        binding.gamesArchiveRecyclerView.adapter = adapter

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