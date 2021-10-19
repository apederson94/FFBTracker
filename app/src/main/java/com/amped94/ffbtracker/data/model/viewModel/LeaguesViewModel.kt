package com.amped94.ffbtracker.data.model.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.entity.League
import com.amped94.ffbtracker.data.model.db.entity.LeagueAndPlayers
import com.amped94.ffbtracker.data.repository.SleeperRepository
import com.amped94.ffbtracker.ui.theme.*
import kotlinx.coroutines.launch

class LeaguesViewModel : ViewModel() {
    var isDeleteAlertShowing = mutableStateOf(false)
    var leagueToDelete = mutableStateOf<League?>(null)

    val leaguesAndPlayers = mutableStateListOf<LeagueAndPlayers>()

    init {
        getLeagues()
    }

    fun getLeagues() {
        viewModelScope.launch {
            val queriedResults = SleeperRepository.getLeaguesAndPlayers()
            leaguesAndPlayers.clear()
            leaguesAndPlayers.addAll(queriedResults.sortedBy { it.league.name })
        }
    }

    fun deleteLeague() {
        isDeleteAlertShowing.value = false
        leagueToDelete.value?.let {
            viewModelScope.launch {
                SleeperRepository.deleteLeague(it)
                getLeagues()
            }
        }
    }

    fun showDeleteAlert(league: League) {
        leagueToDelete.value = league
        isDeleteAlertShowing.value = true
    }

    fun dismissDeleteAlert() {
        leagueToDelete.value = null
        isDeleteAlertShowing.value = false
    }
}

enum class Position(val title: String, val backgroundColor: Color) {
    QB(
        title = "QB",
        backgroundColor = QBRed
    ),
    RB(
        title = "RB",
        backgroundColor = RBGreen
    ),
    WR(
        title = "WR",
        backgroundColor = WRBlue
    ),
    TE(
        title = "TE",
        backgroundColor = TEYellow
    ),
    K(
        "K",
        backgroundColor = KPurple
    ),
    DST(
        title = "DST",
        backgroundColor = DSTBrown
    );

    fun isFLEX(): Boolean {
        return this == RB || this == WR || this == TE
    }

    fun isSuperFLEX(): Boolean {
        return this == QB || isFLEX()
    }

    companion object {
        fun getFromString(value: String): Position {
            return when (value) {
                "QB" -> QB
                "RB" -> RB
                "WR" -> WR
                "TE" -> TE
                "K" -> K
                "DEF" -> DST
                else -> throw Error("$value does not map to a position in Position enum")
            }
        }
    }
}