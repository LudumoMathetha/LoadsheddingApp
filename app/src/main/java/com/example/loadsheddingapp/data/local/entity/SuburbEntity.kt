package com.example.loadsheddingapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.loadsheddingapp.domain.model.Suburb

// SuburbEntity represents a table row in the "suburbs" SQLite database table managed by Room.
// Keeping entity classes in data layer separates local database structure from UI domain models.
@Entity(tableName = "suburbs")
data class SuburbEntity(
    @PrimaryKey
    val suburbId: String,
    val name: String,
    val municipality: String,
    val province: String,
    val currentStage: Int,
    val lastUpdated: Long,
    val customLabel: String? = null,
    val isFavorite: Boolean = false
)

// Maps a Room entity into a clean domain model used by ViewModels and Compose screens.
fun SuburbEntity.toDomainModel(): Suburb {
    return Suburb(
        suburbId = suburbId,
        name = name,
        municipality = municipality,
        province = province,
        currentStage = currentStage,
        lastUpdated = lastUpdated,
        customLabel = customLabel,
        isFavorite = isFavorite
    )
}

// Maps a domain model into a Room entity for database storage.
fun Suburb.toEntity(): SuburbEntity {
    return SuburbEntity(
        suburbId = suburbId,
        name = name,
        municipality = municipality,
        province = province,
        currentStage = currentStage,
        lastUpdated = lastUpdated,
        customLabel = customLabel,
        isFavorite = isFavorite
    )
}
