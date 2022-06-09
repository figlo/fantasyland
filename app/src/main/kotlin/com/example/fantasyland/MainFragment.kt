package com.example.fantasyland

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.fantasyland.data.UserPreferencesRepository
import com.example.fantasyland.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var numberOfCardsInFantasyLand = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(
                UserPreferencesRepository(requireContext().dataStore)
            )
        )[MainViewModel::class.java]

        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        viewModel.userPreferencesLiveData.observe(viewLifecycleOwner) { userPreferencesLiveData ->
//            numberOfCardsInFantasyLand = userPreferencesLiveData.numberOfCardsInFantasyLand
//        }

        numberOfCardsInFantasyLand = runBlocking { requireContext().dataStore.data.first()[UserPreferencesRepository.PreferencesKeys.NUMBER_OF_CARDS_IN_FANTASY_LAND] } ?: 0

        val spinnerValue= numberOfCardsInFantasyLand
        val spinnerIndex: Int = spinnerValue - 13
        binding.spinnerNumberOfFantasyLandCards.setSelection(spinnerIndex)

        binding.spinnerNumberOfFantasyLandCards.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString().toInt()
                viewModel.setNumberOfCardsInFantasyLand(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.buttonPlay.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_gameFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}