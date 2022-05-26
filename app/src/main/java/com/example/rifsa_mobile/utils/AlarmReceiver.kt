package com.example.rifsa_mobile.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.rifsa_mobile.R
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

//todo 1.6 implemet alarm in disease
class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(type_alarm)
        val message = intent.getStringExtra(extra_message)
        val id = intent.getIntExtra(extra_id,0)
    }

    fun setRepeatReminder(
        context: Context,
        type: String,
        time : String,
        messege : String,
        id : Int
    ){
        if (isFormatInvalid(time, time_format)) return

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent =
            Intent(context,AlarmReceiver::class.java)

        intent.putExtra(extra_message,messege)
        intent.putExtra(extra_type,type)
        intent.putExtra(extra_id,id)

        Log.d("alarm start from : ",time)

        val timeArray =
            time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE,Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND,0)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            10000, //todo alarm set to 10 s for testing
            pendingIntent
        )

        Toast.makeText(context,"Repeat alarm setup $id",Toast.LENGTH_SHORT).show()
    }

    private fun showAlarmNotification(
        context: Context,
        title : String,
        message : String,
        notifID : Int,
        uuid : String
    ){
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder =
            NotificationCompat.Builder(context,uuid)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle(title)
                .setContentText(message)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    uuid,
                    uuid,
                    NotificationManager.IMPORTANCE_DEFAULT
                )

            channel.enableLights(true)
            builder.setChannelId(uuid)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(notifID,notification)
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