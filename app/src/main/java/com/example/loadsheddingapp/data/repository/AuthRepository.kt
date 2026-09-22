package com.example.loadsheddingapp.data.repository

import com.example.loadsheddingapp.data.local.dao.UserDao
import com.example.loadsheddingapp.data.local.entity.UserEntity
import com.example.loadsheddingapp.data.local.entity.toDomainModel
import com.example.loadsheddingapp.domain.model.User
import com.example.loadsheddingapp.utils.NetworkResult
import com.example.loadsheddingapp.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

// The AuthRepository handles local prototype authentication operations.
// Note: This repository acts as a local prototype implementation.
// In future PoE stages, Firebase Authentication will replace local Room user authentication.
class AuthRepository(
    private val userDao: UserDao
) {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Registers a new user locally using hashed credentials.
    suspend fun registerUser(fullName: String, email: String, password: String): NetworkResult<User> {
        return try {
            val existingUser = userDao.getUserByEmail(email.trim())
            if (existingUser != null) {
                return NetworkResult.Error("An account with this email address already exists.")
            }

            val userId = UUID.randomUUID().toString()
            val passwordHash = ValidationUtils.hashPassword(password)
            val userEntity = UserEntity(
                userId = userId,
                fullName = fullName.trim(),
                email = email.trim(),
                passwordHash = passwordHash
            )

            userDao.insertUser(userEntity)
            val registeredUser = userEntity.toDomainModel()
            _currentUser.value = registeredUser
            NetworkResult.Success(registeredUser)
        } catch (e: Exception) {
            NetworkResult.Error("Failed to register user: ${e.localizedMessage}")
        }
    }

    // Authenticates user credentials locally against the Room Database.
    suspend fun loginUser(email: String, password: String): NetworkResult<User> {
        return try {
            val userEntity = userDao.getUserByEmail(email.trim())
                ?: return NetworkResult.Error("Account not found. Please register first.")

            val inputHash = ValidationUtils.hashPassword(password)
            if (userEntity.passwordHash != inputHash) {
                return NetworkResult.Error("Incorrect password. Please try again.")
            }

            val authenticatedUser = userEntity.toDomainModel()
            _currentUser.value = authenticatedUser
            NetworkResult.Success(authenticatedUser)
        } catch (e: Exception) {
            NetworkResult.Error("Authentication failed: ${e.localizedMessage}")
        }
    }

    // Logs out the currently authenticated user session.
    fun logoutUser() {
        _currentUser.value = null
    }
}
