package com.amped94.ffbtracker.data.model.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LeagueSpecsViewModel: ViewModel() {
    val leagueName = MutableLiveData<String>()
    val numberOfQB = MutableLiveData<Int>(1)
    val numberOfRB = MutableLiveData<Int>(2)
    val numberOfWR = MutableLiveData<Int>(2)
    val numberOfTE = MutableLiveData<Int>(1)
    val numberOfFLEX = MutableLiveData<Int>(1)
    val numberOfK = MutableLiveData<Int>(1)
    val numberOfDST = MutableLiveData<Int>(1)
    val numberOfSuperFLEX = MutableLiveData<Int>(0)
}