package com.pawkeeperdev.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pawkeeperdev.dao.PetDao
import com.pawkeeperdev.dao.UserDao
import com.pawkeeperdev.entities.PetDataModel
import com.pawkeeperdev.entities.UserData

@Database(entities = [UserData::class, PetDataModel::class], version = 2, exportSchema = false)
abstract class UserDataBase : RoomDatabase() {
    abstract fun UserDao(): UserDao
    abstract fun petDao(): PetDao
}