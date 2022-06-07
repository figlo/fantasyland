package com.example.fantasyland

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.example.fantasyland.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val spinnerValue: String = preferences.getString("number_of_cards_in_fantasy_land", "14") ?: "14"
        val spinnerIndex: Int = spinnerValue.toInt() - 13
        binding.spinnerNumberOfFantasyLandCards.setSelection(spinnerIndex)

        binding.spinnerNumberOfFantasyLandCards.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                preferences.edit().putString("number_of_cards_in_fantasy_land", selectedItem).apply()
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