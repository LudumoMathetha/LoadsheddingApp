package com.example.loadsheddingapp.viewmodel

import com.example.loadsheddingapp.data.repository.SuburbRepository
import com.example.loadsheddingapp.domain.model.Suburb
import com.example.loadsheddingapp.fakes.FakeLoadSheddingApiService
import com.example.loadsheddingapp.fakes.FakeSuburbDao
import com.example.loadsheddingapp.ui.viewmodel.SuburbViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SuburbViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeSuburbDao: FakeSuburbDao
    private lateinit var fakeApiService: FakeLoadSheddingApiService
    private lateinit var suburbRepository: SuburbRepository
    private lateinit var suburbViewModel: SuburbViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeSuburbDao = FakeSuburbDao()
        fakeApiService = FakeLoadSheddingApiService()
        suburbRepository = SuburbRepository(fakeSuburbDao, fakeApiService)
        suburbViewModel = SuburbViewModel(suburbRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun searchSuburbs_emptyQuery_setsErrorMessage() {
        suburbViewModel.onSearchQueryChanged("")
        suburbViewModel.searchSuburbs()

        assertEquals("Please enter a suburb or municipality name to search.", suburbViewModel.searchUiState.value.errorMessage)
    }

    @Test
    fun searchSuburbs_validQuery_returnsMatchingResults() {
        suburbViewModel.onSearchQueryChanged("Sandton")
        suburbViewModel.searchSuburbs()

        val state = suburbViewModel.searchUiState.value
        assertNull(state.errorMessage)
        assertTrue(state.searchResults.isNotEmpty())
        assertTrue(state.searchResults.any { it.name.contains("Sandton") })
    }

    @Test
    fun saveSuburb_addsToSavedListAndShowsNotification() {
        val suburb = Suburb(
            suburbId = "sandton-01",
            name = "Sandton Central",
            municipality = "City of Johannesburg",
            province = "Gauteng",
            currentStage = 2,
            lastUpdated = System.currentTimeMillis()
        )

        suburbViewModel.saveSuburb(suburb)

        assertEquals("Sandton Central was successfully added to your saved suburbs.", suburbViewModel.searchUiState.value.successNotification)
        assertEquals(1, suburbViewModel.savedSuburbs.value.size)
    }

    @Test
    fun saveSuburb_duplicateSuburb_showsDuplicateNotification() {
        val suburb = Suburb(
            suburbId = "randburg-02",
            name = "Randburg",
            municipality = "City of Johannesburg",
            province = "Gauteng",
            currentStage = 1,
            lastUpdated = System.currentTimeMillis()
        )

        suburbViewModel.saveSuburb(suburb)
        suburbViewModel.saveSuburb(suburb) // Save duplicate

        assertEquals("Randburg is already saved in your list.", suburbViewModel.searchUiState.value.successNotification)
        assertEquals(1, suburbViewModel.savedSuburbs.value.size)
    }
}
