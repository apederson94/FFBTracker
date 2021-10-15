package com.amped94.ffbtracker.data.model.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amped94.ffbtracker.data.model.db.entity.Player

class LeaguesViewModel : ViewModel() {
    val leagueName: MutableLiveData<String> = MutableLiveData("")
    val players: MutableLiveData<Player> = MutableLiveData()
    val positions = listOf(
        Position.SuperFLEX.QB,
        Position.SuperFLEX.FLEX.RB,
        Position.SuperFLEX.FLEX.RB,
        Position.SuperFLEX.FLEX.WR,
        Position.SuperFLEX.FLEX.WR,
        Position.SuperFLEX.FLEX.TE,
        Position.SuperFLEX.FLEX,
        Position.K,
        Position.DEF,
    )
}

sealed class Position(val title: String) {

    object SuperFLEX : Position(title = "SuperFLEX") {
        object QB : Position(title = "QB")
        object FLEX : Position(title = "FLEX") {
            object RB : Position(title = "RB")
            object WR : Position(title = "WR")
            object TE : Position(title = "TE")
        }
    }

    object K : Position(title = "K")
    object DEF : Position(title = "D/ST")
}