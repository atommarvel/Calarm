package com.radiantmood.calarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            render {
                App(this@MainActivity)
            }
        }
    }

    private fun fetchAndRenderCalendars() = lifecycleScope.launch {
        val calRepo = CalendarRepository()
        val calendars = withContext(Dispatchers.Default) { calRepo.queryCalendars() }
    }

}