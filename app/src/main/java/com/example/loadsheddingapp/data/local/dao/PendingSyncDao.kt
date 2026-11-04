package com.example.loadsheddingapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.loadsheddingapp.data.local.entity.PendingSyncEntity
import kotlinx.coroutines.flow.Flow

// PendingSyncDao defines Room database operations for queueing and processing offline sync tasks.
@Dao
interface PendingSyncDao {

    // Queue an offline pending action into Room database.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendingSync(sync: PendingSyncEntity): Long

    // Retrieve all queued offline sync actions in creation order.
    @Query("SELECT * FROM pending_syncs ORDER BY createdAt ASC")
    suspend fun getPendingSyncs(): List<PendingSyncEntity>

    // Observable Flow counting how many offline actions are pending synchronization.
    @Query("SELECT COUNT(*) FROM pending_syncs")
    fun getPendingSyncCountFlow(): Flow<Int>

    // Delete a processed sync action after successful server sync.
    @Query("DELETE FROM pending_syncs WHERE id = :id")
    suspend fun deletePendingSync(id: Int): Int

    // Clear all pending sync actions.
    @Query("DELETE FROM pending_syncs")
    suspend fun clearPendingSyncs(): Int
}
