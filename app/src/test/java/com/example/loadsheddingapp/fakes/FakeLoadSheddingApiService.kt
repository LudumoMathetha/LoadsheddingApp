package com.example.loadsheddingapp.fakes

import com.example.loadsheddingapp.data.remote.LoadSheddingApiService
import com.example.loadsheddingapp.data.remote.MockLoadSheddingData
import com.example.loadsheddingapp.data.remote.dto.LoadSheddingResponseDto
import com.example.loadsheddingapp.data.remote.dto.ScheduleDto
import com.example.loadsheddingapp.data.remote.dto.SuburbDto
import retrofit2.Response

class FakeLoadSheddingApiService : LoadSheddingApiService {

    var shouldReturnError = false

    override suspend fun searchSuburbs(query: String): Response<List<SuburbDto>> {
        if (shouldReturnError) {
            return Response.error(500, okhttp3.ResponseBody.create(null, "Server Error"))
        }
        val matches = MockLoadSheddingData.mockSuburbs.filter {
            it.name.contains(query, ignoreCase = true) || it.municipality.contains(query, ignoreCase = true)
        }
        return Response.success(matches)
    }

    override suspend fun getSuburbById(suburbId: String): Response<SuburbDto> {
        if (shouldReturnError) {
            return Response.error(404, okhttp3.ResponseBody.create(null, "Not Found"))
        }
        val item = MockLoadSheddingData.mockSuburbs.find { it.suburbId == suburbId }
            ?: MockLoadSheddingData.mockSuburbs[0]
        return Response.success(item)
    }

    override suspend fun getScheduleForSuburb(suburbId: String): Response<List<ScheduleDto>> {
        if (shouldReturnError) {
            return Response.error(500, okhttp3.ResponseBody.create(null, "Server Error"))
        }
        return Response.success(MockLoadSheddingData.getMockSchedules(suburbId))
    }

    override suspend fun getCurrentNationalStage(): Response<LoadSheddingResponseDto> {
        if (shouldReturnError) {
            return Response.error(500, okhttp3.ResponseBody.create(null, "Server Error"))
        }
        return Response.success(LoadSheddingResponseDto(currentStage = 2, status = "Stage 2 Active"))
    }
}
