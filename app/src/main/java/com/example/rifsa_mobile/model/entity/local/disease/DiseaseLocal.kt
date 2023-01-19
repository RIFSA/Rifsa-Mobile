package com.example.rifsa_mobile.model.entity.local.disease

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "DiseaseTable")
data class DiseaseLocal(
    @PrimaryKey(autoGenerate = true)
    var id_disease : Int,
    val key_disease : String,
    var name : String,
    var indication : String,
    var photoUrl : String,
    var date : String,
    var latitude : Double,
    var longitude : Double,
    var description : String,
    var reminderID : Int,
    var isUploaded : Boolean
):Parcelable
