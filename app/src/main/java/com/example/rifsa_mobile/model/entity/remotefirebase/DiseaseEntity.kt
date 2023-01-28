package com.example.rifsa_mobile.model.entity.remotefirebase

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "DiseaseTable")
data class DiseaseEntity(
    @PrimaryKey(autoGenerate = true)
    var localId : Int = 0,
    var diseaseId: String = "",
    val firebaseUserId : String = "",
    var indexDisease : Int = 0,
    var reminderID : Int = 0,
    var nameDisease : String = "",
    var dateDisease : String = "",
    var latitude : String = "",
    var longitude : String = "",
    var imageUrl : String = "",
    var isUploaded : Boolean = false,
    var uploadReminderId : Int = 0
): Parcelable