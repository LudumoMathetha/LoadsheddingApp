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

// The AuthRepository handles local prototype authentication, session state, and Single Sign-On (SSO).
// In production, SSO identity tokens (OAuth2/OpenID Connect) are validated against identity providers like Google or Microsoft.
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

    // Authenticates a user using Google Single Sign-On (SSO).
    // Automatically creates or retrieves the user profile in Room without requiring password entry.
    suspend fun loginWithGoogle(email: String, fullName: String): NetworkResult<User> {
        return authenticateWithSsoProvider("Google", email, fullName)
    }

    // Generic Single Sign-On (SSO) authentication handler for identity providers (Google, Microsoft, GitHub).
    suspend fun authenticateWithSsoProvider(provider: String, email: String, fullName: String): NetworkResult<User> {
        return try {
            val cleanEmail = email.trim().lowercase()
            val cleanName = fullName.ifBlank { "SSO User" }
            var existingUser = userDao.getUserByEmail(cleanEmail)

            if (existingUser == null) {
                // If SSO user does not exist in local database, create a new profile automatically
                val userId = "sso_${UUID.randomUUID()}"
                val ssoPasswordPlaceholder = ValidationUtils.hashPassword("SSO_FEDERATED_LOGIN_${System.currentTimeMillis()}")

                existingUser = UserEntity(
                    userId = userId,
                    fullName = cleanName,
                    email = cleanEmail,
                    passwordHash = ssoPasswordPlaceholder
                )
                userDao.insertUser(existingUser)
            }

            val ssoUser = existingUser.toDomainModel()
            _currentUser.value = ssoUser
            NetworkResult.Success(ssoUser)
        } catch (e: Exception) {
            NetworkResult.Error("$provider Single Sign-On failed: ${e.localizedMessage}")
        }
    }

    // Logs out the currently authenticated user session.
    fun logoutUser() {
        _currentUser.value = null
    }
}
