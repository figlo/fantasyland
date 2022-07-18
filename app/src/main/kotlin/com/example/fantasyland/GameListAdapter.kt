package com.example.fantasyland

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fantasyland.data.Game
import com.example.fantasyland.databinding.ListItemGameBinding
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class GameHolder(
    private val binding: ListItemGameBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(game: Game, onGameClicked: (Long) -> Unit) {
        binding.apply {
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault())
            val dateString = formatter.format(game.dateTime)
            gameDate.text = dateString

            gameNickname.text = game.nickName
            gameNumberOfCards.text = game.numberOfCardsInFantasyLand.toString()
            gameResult.text = game.result.toString()

            root.setOnClickListener {
                onGameClicked(game.gameId)
            }
        }
    }
}

class GameListAdapter(
    private val games: List<Game>,
    private val onGameClicked: (Long) -> Unit,
) : RecyclerView.Adapter<GameHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemGameBinding.inflate(inflater, parent, false)
        return GameHolder(binding)
    }

    override fun onBindViewHolder(holder: GameHolder, position: Int) {
        val game = games[position]
        holder.bind(game, onGameClicked)
    }

    override fun getItemCount() = games.size
}