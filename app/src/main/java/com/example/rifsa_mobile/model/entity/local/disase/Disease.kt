package com.example.rifsa_mobile.model.entity.local.disase

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Disease_Table")
data class Disease(
    @PrimaryKey(autoGenerate = true)
    val id_sort : Int,

    val id_disease : String,

    var indication : String,
    var photoUrl : String,
    var latitude : Double,
    var longitude : Double,

    var reminderID : Int,
    var isUploaded : Boolean
): Parcelable
