package com.example.loadsheddingapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Data Transfer Object for current national load-shedding status response.
data class LoadSheddingResponseDto(
    @SerializedName("currentStage") val currentStage: Int,
    @SerializedName("status") val status: String? = null,
    @SerializedName("lastUpdated") val lastUpdated: Long = System.currentTimeMillis(),
    @SerializedName("message") val message: String? = null
)
