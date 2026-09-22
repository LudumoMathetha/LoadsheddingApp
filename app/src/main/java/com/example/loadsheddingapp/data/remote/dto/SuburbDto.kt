package com.example.loadsheddingapp.data.remote.dto

import com.example.loadsheddingapp.data.local.entity.SuburbEntity
import com.example.loadsheddingapp.domain.model.Suburb
import com.google.gson.annotations.SerializedName

// Data Transfer Object (DTO) for suburb JSON payloads received from the REST API.
data class SuburbDto(
    @SerializedName("suburbId") val suburbId: String,
    @SerializedName("name") val name: String,
    @SerializedName("municipality") val municipality: String,
    @SerializedName("province") val province: String,
    @SerializedName("currentStage") val currentStage: Int,
    @SerializedName("lastUpdated") val lastUpdated: Long? = null,
    @SerializedName("customLabel") val customLabel: String? = null
)

// Mapping functions from API DTO to local Room Entity and Domain Models
fun SuburbDto.toEntity(isFavorite: Boolean = false): SuburbEntity {
    return SuburbEntity(
        suburbId = suburbId,
        name = name,
        municipality = municipality,
        province = province,
        currentStage = currentStage,
        lastUpdated = lastUpdated ?: System.currentTimeMillis(),
        customLabel = customLabel,
        isFavorite = isFavorite
    )
}

fun SuburbDto.toDomainModel(isFavorite: Boolean = false): Suburb {
    return Suburb(
        suburbId = suburbId,
        name = name,
        municipality = municipality,
        province = province,
        currentStage = currentStage,
        lastUpdated = lastUpdated ?: System.currentTimeMillis(),
        customLabel = customLabel,
        isFavorite = isFavorite
    )
}
