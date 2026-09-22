package com.example.loadsheddingapp.data.remote.dto

import com.example.loadsheddingapp.data.local.entity.ScheduleEntity
import com.example.loadsheddingapp.domain.model.Schedule
import com.google.gson.annotations.SerializedName

// Data Transfer Object (DTO) for load-shedding schedule slots received from the REST API.
data class ScheduleDto(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("suburbId") val suburbId: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("stage") val stage: Int,
    @SerializedName("date") val date: String
)

// Mapping functions from ScheduleDto to Room Entity and Domain models
fun ScheduleDto.toEntity(): ScheduleEntity {
    return ScheduleEntity(
        id = id ?: 0,
        suburbId = suburbId,
        startTime = startTime,
        endTime = endTime,
        stage = stage,
        date = date
    )
}

fun ScheduleDto.toDomainModel(): Schedule {
    return Schedule(
        id = id ?: 0,
        suburbId = suburbId,
        startTime = startTime,
        endTime = endTime,
        stage = stage,
        date = date
    )
}
