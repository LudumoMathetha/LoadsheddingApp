package com.example.loadsheddingapp.domain.model

// Domain model representing an authenticated user profile in the local prototype.
data class User(
    val userId: String,
    val fullName: String,
    val email: String
)
