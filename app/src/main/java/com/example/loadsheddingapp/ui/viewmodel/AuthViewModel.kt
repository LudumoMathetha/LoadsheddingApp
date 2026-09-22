package com.example.loadsheddingapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loadsheddingapp.data.repository.AuthRepository
import com.example.loadsheddingapp.domain.model.User
import com.example.loadsheddingapp.utils.NetworkResult
import com.example.loadsheddingapp.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// UI state for Login form
data class LoginUiState(
    val emailInput: String = "",
    val passwordInput: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

// UI state for Register form
data class RegisterUiState(
    val fullNameInput: String = "",
    val emailInput: String = "",
    val passwordInput: String = "",
    val confirmPasswordInput: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

// ViewModel managing login, registration, and user authentication state.
class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginUiState())
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow(RegisterUiState())
    val registerState: StateFlow<RegisterUiState> = _registerState.asStateFlow()

    val currentUser: StateFlow<User?> = authRepository.currentUser

    // Login Form Event Handlers
    fun onLoginEmailChanged(email: String) {
        _loginState.value = _loginState.value.copy(emailInput = email, errorMessage = null)
    }

    fun onLoginPasswordChanged(password: String) {
        _loginState.value = _loginState.value.copy(passwordInput = password, errorMessage = null)
    }

    fun toggleLoginPasswordVisibility() {
        _loginState.value = _loginState.value.copy(isPasswordVisible = !_loginState.value.isPasswordVisible)
    }

    fun login() {
        val email = _loginState.value.emailInput.trim()
        val password = _loginState.value.passwordInput

        if (email.isBlank()) {
            _loginState.value = _loginState.value.copy(errorMessage = "Please enter your email address.")
            return
        }
        if (!ValidationUtils.isValidEmail(email)) {
            _loginState.value = _loginState.value.copy(errorMessage = "Please enter a valid email address.")
            return
        }
        if (password.isBlank()) {
            _loginState.value = _loginState.value.copy(errorMessage = "Please enter your password.")
            return
        }

        _loginState.value = _loginState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            when (val result = authRepository.loginUser(email, password)) {
                is NetworkResult.Success -> {
                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        errorMessage = null
                    )
                }
                is NetworkResult.Error -> {
                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Authentication failed."
                    )
                }
                is NetworkResult.Loading -> {
                    _loginState.value = _loginState.value.copy(isLoading = true)
                }
            }
        }
    }

    // Register Form Event Handlers
    fun onRegisterFullNameChanged(name: String) {
        _registerState.value = _registerState.value.copy(fullNameInput = name, errorMessage = null)
    }

    fun onRegisterEmailChanged(email: String) {
        _registerState.value = _registerState.value.copy(emailInput = email, errorMessage = null)
    }

    fun onRegisterPasswordChanged(password: String) {
        _registerState.value = _registerState.value.copy(passwordInput = password, errorMessage = null)
    }

    fun onRegisterConfirmPasswordChanged(confirmPassword: String) {
        _registerState.value = _registerState.value.copy(confirmPasswordInput = confirmPassword, errorMessage = null)
    }

    fun toggleRegisterPasswordVisibility() {
        _registerState.value = _registerState.value.copy(isPasswordVisible = !_registerState.value.isPasswordVisible)
    }

    fun toggleRegisterConfirmPasswordVisibility() {
        _registerState.value = _registerState.value.copy(isConfirmPasswordVisible = !_registerState.value.isConfirmPasswordVisible)
    }

    fun register() {
        val fullName = _registerState.value.fullNameInput.trim()
        val email = _registerState.value.emailInput.trim()
        val password = _registerState.value.passwordInput
        val confirmPassword = _registerState.value.confirmPasswordInput

        if (fullName.isBlank()) {
            _registerState.value = _registerState.value.copy(errorMessage = "Please enter your full name.")
            return
        }
        if (!ValidationUtils.isValidEmail(email)) {
            _registerState.value = _registerState.value.copy(errorMessage = "Please enter a valid email address.")
            return
        }
        if (!ValidationUtils.isValidPassword(password)) {
            _registerState.value = _registerState.value.copy(errorMessage = "Password must be at least 6 characters long.")
            return
        }
        if (!ValidationUtils.doPasswordsMatch(password, confirmPassword)) {
            _registerState.value = _registerState.value.copy(errorMessage = "Passwords do not match.")
            return
        }

        _registerState.value = _registerState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            when (val result = authRepository.registerUser(fullName, email, password)) {
                is NetworkResult.Success -> {
                    _registerState.value = _registerState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        errorMessage = null
                    )
                }
                is NetworkResult.Error -> {
                    _registerState.value = _registerState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Registration failed."
                    )
                }
                is NetworkResult.Loading -> {
                    _registerState.value = _registerState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun logout() {
        authRepository.logoutUser()
        _loginState.value = LoginUiState()
        _registerState.value = RegisterUiState()
    }
}
