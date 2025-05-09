package com.pawkeeperdev.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pawkeeperdev.entities.UserData
import com.pawkeeperdev.entities.relations.UserWithPets

@Dao
interface UserDao {

    //for add new users
    @Insert
    suspend fun insertUser(user: UserData): Long

    //for update
    @Update
    suspend fun updateUser(user: UserData)

    //for get all users
    @Query("SELECT * FROM users ORDER BY id ASC")
    suspend fun getUsers(): List<UserData>

    //check userEmail validation
    @Query("SELECT * FROM users WHERE email = :userEmail")
    suspend fun getUserByUserEmail(userEmail: String): UserData?

    //for authenticate user
    @Query("SELECT * FROM users WHERE email = :email AND password = :password ")
    suspend fun verifyLoginUser(email: String, password: String): UserData?

    //for update logIn status
    @Query("UPDATE users SET isLogin = :isLogin WHERE id = :id")
    suspend fun updateLogInStatus(id: Long, isLogin: Int): Int

    //get the users who have already logIn
    @Query("SELECT * FROM users WHERE isLogin = 1 OR id = :id")
    suspend fun getLoggedInUsers(id: Long): UserData?

    //get the users who have already logIn
    @Query("SELECT * FROM users WHERE isLogin = 1 ")
    suspend fun getLoggedInUsersLogInS(): UserData?

    //Delete specific user
    @Query("DELETE FROM users WHERE id=:id ")
    suspend fun delete(id: Long)

    //get User's Pets
    @Transaction
    @Query("SELECT * FROM users")
    suspend fun getUsersWithPets(): List<UserWithPets>

}