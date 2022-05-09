package com.amped94.ffbtracker.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.amped94.ffbtracker.data.repository.SleeperRepository

class PlayersAndLeaguesWorker(appContext: Context,
                              params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        SleeperRepository.refreshPlayersAndLeagues()
        return Result.success()
    }
}