package com.amped94.ffbtracker

import android.app.Application
import android.content.Context

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MainApplication

        fun getContext(): Context {
            return instance.applicationContext
        }
    }

}