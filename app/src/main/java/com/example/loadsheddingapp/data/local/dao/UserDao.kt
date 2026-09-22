package com.example.loadsheddingapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.loadsheddingapp.data.local.entity.UserEntity

// UserDao handles local database storage and verification for prototype user accounts.
// Plaintext passwords are not stored here; password hashes are stored instead.
@Dao
interface UserDao {

    // Insert a new prototype user account into Room.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    // Query user profile by email address for authentication verification.
    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    // Query user profile by unique user ID string.
    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    suspend fun getUserById(userId: String): UserEntity?

    // Delete a user profile from local database.
    @Query("DELETE FROM users WHERE userId = :userId")
    suspend fun deleteUser(userId: String): Int
}
