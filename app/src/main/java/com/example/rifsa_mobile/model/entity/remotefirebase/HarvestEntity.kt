package com.example.rifsa_mobile.model.entity.remotefirebase

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName="HarvestTable")
class HarvestEntity(
    @PrimaryKey(autoGenerate = true)
    var localId : Int = 0,
    var id : String="",
    var firebaseUserId : String = "",
    var date : String="",
    val day : Int = 0,
    val month : Int = 0,
    val year : Int = 0,
    var typeOfGrain : String="",
    var weight : Int = 0,
    var income : Int = 0,
    var note : String="",
    var isUploaded : Boolean = true,
    var uploadReminderId : Int = 0
): Parcelable