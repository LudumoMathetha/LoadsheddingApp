package com.example.loadsheddingapp.repository

import com.example.loadsheddingapp.data.repository.SuburbRepository
import com.example.loadsheddingapp.domain.model.Suburb
import com.example.loadsheddingapp.fakes.FakeSuburbDao
import com.example.loadsheddingapp.utils.NetworkResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SuburbRepositoryTest {

    private lateinit var fakeSuburbDao: FakeSuburbDao
    private lateinit var suburbRepository: SuburbRepository

    @Before
    fun setUp() {
        fakeSuburbDao = FakeSuburbDao()
        suburbRepository = SuburbRepository(fakeSuburbDao)
    }

    @Test
    fun saveSuburb_addsSuburbToDatabase() = runBlocking {
        val testSuburb = Suburb(
            suburbId = "sandton-01",
            name = "Sandton",
            municipality = "City of Johannesburg",
            province = "Gauteng",
            currentStage = 2,
            lastUpdated = System.currentTimeMillis()
        )

        suburbRepository.saveSuburb(testSuburb)

        val saved = suburbRepository.savedSuburbs.first()
        assertEquals(1, saved.size)
        assertEquals("Sandton", saved[0].name)
        assertTrue(suburbRepository.exists("sandton-01"))
    }

    @Test
    fun deleteSuburb_removesSuburbFromDatabase() = runBlocking {
        val testSuburb = Suburb(
            suburbId = "randburg-02",
            name = "Randburg",
            municipality = "City of Johannesburg",
            province = "Gauteng",
            currentStage = 1,
            lastUpdated = System.currentTimeMillis()
        )

        suburbRepository.saveSuburb(testSuburb)
        assertTrue(suburbRepository.exists("randburg-02"))

        suburbRepository.deleteSuburb(testSuburb)
        assertFalse(suburbRepository.exists("randburg-02"))
        val saved = suburbRepository.savedSuburbs.first()
        assertEquals(0, saved.size)
    }

    @Test
    fun toggleFavorite_updatesFavoriteState() = runBlocking {
        val testSuburb = Suburb(
            suburbId = "seapoint-03",
            name = "Sea Point",
            municipality = "City of Cape Town",
            province = "Western Cape",
            currentStage = 0,
            lastUpdated = System.currentTimeMillis(),
            isFavorite = false
        )

        suburbRepository.saveSuburb(testSuburb)
        suburbRepository.toggleFavorite("seapoint-03", true)

        val updated = suburbRepository.getSuburbById("seapoint-03").first()
        assertTrue(updated!!.isFavorite)
    }

    @Test
    fun searchRemoteSuburbs_blankQuery_returnsEmptyList() = runBlocking {
        val result = suburbRepository.searchRemoteSuburbs("  ")
        assertTrue(result is NetworkResult.Success)
        assertTrue(result.data!!.isEmpty())
    }
}
