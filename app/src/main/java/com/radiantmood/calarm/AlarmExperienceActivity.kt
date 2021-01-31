package com.radiantmood.calarm

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.radiantmood.calarm.screen.AlarmExperienceScreen
import com.radiantmood.calarm.util.render


class AlarmExperienceActivity : AppCompatActivity() {

    var ringtone: Ringtone? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getBooleanExtra("isAlarm", false)) {
            Log.d(com.radiantmood.calarm.util.TAG, "onCreate: alarm!")
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
        lifecycleScope.launchWhenResumed {
            startExperience()
            val title = intent.getStringExtra("title") ?: "Alarm"
            render {
                AlarmExperienceScreen(title, this@AlarmExperienceActivity::stopExperience)
            }
        }
    }

    private fun startExperience() {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(applicationContext, notification).also {
            it.play()
        }
    }

    private fun stopExperience() {
        ringtone?.stop()
        finish()
    }
}