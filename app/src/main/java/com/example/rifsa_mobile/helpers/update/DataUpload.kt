package com.example.rifsa_mobile.helpers.update

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.rifsa_mobile.injection.Injection
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executors

class DataUpload: BroadcastReceiver() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent) {
//        val localData = Injection.provideDiseaseRepository(
//            context
//        ).getDiseaseNotUploaded()
//        GlobalScope.launch(Dispatchers.IO) {
//            localData.observe(this.coroutineContext.view) { data ->
//                data.forEach { uploadData(context,it)}
//            }
//        }
        val id = intent.getIntExtra("uploadId",0)
        Log.d("uploadId",id.toString())
    }

    fun setDailyUpload(
        context: Context,
        uploadId : Int
    ){
        val workManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context,DataUpload::class.java)
        intent.putExtra("uploadId",uploadId)

        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY,5)
            set(Calendar.MINUTE,0)
            set(Calendar.SECOND,0)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            100,
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
        uploadDiseaseData(data)
    }

    private fun uploadDiseaseData(data : DiseaseEntity){
        try {
            Injection.provideFirebaseRepsitory()
                .saveDisease(data,data.firebaseUserId)
                .addOnSuccessListener {
                    Log.d("settingFragment","succes upload")
                }
                .addOnFailureListener {
                    Log.d("settingFragment",it.message.toString())
                }
        }catch (e : Exception){
            Log.d("settingFragment",e.message.toString())
        }
    }

}