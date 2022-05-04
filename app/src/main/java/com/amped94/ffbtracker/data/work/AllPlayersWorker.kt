package com.amped94.ffbtracker.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.Dispatchers

class AllPlayersWorker(appContext: Context, workerParameters: WorkerParameters) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        SleeperRepository.refreshAllPlayers()
        return Result.success()
    }
}