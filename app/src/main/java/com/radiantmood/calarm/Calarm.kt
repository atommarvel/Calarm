package com.radiantmood.calarm

import android.app.Application
import com.radiantmood.calarm.common.appendToFile
import com.radiantmood.calarm.log.AppendFileTree
import timber.log.Timber
import timber.log.Timber.*


lateinit var calarm: Calarm
    private set

class Calarm : Application() {

    override fun onCreate() {
        super.onCreate()
        calarm = this
        if (BuildConfig.DEBUG) {
            Timber.plant(
                DebugTree(),
                AppendFileTree()
            )
        }
        Timber.appendToFile("Calarm.onCreate")
    }
}