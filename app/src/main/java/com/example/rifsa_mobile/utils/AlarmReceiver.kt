package com.example.rifsa_mobile.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(type_alarm)
        val message = intent.getStringExtra(extra_message)
        val id = intent.getIntExtra(extra_id,0)
    }

    private fun isFormatInvalid(type : String,format : String): Boolean{
        return try {
            val timeFormat = SimpleDateFormat(format, Locale.getDefault())
            timeFormat.isLenient = false
            timeFormat.parse(type)
            false
        }catch (e : Exception){
            Log.d(log_key,e.message.toString())
            true
        }
    }

    companion object{
        const val log_key = "AlarmReceiver"
        const val time_format = "HH:mm"
        const val type_alarm = "repeat_alarm"

        const val extra_message = "Message"
        const val extra_type = "type"
        const val extra_id = "id"
    }

}