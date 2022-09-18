package com.example.rifsa_mobile.helpers.utils

import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseDetailFirebaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseFirebaseEntity

object DataMapper {
    fun FirebaseDetailToFirebaseEntity(value : DiseaseDetailFirebaseEntity):DiseaseFirebaseEntity {
        return DiseaseFirebaseEntity(
            "",
            value.Name,
            "",
            "",
            "",
            "",
            value.imageUrl,
        )
    }
}