package com.amped94.ffbtracker

import android.app.Application
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.amped94.ffbtracker.data.work.AllPlayersWorker
import com.amped94.ffbtracker.data.work.PlayersAndLeaguesWorker
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.ExperimentalTime

class MainApplication: Application() {

    @ExperimentalTime
    override fun onCreate() {
        super.onCreate()
        instance = this

        enqueueGetAllPlayers()
        enqueueGetPlayersAndLeagues()
    }

    private fun enqueueGetAllPlayers() {
        val currentDate = Calendar.getInstance()
        val requestDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 3)
            set(Calendar.MINUTE , 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_YEAR, 1)
        }
        val initialDelay = requestDate.timeInMillis - currentDate.timeInMillis

        val workRequest = PeriodicWorkRequest.Builder(
            AllPlayersWorker::class.java,
            1,
            TimeUnit.DAYS
        ).setInitialDelay(
            initialDelay,
            TimeUnit.MILLISECONDS
        ).build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "GetAllPlayers",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }

    private fun enqueueGetPlayersAndLeagues() {
        val workRequest = PeriodicWorkRequest.Builder(
            PlayersAndLeaguesWorker::class.java,
            1,
            TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "RefreshPlayersAndLeagues",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }

    companion object {
        lateinit var instance: MainApplication

        fun getContext(): Context {
            return instance.applicationContext
        }
    }

}