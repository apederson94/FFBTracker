package com.amped94.ffbtracker.data.model.db.dao

import androidx.room.*
import com.amped94.ffbtracker.data.model.db.entity.User
import com.amped94.ffbtracker.data.model.db.entity.UserAndLeagues

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM User WHERE userId = :id")
    suspend fun getUser(id: Long): User?

    @Query("SELECT * FROM User WHERE username = :username")
    suspend fun getUser(username: String): User?

    @Transaction
    @Query("SELECT * FROM User WHERE username = :username")
    suspend fun getUserAndLeagues(username: String): UserAndLeagues?

    @Update
    suspend fun updateUser(user: User)

    @Insert
    suspend fun insert(vararg users: User): List<Long>

    @Delete
    suspend fun delete(user: User)
}