package com.example.loadsheddingapp.data.remote

import com.example.loadsheddingapp.data.remote.dto.LoadSheddingResponseDto
import com.example.loadsheddingapp.data.remote.dto.ScheduleDto
import com.example.loadsheddingapp.data.remote.dto.SuburbDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Retrofit interface defining REST API endpoints for suburb search, schedules, and national stage status.
interface LoadSheddingApiService {

    // Conceptual endpoint: GET /api/suburbs/search?query={query}
    @GET("suburbs/search")
    suspend fun searchSuburbs(
        @Query("query") query: String
    ): Response<List<SuburbDto>>

    // Conceptual endpoint: GET /api/suburbs/{suburbId}
    @GET("suburbs/{suburbId}")
    suspend fun getSuburbById(
        @Path("suburbId") suburbId: String
    ): Response<SuburbDto>

    // Conceptual endpoint: GET /api/suburbs/{suburbId}/schedule
    @GET("suburbs/{suburbId}/schedule")
    suspend fun getScheduleForSuburb(
        @Path("suburbId") suburbId: String
    ): Response<List<ScheduleDto>>

    // Conceptual endpoint: GET /api/loadshedding/current
    @GET("loadshedding/current")
    suspend fun getCurrentNationalStage(): Response<LoadSheddingResponseDto>
}
