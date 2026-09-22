package com.example.loadsheddingapp.viewmodel

import com.example.loadsheddingapp.data.repository.AuthRepository
import com.example.loadsheddingapp.fakes.FakeUserDao
import com.example.loadsheddingapp.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class AuthViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeUserDao: FakeUserDao
    private lateinit var authRepository: AuthRepository
    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeUserDao = FakeUserDao()
        authRepository = AuthRepository(fakeUserDao)
        authViewModel = AuthViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun login_emptyEmail_setsErrorMessage() {
        authViewModel.onLoginEmailChanged("")
        authViewModel.onLoginPasswordChanged("Password123")
        authViewModel.login()

        assertEquals("Please enter your email address.", authViewModel.loginState.value.errorMessage)
        assertFalse(authViewModel.loginState.value.isSuccess)
    }

    @Test
    fun login_invalidEmailFormat_setsErrorMessage() {
        authViewModel.onLoginEmailChanged("notanemail")
        authViewModel.onLoginPasswordChanged("Password123")
        authViewModel.login()

        assertEquals("Please enter a valid email address.", authViewModel.loginState.value.errorMessage)
    }

    @Test
    fun register_weakPassword_setsErrorMessage() {
        authViewModel.onRegisterFullNameChanged("Ludumo Mathetha")
        authViewModel.onRegisterEmailChanged("ludumo@example.com")
        authViewModel.onRegisterPasswordChanged("123")
        authViewModel.onRegisterConfirmPasswordChanged("123")
        authViewModel.register()

        assertEquals("Password must be at least 6 characters long.", authViewModel.registerState.value.errorMessage)
    }

    @Test
    fun register_passwordMismatch_setsErrorMessage() {
        authViewModel.onRegisterFullNameChanged("Ludumo Mathetha")
        authViewModel.onRegisterEmailChanged("ludumo@example.com")
        authViewModel.onRegisterPasswordChanged("Password123")
        authViewModel.onRegisterConfirmPasswordChanged("Password456")
        authViewModel.register()

        assertEquals("Passwords do not match.", authViewModel.registerState.value.errorMessage)
    }

    @Test
    fun register_validDetails_registersSuccessfully() {
        authViewModel.onRegisterFullNameChanged("Ludumo Mathetha")
        authViewModel.onRegisterEmailChanged("ludumo@example.com")
        authViewModel.onRegisterPasswordChanged("Password123")
        authViewModel.onRegisterConfirmPasswordChanged("Password123")
        authViewModel.register()

        assertTrue(authViewModel.registerState.value.isSuccess)
        assertNull(authViewModel.registerState.value.errorMessage)
        assertEquals("ludumo@example.com", authViewModel.currentUser.value?.email)
    }

    @Test
    fun logout_resetsAuthStateAndSession() {
        authViewModel.onRegisterFullNameChanged("Ludumo Mathetha")
        authViewModel.onRegisterEmailChanged("ludumo@example.com")
        authViewModel.onRegisterPasswordChanged("Password123")
        authViewModel.onRegisterConfirmPasswordChanged("Password123")
        authViewModel.register()

        authViewModel.logout()

        assertNull(authViewModel.currentUser.value)
        assertFalse(authViewModel.loginState.value.isSuccess)
        assertFalse(authViewModel.registerState.value.isSuccess)
    }
}
