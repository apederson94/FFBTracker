package com.amped94.ffbtracker.data.model.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.api.SleeperApi
import com.amped94.ffbtracker.data.model.data.SleeperLeague
import com.amped94.ffbtracker.data.model.data.SleeperPlayer
import com.amped94.ffbtracker.data.model.data.SleeperUser
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var _user: MutableLiveData<SleeperUser> = MutableLiveData(SleeperUser())
    val user: LiveData<SleeperUser> = _user

    private var _leagues: MutableLiveData<List<SleeperLeague>> = MutableLiveData(listOf())
    val leagues: LiveData<List<SleeperLeague>> = _leagues

    private var _players: MutableLiveData<List<SleeperPlayer>> = MutableLiveData(listOf())
    val players: LiveData<List<SleeperPlayer>> = _players

    init {
        getUser("apederson94")
    }

    fun getUser(username: String) {
        viewModelScope.launch {
            val user = SleeperApi.getSleeperUser(username)
            _user.postValue(user)
            getLeagues(user.userId)
        }
    }

    fun getLeagues(userId: String) {
        viewModelScope.launch {
            val listOfLeagues = SleeperApi.getSleeperLeagues(userId)
            _leagues.postValue(listOfLeagues)
            getPlayers(listOfLeagues, userId)
        }
    }

    fun getPlayers(leagues: List<SleeperLeague>, userId: String) {
        viewModelScope.launch {
            val allPlayersMap = hashMapOf<String, MutableList<SleeperLeague>>()
            leagues.forEach { league ->
                val leagueParticipant = SleeperApi.getLeagueParticipants(league).first { participant ->
                    participant.ownerId == userId
                }

                leagueParticipant.players.forEach { playerId ->
                    allPlayersMap[playerId]?.add(league) ?: run {
                        val newList = mutableListOf(league)
                        allPlayersMap[playerId] = newList
                    }
                }
            }

            val allPlayers = allPlayersMap.map {
                SleeperPlayer(it.key, it.value.toList())
            }

            _players.postValue(allPlayers)
        }
    }
}