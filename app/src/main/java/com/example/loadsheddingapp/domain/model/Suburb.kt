package com.example.loadsheddingapp.domain.model

// Domain model representing a suburb location for load-shedding tracking.
data class Suburb(
    val suburbId: String,
    val name: String,
    val municipality: String,
    val province: String,
    val currentStage: Int,
    val lastUpdated: Long,
    val customLabel: String? = null,
    val isFavorite: Boolean = false
)
