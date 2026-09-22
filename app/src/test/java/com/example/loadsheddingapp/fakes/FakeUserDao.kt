package com.example.loadsheddingapp.fakes

import com.example.loadsheddingapp.data.local.dao.UserDao
import com.example.loadsheddingapp.data.local.entity.UserEntity

class FakeUserDao : UserDao {

    private val usersMap = mutableMapOf<String, UserEntity>()

    override suspend fun insertUser(user: UserEntity): Long {
        usersMap[user.userId] = user
        return 1L
    }

    override suspend fun getUserByEmail(email: String): UserEntity? {
        return usersMap.values.find { it.email.equals(email, ignoreCase = true) }
    }

    override suspend fun getUserById(userId: String): UserEntity? {
        return usersMap[userId]
    }

    override suspend fun deleteUser(userId: String): Int {
        return if (usersMap.remove(userId) != null) 1 else 0
    }
}
