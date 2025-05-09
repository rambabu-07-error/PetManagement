package com.pawkeeperdev.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pets",
    foreignKeys = [ForeignKey(
        entity = UserData::class,
        parentColumns = ["id"],
        childColumns = ["ownerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["ownerId"])]
)
data class PetDataModel(
    @PrimaryKey(autoGenerate = true)
    val petId: Long = 0,
    val ownerId: Long,
    val petName: String,
    val petType: String,
    val petAdoptDate: String,
    val petImage : ByteArray,
    val aboutPet : String?="",
    var isLiked : Int = 0
)
