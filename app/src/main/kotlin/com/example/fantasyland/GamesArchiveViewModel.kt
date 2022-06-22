package com.example.fantasyland

import android.app.Application
import android.content.res.Resources
import android.text.Spanned
import androidx.core.text.HtmlCompat
import androidx.lifecycle.*
import com.example.fantasyland.data.FantasyLandDao
import com.example.fantasyland.data.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class GamesArchiveViewModel @Inject constructor(
    private val dao: FantasyLandDao,
    application: Application
) : ViewModel() {

    private val games: LiveData<List<Game>> = dao.getAllGames()
    val gamesString = Transformations.map(games) { games ->
        formatGames(games, application.resources)
    }

    var count: MutableLiveData<Long> = MutableLiveData(0)

    init {
        getCount()
    }

    private fun getCount() {
        viewModelScope.launch {
            count.value = suspendCount()
        }
    }

    private suspend fun suspendCount(): Long {
        return dao.count()
    }
}

fun formatGames(games: List<Game>, resources: Resources): Spanned {
    val sb = StringBuilder()
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss").withZone(ZoneId.systemDefault())
    sb.apply {
        append("Games: ")
        append(games.size)
        append("<br>")
        games.forEach { game ->
            append("<br><br>")
            append(formatter.format(game.dateTime))
            append("\t" + game.nickName)
        }
    }
    return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
}