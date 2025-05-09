package com.pawkeeperdev.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.pawkeeperdev.entities.PetDataModel
import com.pawkeeperdev.entities.UserData

data class UserWithPets(
    @Embedded val user: UserData,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val pets: List<PetDataModel>
)
