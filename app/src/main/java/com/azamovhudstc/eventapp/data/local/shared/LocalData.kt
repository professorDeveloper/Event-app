package com.azamovhudstc.eventapp.data.local.shared

import android.content.Context
import android.content.SharedPreferences

class LocalData {
    companion object{
        private lateinit var sharedPref: SharedPreferences
        fun getInstance(context: Context): SharedPreferences {
            if (!::sharedPref.isInitialized) sharedPref =
                context.getSharedPreferences("DATA", Context.MODE_PRIVATE)
            return sharedPref
        }


        //Big
        fun setAudioSetting(type_undo_big:Int) {
            sharedPref.edit().putInt("type_undo_big", type_undo_big).apply()
        }

        fun getAudioSetting():Int {
            return sharedPref.getInt("type_undo_big", 0)
        }
    }
}