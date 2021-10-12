package com.amped94.ffbtracker.data.model.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amped94.ffbtracker.data.model.db.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun getUser(id: Int): User?

    @Query("SELECT * FROM User WHERE username = :username")
    suspend fun getUser(username: String): User?

    @Update
    suspend fun updateUser(user: User)

    @Insert
    suspend fun insert(vararg users: User)

    @Delete
    suspend fun delete(user: User)
}