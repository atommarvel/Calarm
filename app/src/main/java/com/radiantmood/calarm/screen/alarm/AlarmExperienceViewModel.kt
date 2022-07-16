package com.radiantmood.calarm.screen.alarm

import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radiantmood.calarm.calarm
import com.radiantmood.calarm.compose.ButtonUiState
import com.radiantmood.calarm.repo.AlarmRepository
import com.radiantmood.calarm.repo.UserAlarm
import com.radiantmood.calarm.screen.LoadingUiStateContainer
import com.radiantmood.calarm.screen.UiStateContainer
import com.radiantmood.calarm.common.AlarmUtil
import com.radiantmood.calarm.common.AlarmUtil.AlarmIntentData
import com.radiantmood.calarm.common.bind
import com.radiantmood.calarm.common.getFutureCalendar
import kotlinx.coroutines.launch
import kotlin.math.abs

// TODO: try/catch everything this does
class AlarmExperienceViewModel : ViewModel() {

    private var _uiState = MutableLiveData<UiStateContainer<AlarmExperienceScreenUiState>>(LoadingUiStateContainer())
    val uiState: LiveData<UiStateContainer<AlarmExperienceScreenUiState>> = _uiState

    private var _stopExperienceEvent = MutableLiveData<Boolean?>()
    val stopExperienceEvent: LiveData<Boolean?> = _stopExperienceEvent

    private val alarmRepo = AlarmRepository()
    private val alarmUtil = AlarmUtil()

    private var ringtone: Ringtone? = null

    fun startExperience(intent: Intent) {
        val alarmIntentData = AlarmIntentData.fromIntent(intent)
        if (alarmIntentData != null) {
            val scheduleAndStopUiState = if (alarmIntentData.offsetMinutes < 0) {
                ButtonUiState(
                    text = "Stop and Schedule",
                    label = "Set a ${abs(alarmIntentData.offsetMinutes)} minute alarm for event start",
                    action = ::stopAndSchedule.bind(alarmIntentData)
                )
            } else null
            val screenUiState = AlarmExperienceScreenUiState(
                stopUiState = ButtonUiState(
                    text = "Stop",
                    label = alarmIntentData.title,
                    action = ::stopExperience
                ),
                scheduleAndStopUiState = scheduleAndStopUiState
            )
            _uiState.value = screenUiState
        }
        playSound()
        deleteAlarm(alarmIntentData)
    }

    private fun stopExperience() {
        ringtone?.stop()
        _stopExperienceEvent.value = true
    }

    fun consumeStopExperienceEvent() {
        _stopExperienceEvent.value = null
    }

    fun stopAndSchedule(alarmIntentData: AlarmIntentData) {
        stopExperience()
        val nextCalendar = getFutureCalendar(alarmIntentData.calendar, minutesInFuture = abs(alarmIntentData.offsetMinutes.toLong()))
        val alarm = with(alarmIntentData) {
            UserAlarm(eventPart + eventId, eventId, nextCalendar, title, eventPart, 0)
        }
        viewModelScope.launch {
            alarmRepo.add(alarm)
            alarmUtil.scheduleAlarm(alarm)
        }
    }

    private fun playSound() {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(calarm, notification).also {
            it.play()
        }
    }

    private fun deleteAlarm(alarmIntentData: AlarmIntentData?) = viewModelScope.launch {
        try {
            // TODO: wrap alarm, alarmRepo usage in some use cases
            alarmIntentData?.let { alarmRepo.remove(it.eventPart + it.eventId) }
        } catch (e: Exception) {
            // Do nothing. We will reconcile it later if needed.
        }
    }
}