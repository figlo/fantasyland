package com.example.fantasyland

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fantasyland.data.FantasyLandDao
import com.example.fantasyland.data.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesArchiveViewModel @Inject constructor(
    private val dao: FantasyLandDao
) : ViewModel() {

    private val _games: MutableStateFlow<List<Game>> = MutableStateFlow(emptyList())
    val games: StateFlow<List<Game>>
    get() = _games.asStateFlow()

    init {
        viewModelScope.launch {
            dao.getAllGames().collect {
                _games.value = it
            }
        }
    }
}