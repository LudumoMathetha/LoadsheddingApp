package com.example.loadsheddingapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.loadsheddingapp.domain.model.Schedule

// ScheduleEntity defines the "schedules" table in Room.
// Foreign key relationship binds each schedule slot to a parent suburbId in "suburbs".
// Cascade deletion ensures that removing a saved suburb automatically deletes its cached schedules.
@Entity(
    tableName = "schedules",
    foreignKeys = [
        ForeignKey(
            entity = SuburbEntity::class,
            parentColumns = ["suburbId"],
            childColumns = ["suburbId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["suburbId"]), Index(value = ["suburbId", "date"])]
)
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val suburbId: String,
    val startTime: String,
    val endTime: String,
    val stage: Int,
    val date: String
)

// Maps ScheduleEntity to domain model for UI presentation.
fun ScheduleEntity.toDomainModel(): Schedule {
    return Schedule(
        id = id,
        suburbId = suburbId,
        startTime = startTime,
        endTime = endTime,
        stage = stage,
        date = date
    )
}

// Maps Schedule domain model to Room entity.
fun Schedule.toEntity(): ScheduleEntity {
    return ScheduleEntity(
        id = id,
        suburbId = suburbId,
        startTime = startTime,
        endTime = endTime,
        stage = stage,
        date = date
    )
}
