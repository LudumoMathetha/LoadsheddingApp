package com.example.loadsheddingapp.fakes

import com.example.loadsheddingapp.data.local.dao.ScheduleDao
import com.example.loadsheddingapp.data.local.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeScheduleDao : ScheduleDao {

    private val schedulesList = mutableListOf<ScheduleEntity>()
    private val schedulesFlow = MutableStateFlow<List<ScheduleEntity>>(emptyList())

    private fun updateFlow() {
        schedulesFlow.value = schedulesList.toList()
    }

    override suspend fun insertSchedule(schedule: ScheduleEntity): Long {
        schedulesList.add(schedule)
        updateFlow()
        return 1L
    }

    override suspend fun insertSchedules(schedules: List<ScheduleEntity>): List<Long> {
        schedulesList.addAll(schedules)
        updateFlow()
        return schedules.map { 1L }
    }

    override fun getSchedulesForSuburb(suburbId: String): Flow<List<ScheduleEntity>> {
        return schedulesFlow.map { list -> list.filter { it.suburbId == suburbId } }
    }

    override suspend fun getSchedulesForSuburbDirect(suburbId: String): List<ScheduleEntity> {
        return schedulesList.filter { it.suburbId == suburbId }
    }

    override fun getSchedulesForSuburbAndDate(suburbId: String, date: String): Flow<List<ScheduleEntity>> {
        return schedulesFlow.map { list -> list.filter { it.suburbId == suburbId && it.date == date } }
    }

    override suspend fun deleteSchedulesForSuburb(suburbId: String): Int {
        val count = schedulesList.count { it.suburbId == suburbId }
        schedulesList.removeAll { it.suburbId == suburbId }
        updateFlow()
        return count
    }

    override suspend fun clearOutdatedSchedules(currentDate: String): Int {
        val count = schedulesList.count { it.date < currentDate }
        schedulesList.removeAll { it.date < currentDate }
        updateFlow()
        return count
    }
}
