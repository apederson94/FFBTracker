package com.amped94.ffbtracker.data.model.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.entity.PlayerAndLeagues
import com.amped94.ffbtracker.data.model.db.entity.UserAndLeagues
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
//    private var _user: MutableLiveData<UserAndLeagues> = MutableLiveData()
//    val user: LiveData<UserAndLeagues> = _user

    private var _playersAndLeagues: MutableLiveData<List<PlayerAndLeagues>> = MutableLiveData()
    val playersAndLeagues: LiveData<List<PlayerAndLeagues>> = _playersAndLeagues

    init {
        viewModelScope.launch {
//            getSleeperAccountDetails("apederson94")
            getPlayersAndLeagues()
        }
    }

//    fun getSleeperAccountDetails(username: String) {
//        viewModelScope.launch {
//            val user = SleeperRepository.getUserAndLeagues(username)
//            _user.postValue(user)
//        }
//    }

    fun getPlayersAndLeagues() {
        viewModelScope.launch {
            val playersAndLeagues = SleeperRepository.getPlayersAndLeagues()
            _playersAndLeagues.postValue(playersAndLeagues)
        }
    }
}