package com.example.rifsa_mobile.helpers.update

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.rifsa_mobile.injection.Injection
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import java.util.*
import java.util.concurrent.Executors

class FinanceUploadWorker : BroadcastReceiver() {
    override fun onReceive(context: Context, intent : Intent) {
        Executors.newSingleThreadExecutor().execute {
            val dataNotUploaded = Injection.provideFinancialRepository(
                context
            ).readNotUploaded()

            dataNotUploaded.forEach { harvest ->
                uploadDataRemote(context,harvest)
            }
            Log.d("Fianncialworker","e.toString()")
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
            FinanceUploadWorker::class.java
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
            4,
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
        data : FinancialEntity
    ){
        val firebase = Injection.provideFirebaseRepsitory()
        firebase.insertUpdateFinancial(data,data.firebaseUserId)
            .addOnSuccessListener {
                updateFinancialLocal(context,data.idFinance)
            }
            .addOnFailureListener { e->
                Log.d("HarvestUploadWorker",e.toString())
            }
    }

    private fun updateFinancialLocal(
        context: Context,
        harvestId : String
    ){
        val local = Injection.provideFinancialRepository(context)
        local.updateUploadStatus(harvestId)
    }
}