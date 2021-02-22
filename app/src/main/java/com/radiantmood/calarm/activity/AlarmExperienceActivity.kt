package com.radiantmood.calarm.activity

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.lifecycleScope
import com.radiantmood.calarm.AlarmExperienceViewModel
import com.radiantmood.calarm.compose.render
import com.radiantmood.calarm.screen.AlarmExperienceScreen


class AlarmExperienceActivity : AppCompatActivity() {

    private val vm: AlarmExperienceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showNoMatterWhat()
        lifecycleScope.launchWhenResumed {
            vm.startExperience(intent)
            render {
                val title by vm.title.observeAsState("Alarm")
                AlarmExperienceScreen(title) {
                    vm.stopExperience()
                    finish()
                }
            }
        }
    }

    private fun showNoMatterWhat() {
        var windowFlags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true)
            setShowWhenLocked(true)
        } else {
            windowFlags = windowFlags or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        }
        window.addFlags(windowFlags)
    }
}