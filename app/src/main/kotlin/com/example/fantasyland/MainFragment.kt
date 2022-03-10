package com.example.fantasyland

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.example.fantasyland.databinding.FragmentMainBinding
import timber.log.Timber

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("onCreateView called")

        binding = FragmentMainBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.i("onViewCreated called")

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("onAttach called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("onDestroyView called")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.i("onDetach called")
    }
}