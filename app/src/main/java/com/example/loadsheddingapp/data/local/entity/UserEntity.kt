package com.example.loadsheddingapp.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.loadsheddingapp.domain.model.User

// UserEntity stores prototype account information locally in the "users" table.
// Storing SHA-256 passwordHash instead of plain text prevents credential exposure.
@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey
    val userId: String,
    val fullName: String,
    val email: String,
    val passwordHash: String,
    val createdAt: Long = System.currentTimeMillis()
)

// Converts user entity to domain model.
fun UserEntity.toDomainModel(): User {
    return User(
        userId = userId,
        fullName = fullName,
        email = email
    )
}
