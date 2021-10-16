package com.amped94.ffbtracker.data.model.viewModel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.FantasyProvider
import com.amped94.ffbtracker.data.model.db.entity.League
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch

class LeagueSpecsViewModel : ViewModel() {
    val leagueName = MutableLiveData(TextFieldValue())
    val numberOfQB = MutableLiveData(1)
    val numberOfRB = MutableLiveData(2)
    val numberOfWR = MutableLiveData(2)
    val numberOfTE = MutableLiveData(1)
    val numberOfFLEX = MutableLiveData(1)
    val numberOfK = MutableLiveData(1)
    val numberOfDST = MutableLiveData(1)
    val numberOfSuperFLEX = MutableLiveData(0)
    val numberOfBench = MutableLiveData(7)

    fun saveLeague() {
        viewModelScope.launch {
            SleeperRepository.saveLeague(League(
                leagueId = 0,
                externalLeagueId = "",
                associatedUserId = -1,
                name = leagueName.value?.text ?: "",
                type = FantasyProvider.Custom,
                numQB = numberOfQB.value ?: 0,
                numRB = numberOfRB.value ?: 0,
                numWR = numberOfWR.value ?: 0,
                numTE = numberOfTE.value ?: 0,
                numFLEX = numberOfFLEX.value ?: 0,
                numK = numberOfK.value ?: 0,
                numDST = numberOfDST.value ?: 0,
                numSuperFLEX = numberOfSuperFLEX.value ?: 0,
                numBench = numberOfBench.value ?: 0,
            ))
        }
    }
}