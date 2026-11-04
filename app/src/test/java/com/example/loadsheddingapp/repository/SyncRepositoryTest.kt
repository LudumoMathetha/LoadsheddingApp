package com.example.loadsheddingapp.repository

import com.example.loadsheddingapp.data.local.entity.PendingSyncEntity
import com.example.loadsheddingapp.fakes.FakePendingSyncDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SyncRepositoryTest {

    private lateinit var fakePendingSyncDao: FakePendingSyncDao

    @Before
    fun setUp() {
        fakePendingSyncDao = FakePendingSyncDao()
    }

    @Test
    fun insertPendingSync_queuesOfflineActionInRoom() = runBlocking {
        val syncAction = PendingSyncEntity(
            id = 1,
            actionType = "ADD_SUBURB",
            suburbId = "sandton-01"
        )

        fakePendingSyncDao.insertPendingSync(syncAction)

        val pendingList = fakePendingSyncDao.getPendingSyncs()
        assertEquals(1, pendingList.size)
        assertEquals("ADD_SUBURB", pendingList[0].actionType)
        assertEquals(1, fakePendingSyncDao.getPendingSyncCountFlow().first())
    }

    @Test
    fun deletePendingSync_removesProcessedActionFromRoom() = runBlocking {
        val syncAction = PendingSyncEntity(
            id = 1,
            actionType = "TOGGLE_FAVORITE",
            suburbId = "randburg-02"
        )

        fakePendingSyncDao.insertPendingSync(syncAction)
        assertEquals(1, fakePendingSyncDao.getPendingSyncs().size)

        fakePendingSyncDao.deletePendingSync(1)
        assertEquals(0, fakePendingSyncDao.getPendingSyncs().size)
        assertEquals(0, fakePendingSyncDao.getPendingSyncCountFlow().first())
    }
}
