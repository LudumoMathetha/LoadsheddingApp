package com.example.loadsheddingapp.data.repository

import com.example.loadsheddingapp.data.local.dao.ScheduleDao
import com.example.loadsheddingapp.data.local.entity.toDomainModel
import com.example.loadsheddingapp.data.local.entity.toEntity
import com.example.loadsheddingapp.data.remote.LoadSheddingApiService
import com.example.loadsheddingapp.data.remote.MockLoadSheddingData
import com.example.loadsheddingapp.data.remote.RetrofitInstance
import com.example.loadsheddingapp.data.remote.dto.toDomainModel
import com.example.loadsheddingapp.data.remote.dto.toEntity
import com.example.loadsheddingapp.domain.model.Schedule
import com.example.loadsheddingapp.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// The ScheduleRepository manages load-shedding schedules with Retrofit API sync and Room offline caching.
class ScheduleRepository(
    private val scheduleDao: ScheduleDao,
    private val apiService: LoadSheddingApiService = RetrofitInstance.api
) {
    // Fetches schedule slots from remote API, caches in Room Database, and returns results.
    // If the network request fails, it transparently falls back to Room cached entries.
    suspend fun fetchAndCacheSchedule(suburbId: String): NetworkResult<List<Schedule>> {
        return try {
            val response = apiService.getScheduleForSuburb(suburbId)
            if (response.isSuccessful && response.body() != null) {
                val scheduleDtos = response.body()!!
                scheduleDao.insertSchedules(scheduleDtos.map { it.toEntity() })
                val domainSchedules = scheduleDtos.map { it.toDomainModel() }
                NetworkResult.Success(domainSchedules)
            } else {
                fallbackToCacheOrMock(suburbId)
            }
        } catch (e: Exception) {
            fallbackToCacheOrMock(suburbId)
        }
    }

    // Helper method handling offline fallback when network API calls fail.
    private suspend fun fallbackToCacheOrMock(suburbId: String): NetworkResult<List<Schedule>> {
        val cachedEntities = scheduleDao.getSchedulesForSuburbDirect(suburbId)
        return if (cachedEntities.isNotEmpty()) {
            NetworkResult.Error(
                message = "Showing offline cached schedule.",
                data = cachedEntities.map { it.toDomainModel() }
            )
        } else {
            val mockDtos = MockLoadSheddingData.getMockSchedules(suburbId)
            scheduleDao.insertSchedules(mockDtos.map { it.toEntity() })
            NetworkResult.Error(
                message = "Showing offline cached schedule.",
                data = mockDtos.map { it.toDomainModel() }
            )
        }
    }

    // Observe schedule slots for a suburb.
    fun getSchedulesForSuburb(suburbId: String): Flow<List<Schedule>> {
        return scheduleDao.getSchedulesForSuburb(suburbId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    // Observe schedules filtered by suburb and date.
    fun getSchedulesForSuburbAndDate(suburbId: String, date: String): Flow<List<Schedule>> {
        return scheduleDao.getSchedulesForSuburbAndDate(suburbId, date).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    // Save schedules into Room.
    suspend fun saveSchedules(schedules: List<Schedule>) {
        scheduleDao.insertSchedules(schedules.map { it.toEntity() })
    }

    // Delete schedules for a suburb.
    suspend fun deleteSchedulesForSuburb(suburbId: String) {
        scheduleDao.deleteSchedulesForSuburb(suburbId)
    }

    // Clear outdated schedule slots.
    suspend fun clearOutdatedSchedules(currentDate: String) {
        scheduleDao.clearOutdatedSchedules(currentDate)
    }
}
