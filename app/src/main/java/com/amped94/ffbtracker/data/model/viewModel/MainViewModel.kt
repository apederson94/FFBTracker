package com.amped94.ffbtracker.data.model.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.entity.PlayerAndLeagues
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var _playersAndLeagues: MutableLiveData<List<PlayerAndLeagues>> = MutableLiveData()
    val playersAndLeagues: LiveData<List<PlayerAndLeagues>> = _playersAndLeagues

    var onFABTapped = mutableStateOf({})
    var title = mutableStateOf("FFBTracker")

    init {
        viewModelScope.launch {
            getPlayersAndLeaguesInitial()
        }
    }

    fun getPlayersAndLeaguesInitial() {
        viewModelScope.launch {
            val playerAndLeagues = SleeperRepository.getPlayersAndLeaguesInitial()
            _playersAndLeagues.postValue(playerAndLeagues)
        }
    }

    fun getPlayersAndLeagues() {
        viewModelScope.launch {
            val playersAndLeagues = SleeperRepository.getPlayersAndLeagues()
            _playersAndLeagues.postValue(playersAndLeagues)
        }
    }
}