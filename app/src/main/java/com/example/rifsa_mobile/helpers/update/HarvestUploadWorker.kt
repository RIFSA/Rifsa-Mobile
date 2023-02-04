package com.example.rifsa_mobile.helpers.update

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.rifsa_mobile.injection.Injection
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import java.util.*
import java.util.concurrent.Executors

class HarvestUploadWorker : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      Executors.newSingleThreadExecutor().execute {
          val dataNotUploaded = Injection.provideHarvestRepository(
              context
          ).readNotUploaded()

          dataNotUploaded.forEach { harvest ->
              uploadDataRemote(context,harvest)
          }
      }
    }

    fun setDailyUpload(
        context: Context,
        uploadId : Int
    ){
        val workManager = context.getSystemService(
            Context.ALARM_SERVICE
        ) as AlarmManager

        val intent = Intent(
            context,
            HarvestUploadWorker::class.java
        )
        intent.putExtra("uploadId",uploadId)

        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY,5)
            set(Calendar.MINUTE,0)
            set(Calendar.SECOND,0)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            3,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        workManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000, //2 hours 1800000
            pendingIntent
        )

    }

    private fun uploadDataRemote(
        context: Context,
        data : HarvestEntity
    ){
        val firebase = Injection.provideFirebaseRepsitory()
        firebase.insertUpdateHarvestResult(data,data.firebaseUserId)
            .addOnSuccessListener {
                updateHarvestLocal(context,data.id)
            }
            .addOnFailureListener { e->
                Log.d("HarvestUploadWorker",e.toString())
            }
    }

    private fun updateHarvestLocal(
        context: Context,
        harvestId : String
    ){
        val local = Injection.provideHarvestRepository(context)
        local.updateUploadStatus(harvestId)
    }


}