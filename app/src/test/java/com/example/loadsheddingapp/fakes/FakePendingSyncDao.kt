package com.example.loadsheddingapp.fakes

import com.example.loadsheddingapp.data.local.dao.PendingSyncDao
import com.example.loadsheddingapp.data.local.entity.PendingSyncEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakePendingSyncDao : PendingSyncDao {

    private val pendingList = mutableListOf<PendingSyncEntity>()
    private val countFlow = MutableStateFlow(0)

    private fun updateFlow() {
        countFlow.value = pendingList.size
    }

    override suspend fun insertPendingSync(sync: PendingSyncEntity): Long {
        pendingList.add(sync)
        updateFlow()
        return 1L
    }

    override suspend fun getPendingSyncs(): List<PendingSyncEntity> {
        return pendingList.toList()
    }

    override fun getPendingSyncCountFlow(): Flow<Int> = countFlow

    override suspend fun deletePendingSync(id: Int): Int {
        val removed = pendingList.removeIf { it.id == id }
        updateFlow()
        return if (removed) 1 else 0
    }

    override suspend fun clearPendingSyncs(): Int {
        val count = pendingList.size
        pendingList.clear()
        updateFlow()
        return count
    }
}
