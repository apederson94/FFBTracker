package com.amped94.ffbtracker.data.model.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.entity.Player
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch

class PlayerFieldViewModel : ViewModel() {
    val text = MutableLiveData("")
    val selectedPlayer = MutableLiveData<Player>()
    val autofillSuggestions: MutableLiveData<List<Player>> = MutableLiveData()

    fun getAutofillSuggestions(position: Position, searchText: String) {
        viewModelScope.launch {
            val suggestions =
                if (
                    position != Position.Bench
                    && !position.isSuperFLEX()
                ) {
                    val searchPosition = if (position == Position.DST) "DEF" else position.title
                    SleeperRepository.db.playerDao()
                        .searchPlayersByPosition(searchPosition, searchText)
                } else {
                    SleeperRepository.db.playerDao().searchPlayers(searchText)
                }
            autofillSuggestions.postValue(suggestions)
        }
    }

    fun selectPlayer(player: Player) {
        text.postValue("${player.firstName} ${player.lastName}")
        selectedPlayer.postValue(player)
        autofillSuggestions.postValue(emptyList())
    }
}