package com.example.rifsa_mobile.helpers.update

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.injection.Injection
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import java.util.*
import java.util.concurrent.Executors

class DiseaseUploadWorker: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //todo uploadchecker by live data
        //todo no itnernet connection view
        Executors.newSingleThreadExecutor().execute {
            val randomNotificationId = UUID.randomUUID().toString()
            val uploadId = intent.getIntExtra("uploadId",0)
            val dataNotUploaded = Injection.provideDiseaseRepository(
                context
            ).getNotUploaded()

            dataNotUploaded.forEach { data->
                uploadData(context,data)
                Log.d("test", data.diseaseId)
            }
            showUploadNotification(
                context,
                "upload data",
                uploadId,
                randomNotificationId
            )
        }
    }

    fun setDailyUpload(
        context: Context,
        uploadId : Int
    ){
        val workManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context,DiseaseUploadWorker::class.java)
        intent.putExtra("uploadId",uploadId)

        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY,5)
            set(Calendar.MINUTE,0)
            set(Calendar.SECOND,0)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            uploadId,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        workManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            10000, //2 hours 1800000
            pendingIntent
        )
    }

    private fun uploadData(
        context: Context,
        data : DiseaseEntity
    ){
        val storage = Injection.provideFirebaseRepsitory()
        storage.uploadDiseaseImage(
            idDisease = data.diseaseId,
            fileUri = data.imageUrl.toUri(),
            userId = data.firebaseUserId
        ) .addOnSuccessListener {
            it.storage.downloadUrl
                .addOnSuccessListener { imgUrl ->
                    updateDiseaseLocal(context,imgUrl,data)
                }
                .addOnFailureListener { e ->
                    Log.d("settingFragment",e.message.toString())
                }
        }
            .addOnFailureListener { e ->
                Log.d("settingFragment",e.message.toString())
            }
    }

    private fun updateDiseaseLocal(
        context: Context,
        url : Uri,
        data : DiseaseEntity
    ){
        val storage = Injection.provideDiseaseRepository(context)
        storage.updateDiseaseUpload(url,data.diseaseId)
        Thread.sleep(5000)
        uploadDiseaseData(context,data)
    }

    private fun uploadDiseaseData(
        context: Context,
        data : DiseaseEntity
    ){
        try {
            Injection.provideFirebaseRepsitory()
                .saveDisease(data,data.firebaseUserId)
                .addOnSuccessListener {
                    Log.d("settingFragment","succes upload")
                    cancelWorker(context,data.uploadReminderId)
                }
                .addOnFailureListener {
                    Log.d("settingFragment",it.message.toString())
                }
        }catch (e : Exception){
            Log.d("settingFragment",e.message.toString())
        }
    }

    private fun showUploadNotification(
        context: Context,
        title : String,
        dataId : Int,
        notificationId : String
    ){
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder =
            NotificationCompat.Builder(context,notificationId)
                .setSmallIcon(R.drawable.ic_back)
                .setContentTitle(title)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    notificationId,
                    notificationId,
                    NotificationManager.IMPORTANCE_DEFAULT
                )

            channel.enableLights(true)
            builder.setChannelId(notificationId)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(dataId,notification)
    }

    fun cancelWorker(
        context: Context,
        workerId : Int
    ){
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent =
            Intent(context,DiseaseUploadWorker::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            workerId,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }
}