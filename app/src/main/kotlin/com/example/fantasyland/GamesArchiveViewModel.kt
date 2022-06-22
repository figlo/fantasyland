package com.example.fantasyland

import androidx.lifecycle.ViewModel
import com.example.fantasyland.data.FantasyLandDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GamesArchiveViewModel @Inject constructor(
    private val dao: FantasyLandDao
) : ViewModel() {

}