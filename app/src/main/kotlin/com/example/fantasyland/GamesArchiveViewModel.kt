package com.example.fantasyland

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fantasyland.data.FantasyLandDao
import com.example.fantasyland.data.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GamesArchiveViewModel @Inject constructor(
    dao: FantasyLandDao
) : ViewModel() {

    val games: LiveData<List<Game>> = dao.getAllGames()
}