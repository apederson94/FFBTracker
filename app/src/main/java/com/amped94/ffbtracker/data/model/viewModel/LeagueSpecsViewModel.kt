package com.amped94.ffbtracker.data.model.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LeagueSpecsViewModel : ViewModel() {
    val leagueName = MutableLiveData<String>()
    val numberOfQB = MutableLiveData(1)
    val numberOfRB = MutableLiveData(2)
    val numberOfWR = MutableLiveData(2)
    val numberOfTE = MutableLiveData(1)
    val numberOfFLEX = MutableLiveData(1)
    val numberOfK = MutableLiveData(1)
    val numberOfDST = MutableLiveData(1)
    val numberOfSuperFLEX = MutableLiveData(0)
    val numberOfBench = MutableLiveData(7)
}