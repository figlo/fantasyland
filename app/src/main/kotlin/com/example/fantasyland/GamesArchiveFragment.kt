package com.example.fantasyland

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fantasyland.databinding.FragmentGamesArchiveBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.games.collect { newGames ->
                    binding.gamesArchiveRecyclerView.adapter = GameListAdapter(newGames) { gameId ->
                            // TODO navigation to game detail
                            // findNavController().navigate(GameListFragmentDirections.action___)

                            Snackbar.make(
                                requireView(),
                                "Game $gameId clicked",
                                Snackbar.LENGTH_LONG
                            ).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}