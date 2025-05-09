package com.pawkeeperdev.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val userName: String,
    val email: String,
    val dateOfBirth: String,
    val password: String,
    val isLogin: Int = 0,
    var userImage: ByteArray? = null,
    var userBackImage: ByteArray? = null,
)