package com.example.loadsheddingapp.viewmodel

import com.example.loadsheddingapp.data.repository.ScheduleRepository
import com.example.loadsheddingapp.data.repository.SuburbRepository
import com.example.loadsheddingapp.domain.model.Suburb
import com.example.loadsheddingapp.fakes.FakeLoadSheddingApiService
import com.example.loadsheddingapp.fakes.FakeScheduleDao
import com.example.loadsheddingapp.fakes.FakeSuburbDao
import com.example.loadsheddingapp.ui.viewmodel.ScheduleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ScheduleViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeScheduleDao: FakeScheduleDao
    private lateinit var fakeSuburbDao: FakeSuburbDao
    private lateinit var fakeApiService: FakeLoadSheddingApiService
    private lateinit var scheduleRepository: ScheduleRepository
    private lateinit var suburbRepository: SuburbRepository
    private lateinit var scheduleViewModel: ScheduleViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeScheduleDao = FakeScheduleDao()
        fakeSuburbDao = FakeSuburbDao()
        fakeApiService = FakeLoadSheddingApiService()

        scheduleRepository = ScheduleRepository(fakeScheduleDao, fakeApiService)
        suburbRepository = SuburbRepository(fakeSuburbDao, fakeApiService)
        scheduleViewModel = ScheduleViewModel(scheduleRepository, suburbRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadScheduleForSuburb_loadsScheduleSuccessfully() = runBlocking {
        val testSuburb = Suburb(
            suburbId = "jhb-sandton-01",
            name = "Sandton Central",
            municipality = "City of Johannesburg",
            province = "Gauteng",
            currentStage = 2,
            lastUpdated = System.currentTimeMillis()
        )
        suburbRepository.saveSuburb(testSuburb)

        scheduleViewModel.loadScheduleForSuburb("jhb-sandton-01")

        val state = scheduleViewModel.scheduleUiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals("Sandton Central", state.suburb?.name)
        assertTrue(state.schedules.isNotEmpty())
    }

    @Test
    fun loadScheduleForSuburb_apiError_loadsCachedSchedulesAndSetsCachedFlag() = runBlocking {
        fakeApiService.shouldReturnError = true

        scheduleViewModel.loadScheduleForSuburb("jhb-sandton-01")

        val state = scheduleViewModel.scheduleUiState.value
        assertFalse(state.isLoading)
        assertTrue(state.isCachedData)
        assertTrue(state.schedules.isNotEmpty())
    }
}
