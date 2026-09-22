package com.example.loadsheddingapp.repository

import com.example.loadsheddingapp.data.repository.AuthRepository
import com.example.loadsheddingapp.fakes.FakeUserDao
import com.example.loadsheddingapp.utils.NetworkResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthRepositoryTest {

    private lateinit var fakeUserDao: FakeUserDao
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        fakeUserDao = FakeUserDao()
        authRepository = AuthRepository(fakeUserDao)
    }

    @Test
    fun registerUser_successfulRegistration_setsCurrentUser() = runBlocking {
        val result = authRepository.registerUser("Ludumo Mathetha", "student@example.com", "Password123")

        assertTrue(result is NetworkResult.Success)
        val user = result.data
        assertEquals("Ludumo Mathetha", user?.fullName)
        assertEquals("student@example.com", user?.email)
        assertEquals("student@example.com", authRepository.currentUser.value?.email)
    }

    @Test
    fun registerUser_duplicateEmail_returnsError() = runBlocking {
        authRepository.registerUser("Student One", "duplicate@example.com", "Password123")
        val result = authRepository.registerUser("Student Two", "duplicate@example.com", "Password456")

        assertTrue(result is NetworkResult.Error)
        assertEquals("An account with this email address already exists.", result.message)
    }

    @Test
    fun loginUser_validCredentials_authenticatesSuccessfully() = runBlocking {
        authRepository.registerUser("Ludumo Mathetha", "login@example.com", "ValidPass123")
        authRepository.logoutUser()
        assertNull(authRepository.currentUser.value)

        val result = authRepository.loginUser("login@example.com", "ValidPass123")
        assertTrue(result is NetworkResult.Success)
        assertEquals("login@example.com", authRepository.currentUser.value?.email)
    }

    @Test
    fun loginUser_incorrectPassword_returnsError() = runBlocking {
        authRepository.registerUser("Ludumo Mathetha", "login2@example.com", "ValidPass123")
        authRepository.logoutUser()

        val result = authRepository.loginUser("login2@example.com", "WrongPassword")
        assertTrue(result is NetworkResult.Error)
        assertEquals("Incorrect password. Please try again.", result.message)
    }

    @Test
    fun loginUser_unregisteredEmail_returnsError() = runBlocking {
        val result = authRepository.loginUser("nonexistent@example.com", "SomePassword")
        assertTrue(result is NetworkResult.Error)
        assertEquals("Account not found. Please register first.", result.message)
    }

    @Test
    fun logoutUser_clearsCurrentUserSession() = runBlocking {
        authRepository.registerUser("Ludumo Mathetha", "logout@example.com", "Password123")
        assertTrue(authRepository.currentUser.value != null)

        authRepository.logoutUser()
        assertNull(authRepository.currentUser.value)
    }
}
