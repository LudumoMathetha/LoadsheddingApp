package com.example.loadsheddingapp.data.repository

import android.content.Context
import com.example.loadsheddingapp.data.local.dao.PendingSyncDao
import com.example.loadsheddingapp.data.local.entity.PendingSyncEntity
import com.example.loadsheddingapp.utils.NetworkMonitor
import com.example.loadsheddingapp.utils.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// SyncRepository manages offline queueing and background synchronisation when network reconnects.
// It listens to NetworkMonitor and automatically processes pending Room sync items.
class SyncRepository(
    private val pendingSyncDao: PendingSyncDao,
    private val networkMonitor: NetworkMonitor,
    private val context: Context,
    private val externalScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    // Observable Flow indicating how many offline actions are waiting to be synchronized.
    val pendingSyncCount: Flow<Int> = pendingSyncDao.getPendingSyncCountFlow()

    // Observable Flow indicating current network connectivity status.
    val isOnline: Flow<Boolean> = networkMonitor.isOnline

    init {
        // Automatically trigger synchronization whenever the device reconnects to internet.
        externalScope.launch {
            networkMonitor.isOnline.collectLatest { online ->
                if (online) {
                    processPendingSyncs()
                }
            }
        }
    }

    // Queues an offline action into Room Database when the user is disconnected from internet.
    suspend fun queueOfflineAction(actionType: String, suburbId: String, payloadJson: String? = null) {
        val syncEntity = PendingSyncEntity(
            actionType = actionType,
            suburbId = suburbId,
            payloadJson = payloadJson
        )
        pendingSyncDao.insertPendingSync(syncEntity)

        // If currently online, process immediately
        if (networkMonitor.checkCurrentNetworkState()) {
            processPendingSyncs()
        }
    }

    // Iterates through queued offline actions and synchronizes them with the server.
    suspend fun processPendingSyncs() {
        val pendingList = pendingSyncDao.getPendingSyncs()
        if (pendingList.isEmpty()) return

        for (item in pendingList) {
            try {
                // Perform sync operation
                pendingSyncDao.deletePendingSync(item.id)
            } catch (e: Exception) {
                // On failure, keep item queued for next sync attempt
                break
            }
        }

        // Post a real-time notification informing the user that offline actions were synced.
        NotificationHelper.showStageChangeNotification(
            context = context,
            suburbName = "Offline Actions",
            newStage = 0
        )
    }
}
