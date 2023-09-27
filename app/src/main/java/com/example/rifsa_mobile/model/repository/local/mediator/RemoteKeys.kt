package com.example.rifsa_mobile.model.repository.local.mediator

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remoteKeys")
data class RemoteKeys(
    @PrimaryKey
    val id: String,
    val prevKey : Int?,
    val nextKet : Int?
)
