package com.amped94.ffbtracker.data.model.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.api.SleeperApi
import com.amped94.ffbtracker.data.api.model.SleeperLeagueResponse
import com.amped94.ffbtracker.data.api.model.SleeperPlayerResponse
import com.amped94.ffbtracker.data.api.model.SleeperUserResponse
import com.amped94.ffbtracker.data.model.db.entity.Player
import com.amped94.ffbtracker.data.model.db.entity.User
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user

    private var _players: MutableLiveData<List<Player>> = MutableLiveData()
    val players: LiveData<List<Player>> = _players

    init {
        viewModelScope.launch {
            getSleeperAccountDetails("apederson94")
        }
    }

    fun getSleeperAccountDetails(username: String) {
        viewModelScope.launch {
            val user = SleeperRepository.getUser(username)
            val leagues = SleeperRepository.getLeagues(user)
            val playersToFind = leagues.map { it.players }.reduce { result, players ->
                val newList = result.toMutableSet()
                newList.addAll(players)
                newList.toList()
            }
            val players = SleeperRepository.getPlayersById(playersToFind)

            _players.postValue(players)
            _user.postValue(user)
        }
    }

    fun getPlayers() {
        viewModelScope.launch {
            val players = SleeperRepository.getPlayersById(listOf("1", "2", "99", "981", "204"))
            _players.postValue(players)
        }
    }
}