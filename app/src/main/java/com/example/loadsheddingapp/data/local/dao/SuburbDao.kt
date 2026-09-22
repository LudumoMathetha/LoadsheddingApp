package com.example.loadsheddingapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.loadsheddingapp.data.local.entity.SuburbEntity
import kotlinx.coroutines.flow.Flow

// SuburbDao defines database operations for saved suburbs in Room.
// Flow is used for read queries so UI screens receive automatic updates whenever suburb data changes.
// Suspend functions are used for write operations to ensure database calls run off the main thread.
@Dao
interface SuburbDao {

    // Insert or replace a suburb record when saved by the user.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuburb(suburb: SuburbEntity): Long

    // Update existing suburb details in local database.
    @Update
    suspend fun updateSuburb(suburb: SuburbEntity): Int

    // Delete a saved suburb record.
    @Delete
    suspend fun deleteSuburb(suburb: SuburbEntity): Int

    // Delete a suburb by its unique ID string.
    @Query("DELETE FROM suburbs WHERE suburbId = :suburbId")
    suspend fun deleteSuburbById(suburbId: String): Int

    // Query all saved suburbs ordered by favorite status first, then by name.
    @Query("SELECT * FROM suburbs ORDER BY isFavorite DESC, name ASC")
    fun getAllSuburbs(): Flow<List<SuburbEntity>>

    // Query a specific suburb by ID as an observable Flow.
    @Query("SELECT * FROM suburbs WHERE suburbId = :suburbId")
    fun getSuburbById(suburbId: String): Flow<SuburbEntity?>

    // Direct suspend query for single suburb retrieval without Flow observation.
    @Query("SELECT * FROM suburbs WHERE suburbId = :suburbId")
    suspend fun getSuburbByIdDirect(suburbId: String): SuburbEntity?

    // Filter saved suburbs matching search term.
    @Query("SELECT * FROM suburbs WHERE name LIKE '%' || :query || '%' OR municipality LIKE '%' || :query || '%'")
    fun searchSavedSuburbs(query: String): Flow<List<SuburbEntity>>

    // Check whether a suburb ID already exists in local database to prevent duplicate entries.
    @Query("SELECT EXISTS(SELECT 1 FROM suburbs WHERE suburbId = :suburbId)")
    suspend fun exists(suburbId: String): Boolean

    // Toggle favorite flag on a saved suburb.
    @Query("UPDATE suburbs SET isFavorite = :isFavorite WHERE suburbId = :suburbId")
    suspend fun updateFavoriteStatus(suburbId: String, isFavorite: Boolean): Int
}
