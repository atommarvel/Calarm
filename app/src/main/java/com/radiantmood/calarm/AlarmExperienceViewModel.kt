package com.radiantmood.calarm

import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radiantmood.calarm.repo.AlarmRepository
import com.radiantmood.calarm.util.AlarmUtil.AlarmIntentData
import kotlinx.coroutines.launch

// TODO: try/catch everything this does
class AlarmExperienceViewModel : ViewModel() {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val alarmRepo = AlarmRepository()

    private var ringtone: Ringtone? = null

    fun startExperience(intent: Intent) {
        val alarmIntentData = AlarmIntentData.fromIntent(intent)
        _title.value = alarmIntentData?.title ?: "Alarm"
        playSound()
        deleteAlarm(alarmIntentData)
    }

    fun stopExperience() = ringtone?.stop()

    private fun playSound() {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(calarm, notification).also {
            it.play()
        }
    }

    private fun deleteAlarm(alarmIntentData: AlarmIntentData?) = viewModelScope.launch {
        try {
            alarmIntentData?.eventId?.let { alarmRepo.remove(it) }
        } catch (e: Exception) {
            // Do nothing. We will reconcile it later if needed.
        }
    }
}