package com.example.fantasyland

import android.text.Spanned
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.fantasyland.data.FantasyLandDao
import com.example.fantasyland.data.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

@HiltViewModel
class GamesArchiveViewModel @Inject constructor(
    dao: FantasyLandDao
) : ViewModel() {

    private val games: LiveData<List<Game>> = dao.getAllGames()
    val gamesString = Transformations.map(games) { games ->
        formatGames(games)
    }
}

fun formatGames(games: List<Game>): Spanned {
    val sb = StringBuilder()
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault())
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