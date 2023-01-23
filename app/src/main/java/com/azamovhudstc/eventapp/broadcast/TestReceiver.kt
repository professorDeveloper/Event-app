package com.azamovhudstc.eventapp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class TestReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("onReceive: ${intent?.action}")
        when(intent?.action){
            Intent.ACTION_SCREEN_ON->{
                Timber.d("onReceive: ACTION_SCREEN_ON")
            }
            Intent.ACTION_SCREEN_OFF->{
                Timber.d("onReceive: ACTION_SCREEN_OFF")
            }
        }
    }
}