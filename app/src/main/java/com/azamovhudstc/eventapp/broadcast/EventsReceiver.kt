package com.azamovhudstc.eventapp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.SoundPool
import com.azamovhudstc.eventapp.R
import com.azamovhudstc.eventapp.data.local.dao.EventDao
import com.azamovhudstc.eventapp.data.local.shared.LocalData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class EventsReceiver : BroadcastReceiver() {

    private lateinit var mediaPlayer: MediaPlayer
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Inject
    lateinit var eventDao: EventDao

    override fun onReceive(context: Context?, intent: Intent?) {
        scope.launch {
            val events = eventDao.getAllEvents()
            when (intent?.action) {
                Intent.ACTION_SCREEN_ON -> {
                    if (events[0].actionState) {
                        Timber.d("action: ACTION_SCREEN_ON")
                        mediaPlayer = when (LocalData.getAudioSetting()){
                            0-> MediaPlayer.create(context, R.raw.screen_on_anna)
                            1 -> MediaPlayer.create(context, R.raw.screen_on_jack)
                            else -> MediaPlayer.create(context, R.raw.screen_on_robot)
                        }
                        mediaPlayer.start()
                    }
                }
                Intent.ACTION_SCREEN_OFF -> {
                    if (events[1].actionState) {
                        Timber.d("action: ACTION_SCREEN_OFF")
                        mediaPlayer = when (LocalData.getAudioSetting()){
                            0-> MediaPlayer.create(context, R.raw.screen_of_anna)
                            1 -> MediaPlayer.create(context, R.raw.screen_of_jack)
                            else -> MediaPlayer.create(context, R.raw.screen_off_roboto)
                        }
                        mediaPlayer.start()
                    }
                }
                Intent.ACTION_POWER_CONNECTED -> {
                    if (events[2].actionState) {
                        Timber.d("action: ACTION_POWER_CONNECTED")
                        mediaPlayer = when (LocalData.getAudioSetting()){
                            0-> MediaPlayer.create(context, R.raw.power_connected_anna)
                            1 -> MediaPlayer.create(context, R.raw.power_connected_jack)
                            else -> MediaPlayer.create(context, R.raw.power_connected_robot)
                        }
                        mediaPlayer.start()
                    }
                }
                Intent.ACTION_POWER_DISCONNECTED -> {
                    if (events[3].actionState) {
                        Timber.d("action: ACTION_POWER_DISCONNECTED")
                        mediaPlayer = when (LocalData.getAudioSetting()){
                            0-> MediaPlayer.create(context, R.raw.power_disconnected_anna)
                            1 -> MediaPlayer.create(context, R.raw.power_disconnected_jack)
                            else -> MediaPlayer.create(context, R.raw.power_disconnected_robot)
                        }
                        mediaPlayer.start()
                    }
                }
                Intent.ACTION_BATTERY_OKAY -> {
                    if (events[4].actionState) {
                        Timber.d("action: ACTION_BATTERY_OKAY")
                        mediaPlayer = when (LocalData.getAudioSetting()){
                            0-> MediaPlayer.create(context, R.raw.battery_ok)
                            1 -> MediaPlayer.create(context, R.raw.battery_ok)
                            else -> MediaPlayer.create(context, R.raw.battery_ok)
                        }
                        mediaPlayer.start()
                    }
                }
                Intent.ACTION_BATTERY_LOW -> {
                    if (events[5].actionState) {
                        Timber.d("action: ACTION_BATTERY_OKAY")
                        mediaPlayer = when (LocalData.getAudioSetting()){
                            0-> MediaPlayer.create(context, R.raw.battery_low)
                            1 -> MediaPlayer.create(context, R.raw.battery_low)
                            else -> MediaPlayer.create(context, R.raw.battery_low)
                        }
                        mediaPlayer.start()
                    }
                }
                Intent.ACTION_AIRPLANE_MODE_CHANGED->{
                    if (events[6].actionState) {
                        Timber.d("action: ACTION_AIRPLANE_MODE_CHANGED")
                        mediaPlayer =when (LocalData.getAudioSetting()){
                            0-> MediaPlayer.create(context, R.raw.airplane_mode_changed_anna)
                            1 -> MediaPlayer.create(context, R.raw.airplane_mode_changed_jack)
                            else -> MediaPlayer.create(context, R.raw.airplane_mode_changed_robot)
                        }
                        mediaPlayer.start()
                    }
                }
            }
        }
    }

    fun clearReceiver() {
        scope.cancel()
        clearAbortBroadcast()
    }
}

/*val audioAttributes = AudioAttributes
        .Builder()
        .setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()
    soundPool = SoundPool
    .Builder()
    .setMaxStreams(1)
    .setAudioAttributes(audioAttributes)
    .build()
    soundPowerDisconnected = soundPool.load(context, R.raw.wrong, 1)
    soundPool.play(soundPowerDisconnected, 1f, 1f, 1, 0, 1f)*/