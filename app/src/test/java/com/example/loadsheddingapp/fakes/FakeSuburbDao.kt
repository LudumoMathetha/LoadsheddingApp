package com.example.loadsheddingapp.fakes

import com.example.loadsheddingapp.data.local.dao.SuburbDao
import com.example.loadsheddingapp.data.local.entity.SuburbEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeSuburbDao : SuburbDao {

    private val suburbsMap = mutableMapOf<String, SuburbEntity>()
    private val suburbsFlow = MutableStateFlow<List<SuburbEntity>>(emptyList())

    private fun updateFlow() {
        suburbsFlow.value = suburbsMap.values.sortedWith(compareByDescending<SuburbEntity> { it.isFavorite }.thenBy { it.name })
    }

    override suspend fun insertSuburb(suburb: SuburbEntity): Long {
        suburbsMap[suburb.suburbId] = suburb
        updateFlow()
        return 1L
    }

    override suspend fun updateSuburb(suburb: SuburbEntity): Int {
        suburbsMap[suburb.suburbId] = suburb
        updateFlow()
        return 1
    }

    override suspend fun deleteSuburb(suburb: SuburbEntity): Int {
        suburbsMap.remove(suburb.suburbId)
        updateFlow()
        return 1
    }

    override suspend fun deleteSuburbById(suburbId: String): Int {
        val removed = suburbsMap.remove(suburbId) != null
        updateFlow()
        return if (removed) 1 else 0
    }

    override fun getAllSuburbs(): Flow<List<SuburbEntity>> = suburbsFlow

    override fun getSuburbById(suburbId: String): Flow<SuburbEntity?> {
        return suburbsFlow.map { list -> list.find { it.suburbId == suburbId } }
    }

    override suspend fun getSuburbByIdDirect(suburbId: String): SuburbEntity? {
        return suburbsMap[suburbId]
    }

    override fun searchSavedSuburbs(query: String): Flow<List<SuburbEntity>> {
        return suburbsFlow.map { list ->
            list.filter { it.name.contains(query, ignoreCase = true) || it.municipality.contains(query, ignoreCase = true) }
        }
    }

    override suspend fun exists(suburbId: String): Boolean {
        return suburbsMap.containsKey(suburbId)
    }

    override suspend fun updateFavoriteStatus(suburbId: String, isFavorite: Boolean): Int {
        val existing = suburbsMap[suburbId]
        return if (existing != null) {
            suburbsMap[suburbId] = existing.copy(isFavorite = isFavorite)
            updateFlow()
            1
        } else 0
    }
}
