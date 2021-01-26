package com.radiantmood.calarm

import android.app.Application

lateinit var calarm: Calarm
    private set

class Calarm : Application() {

    override fun onCreate() {
        super.onCreate()
        calarm = this
    }
}