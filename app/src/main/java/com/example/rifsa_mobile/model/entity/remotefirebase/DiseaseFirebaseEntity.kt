package com.example.rifsa_mobile.model.entity.remotefirebase

data class DiseaseFirebaseEntity(
    val Name : String = "",
    val Bioname : String = "",
    val Cause : String = "",
    val Causedetail : String = "",
    val Indetfy : String = "",
)

data class DiseaseTreatmentEntity(
    val detail : String = ""
)