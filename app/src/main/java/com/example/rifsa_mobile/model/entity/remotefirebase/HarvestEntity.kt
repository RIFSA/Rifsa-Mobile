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
    var typeOfGrain : String="",
    var weight : String="",
    var income : String="",
    var note : String="",
    var isUploaded : Boolean = true,
    var uploadReminderId : Int = 0
): Parcelable