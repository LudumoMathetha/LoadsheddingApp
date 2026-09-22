package com.example.loadsheddingapp.repository

import com.example.loadsheddingapp.data.repository.ScheduleRepository
import com.example.loadsheddingapp.domain.model.Schedule
import com.example.loadsheddingapp.fakes.FakeLoadSheddingApiService
import com.example.loadsheddingapp.fakes.FakeScheduleDao
import com.example.loadsheddingapp.utils.NetworkResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ScheduleRepositoryTest {

    private lateinit var fakeScheduleDao: FakeScheduleDao
    private lateinit var fakeApiService: FakeLoadSheddingApiService
    private lateinit var scheduleRepository: ScheduleRepository

    @Before
    fun setUp() {
        fakeScheduleDao = FakeScheduleDao()
        fakeApiService = FakeLoadSheddingApiService()
        scheduleRepository = ScheduleRepository(fakeScheduleDao, fakeApiService)
    }

    @Test
    fun saveSchedules_insertsAndRetrievesSchedulesForSuburb() = runBlocking {
        val testSchedules = listOf(
            Schedule(id = 1, suburbId = "suburb-01", startTime = "08:00", endTime = "10:30", stage = 2, date = "2025-05-20"),
            Schedule(id = 2, suburbId = "suburb-01", startTime = "16:00", endTime = "18:30", stage = 2, date = "2025-05-20")
        )

        scheduleRepository.saveSchedules(testSchedules)

        val retrieved = scheduleRepository.getSchedulesForSuburb("suburb-01").first()
        assertEquals(2, retrieved.size)
        assertEquals("08:00", retrieved[0].startTime)
    }

    @Test
    fun getSchedulesForSuburbAndDate_filtersByDate() = runBlocking {
        val testSchedules = listOf(
            Schedule(id = 1, suburbId = "suburb-02", startTime = "08:00", endTime = "10:30", stage = 1, date = "2025-05-20"),
            Schedule(id = 2, suburbId = "suburb-02", startTime = "12:00", endTime = "14:30", stage = 1, date = "2025-05-21")
        )

        scheduleRepository.saveSchedules(testSchedules)

        val retrievedToday = scheduleRepository.getSchedulesForSuburbAndDate("suburb-02", "2025-05-20").first()
        assertEquals(1, retrievedToday.size)
        assertEquals("08:00", retrievedToday[0].startTime)
    }

    @Test
    fun fetchAndCacheSchedule_fallbackSeedsMockSchedulesWhenOffline() = runBlocking {
        fakeApiService.shouldReturnError = true

        val result = scheduleRepository.fetchAndCacheSchedule("offline-suburb-99")
        assertTrue(result is NetworkResult.Error)
        val list = result.data!!
        assertTrue(list.isNotEmpty())
        assertEquals("offline-suburb-99", list[0].suburbId)
    }
}
