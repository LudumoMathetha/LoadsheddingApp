package com.example.loadsheddingapp.domain.model

// Domain model representing a load-shedding schedule slot.
data class Schedule(
    val id: Int = 0,
    val suburbId: String,
    val startTime: String,
    val endTime: String,
    val stage: Int,
    val date: String
)
