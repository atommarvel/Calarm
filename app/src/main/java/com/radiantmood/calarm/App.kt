package com.radiantmood.calarm

import android.app.Application

lateinit var app: App
    private set

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}