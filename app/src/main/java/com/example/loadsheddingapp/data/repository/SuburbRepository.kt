package com.example.loadsheddingapp.data.repository

import com.example.loadsheddingapp.data.local.dao.SuburbDao
import com.example.loadsheddingapp.data.local.entity.toDomainModel
import com.example.loadsheddingapp.data.local.entity.toEntity
import com.example.loadsheddingapp.data.remote.LoadSheddingApiService
import com.example.loadsheddingapp.data.remote.MockLoadSheddingData
import com.example.loadsheddingapp.data.remote.RetrofitInstance
import com.example.loadsheddingapp.data.remote.dto.toDomainModel
import com.example.loadsheddingapp.domain.model.Suburb
import com.example.loadsheddingapp.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// The SuburbRepository manages suburb data between the local Room Database and remote Retrofit API sources.
// Following the Repository Pattern keeps ViewModels separated from data source details.
class SuburbRepository(
    private val suburbDao: SuburbDao,
    private val apiService: LoadSheddingApiService = RetrofitInstance.api
) {
    // Flow of all saved suburbs from Room Database
    val savedSuburbs: Flow<List<Suburb>> = suburbDao.getAllSuburbs().map { entities ->
        entities.map { it.toDomainModel() }
    }

    // Searches remote REST API for matching suburbs with fallback to mock data on network error
    suspend fun searchRemoteSuburbs(query: String): NetworkResult<List<Suburb>> {
        if (query.isBlank()) return NetworkResult.Success(emptyList())

        return try {
            val response = apiService.searchSuburbs(query)
            if (response.isSuccessful && response.body() != null) {
                val suburbs = response.body()!!.map { dto ->
                    val isFav = suburbDao.exists(dto.suburbId)
                    dto.toDomainModel(isFavorite = isFav)
                }
                NetworkResult.Success(suburbs)
            } else {
                // Network HTTP failure fallback
                val mockMatches = MockLoadSheddingData.mockSuburbs
                    .filter { it.name.contains(query, ignoreCase = true) || it.municipality.contains(query, ignoreCase = true) }
                    .map { dto ->
                        val isFav = suburbDao.exists(dto.suburbId)
                        dto.toDomainModel(isFavorite = isFav)
                    }
                NetworkResult.Success(mockMatches)
            }
        } catch (e: Exception) {
            // Offline or network exception fallback
            val mockMatches = MockLoadSheddingData.mockSuburbs
                .filter { it.name.contains(query, ignoreCase = true) || it.municipality.contains(query, ignoreCase = true) }
                .map { dto ->
                    val isFav = suburbDao.exists(dto.suburbId)
                    dto.toDomainModel(isFavorite = isFav)
                }
            NetworkResult.Success(mockMatches)
        }
    }

    // Retrieves national load-shedding stage from API with fallback
    suspend fun getNationalStage(): NetworkResult<Int> {
        return try {
            val response = apiService.getCurrentNationalStage()
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!.currentStage)
            } else {
                NetworkResult.Success(2) // Default prototype fallback stage
            }
        } catch (e: Exception) {
            NetworkResult.Success(2) // Offline fallback stage
        }
    }

    // Save a new suburb into Room Database.
    suspend fun saveSuburb(suburb: Suburb) {
        suburbDao.insertSuburb(suburb.toEntity())
    }

    // Delete a suburb from local database.
    suspend fun deleteSuburb(suburb: Suburb) {
        suburbDao.deleteSuburb(suburb.toEntity())
    }

    // Delete a suburb by ID.
    suspend fun deleteSuburbById(suburbId: String) {
        suburbDao.deleteSuburbById(suburbId)
    }

    // Query single suburb by ID.
    fun getSuburbById(suburbId: String): Flow<Suburb?> {
        return suburbDao.getSuburbById(suburbId).map { it?.toDomainModel() }
    }

    // Check if a suburb already exists in Room.
    suspend fun exists(suburbId: String): Boolean {
        return suburbDao.exists(suburbId)
    }

    // Toggle favorite state.
    suspend fun toggleFavorite(suburbId: String, isFavorite: Boolean) {
        suburbDao.updateFavoriteStatus(suburbId, isFavorite)
    }
}
