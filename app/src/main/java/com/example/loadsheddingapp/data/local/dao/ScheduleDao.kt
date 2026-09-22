package com.example.loadsheddingapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.loadsheddingapp.data.local.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

// ScheduleDao handles Room database queries for load-shedding schedule slots.
// Using Flow allows screens to update dynamically as new schedules are synced from the API.
@Dao
interface ScheduleDao {

    // Insert a single load-shedding schedule slot into local cache.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleEntity): Long

    // Insert a list of schedule slots received from API or mock data source.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<ScheduleEntity>): List<Long>

    // Retrieve all schedules for a given suburb ordered by date and start time.
    @Query("SELECT * FROM schedules WHERE suburbId = :suburbId ORDER BY date ASC, startTime ASC")
    fun getSchedulesForSuburb(suburbId: String): Flow<List<ScheduleEntity>>

    // Direct non-flow query to fetch cached schedules for offline fallback logic.
    @Query("SELECT * FROM schedules WHERE suburbId = :suburbId ORDER BY date ASC, startTime ASC")
    suspend fun getSchedulesForSuburbDirect(suburbId: String): List<ScheduleEntity>

    // Query schedules for a suburb filtered by a specific date.
    @Query("SELECT * FROM schedules WHERE suburbId = :suburbId AND date = :date ORDER BY startTime ASC")
    fun getSchedulesForSuburbAndDate(suburbId: String, date: String): Flow<List<ScheduleEntity>>

    // Remove all cached schedules associated with a specific suburb ID.
    @Query("DELETE FROM schedules WHERE suburbId = :suburbId")
    suspend fun deleteSchedulesForSuburb(suburbId: String): Int

    // Delete outdated schedules prior to current date to maintain database cleanliness.
    @Query("DELETE FROM schedules WHERE date < :currentDate")
    suspend fun clearOutdatedSchedules(currentDate: String): Int
}
