package com.example.loadsheddingapp.data.remote

import com.example.loadsheddingapp.data.remote.dto.ScheduleDto
import com.example.loadsheddingapp.data.remote.dto.SuburbDto

// Temporary mock data generator used as a clean fallback when the live REST API is offline or unreachable.
object MockLoadSheddingData {

    val mockSuburbs = listOf(
        SuburbDto(
            suburbId = "jhb-sandton-01",
            name = "Sandton Central",
            municipality = "City of Johannesburg",
            province = "Gauteng",
            currentStage = 2,
            lastUpdated = System.currentTimeMillis()
        ),
        SuburbDto(
            suburbId = "jhb-randburg-02",
            name = "Randburg",
            municipality = "City of Johannesburg",
            province = "Gauteng",
            currentStage = 1,
            lastUpdated = System.currentTimeMillis()
        ),
        SuburbDto(
            suburbId = "cpt-seapoint-03",
            name = "Sea Point",
            municipality = "City of Cape Town",
            province = "Western Cape",
            currentStage = 0,
            lastUpdated = System.currentTimeMillis()
        ),
        SuburbDto(
            suburbId = "dbn-umhlanga-04",
            name = "Umhlanga Rocks",
            municipality = "eThekwini",
            province = "KwaZulu-Natal",
            currentStage = 3,
            lastUpdated = System.currentTimeMillis()
        ),
        SuburbDto(
            suburbId = "pta-centurion-05",
            name = "Centurion",
            municipality = "City of Tshwane",
            province = "Gauteng",
            currentStage = 2,
            lastUpdated = System.currentTimeMillis()
        ),
        SuburbDto(
            suburbId = "bloem-danpienaar-06",
            name = "Dan Pienaar",
            municipality = "Mangaung",
            province = "Free State",
            currentStage = 1,
            lastUpdated = System.currentTimeMillis()
        )
    )

    fun getMockSchedules(suburbId: String): List<ScheduleDto> {
        val today = "2025-05-20"
        val tomorrow = "2025-05-21"

        return listOf(
            ScheduleDto(
                id = 101,
                suburbId = suburbId,
                startTime = "06:00",
                endTime = "08:30",
                stage = 1,
                date = today
            ),
            ScheduleDto(
                id = 102,
                suburbId = suburbId,
                startTime = "14:00",
                endTime = "16:30",
                stage = 2,
                date = today
            ),
            ScheduleDto(
                id = 103,
                suburbId = suburbId,
                startTime = "22:00",
                endTime = "00:30",
                stage = 2,
                date = today
            ),
            ScheduleDto(
                id = 104,
                suburbId = suburbId,
                startTime = "04:00",
                endTime = "06:30",
                stage = 1,
                date = tomorrow
            ),
            ScheduleDto(
                id = 105,
                suburbId = suburbId,
                startTime = "12:00",
                endTime = "14:30",
                stage = 3,
                date = tomorrow
            ),
            ScheduleDto(
                id = 106,
                suburbId = suburbId,
                startTime = "20:00",
                endTime = "22:30",
                stage = 2,
                date = tomorrow
            )
        )
    }
}
