package com.example.rifsa_mobile.model.entity.remotefirebase

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "DiseaseTable")
data class DiseaseEntity(
    @PrimaryKey(autoGenerate = true)
    var id_local : Int = 0,
    var idDisease: String = "",
    var indexDisease : Int = 0,
    var nameDisease : String = "",
    var dateDisease : String = "",
    var latitude : String = "",
    var longitude : String = "",
    var imageUrl : String = "",
    var reminderID : Int = 0,
    var isUploaded : Boolean = false
): Parcelable