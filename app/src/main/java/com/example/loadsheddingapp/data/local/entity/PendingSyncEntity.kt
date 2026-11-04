package com.example.loadsheddingapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// PendingSyncEntity represents offline actions queued in Room Database when device has no internet connection.
// Once network connectivity returns, queued sync tasks are automatically transmitted to the REST API.
@Entity(tableName = "pending_syncs")
data class PendingSyncEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val actionType: String, // e.g. "ADD_SUBURB", "DELETE_SUBURB", "TOGGLE_FAVORITE"
    val suburbId: String,
    val payloadJson: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
