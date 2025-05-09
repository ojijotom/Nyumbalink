package com.hasham.nyumbalink.repository

import com.hasham.nyumbalink.data.UserDao
import com.hasham.nyumbalink.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun registerUser(user: User) {
        userDao.registerUser(user)
    }

    suspend fun loginUser(email: String, password: String): User? {
        return userDao.loginUser(email, password)
    }
}