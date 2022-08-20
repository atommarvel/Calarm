package com.radiantmood.calarm.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.lifecycleScope
import com.radiantmood.calarm.common.appendToFile
import com.radiantmood.calarm.compose.render
import com.radiantmood.calarm.screen.alarm.AlarmExperienceScreen
import com.radiantmood.calarm.screen.LoadingUiStateContainer
import com.radiantmood.calarm.screen.alarm.AlarmExperienceViewModel
import timber.log.Timber


class AlarmExperienceActivity : AppCompatActivity() {

    private val vm: AlarmExperienceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.appendToFile("AlarmExperienceActivity.onCreate")
        showNoMatterWhat()
        lifecycleScope.launchWhenResumed {
            vm.startExperience(intent)
            renderUi()
            subscribeToEvents()
        }
    }

    private fun renderUi() = render {
        val uiState by vm.uiState.observeAsState(LoadingUiStateContainer())
        AlarmExperienceScreen(uiState)
    }

    private fun subscribeToEvents() {
        vm.stopExperienceEvent.observe(this@AlarmExperienceActivity) {
            if (it == true) {
                vm.consumeStopExperienceEvent()
                // TODO: Find better alternative for closing the activity
                finish()
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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}