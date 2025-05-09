package com.pawkeeperdev.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pawkeeperdev.entities.PetDataModel

@Dao
interface PetDao {

    @Insert
    suspend fun insertPet(pet: PetDataModel)

    @Query("SELECT * FROM pets WHERE ownerId = :ownerId")
    suspend fun getPetsByUserId(ownerId: Long): List<PetDataModel>

    @Query("SELECT * FROM pets WHERE ownerId = :ownerId AND isLiked = :isLiked ")
    suspend fun getLikedPetsByUserId(ownerId: Long, isLiked : Int): List<PetDataModel>

    @Update
    suspend fun updatePet(pet: PetDataModel)
}