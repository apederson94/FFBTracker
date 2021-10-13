package com.amped94.ffbtracker.data.model.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.entity.UserAndLeagues
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var _user: MutableLiveData<UserAndLeagues> = MutableLiveData()
    val user: LiveData<UserAndLeagues> = _user

    init {
        viewModelScope.launch {
            getSleeperAccountDetails("apederson94")
        }
    }

    fun getSleeperAccountDetails(username: String) {
        viewModelScope.launch {
            val user = SleeperRepository.getUserAndLeagues(username)
            _user.postValue(user)
        }
    }
}