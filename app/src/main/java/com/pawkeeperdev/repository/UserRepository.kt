package com.pawkeeperdev.repository

import com.pawkeeperdev.dao.PetDao
import com.pawkeeperdev.dao.UserDao
import com.pawkeeperdev.entities.PetDataModel
import com.pawkeeperdev.entities.UserData
import com.pawkeeperdev.entities.relations.UserWithPets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao, private val petDao : PetDao) {

    suspend fun getUsers(): List<UserData> {
        return withContext(Dispatchers.IO) {
            userDao.getUsers()
        }
    }


    suspend fun insertUser(user: UserData) : Long {
       return withContext(Dispatchers.IO) {
            userDao.insertUser(user)
        }
    }

    suspend fun getUserByUserEmail(userEmail: String): UserData? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByUserEmail(userEmail)
        }
    }

    suspend fun verifyLoginUser(email: String, password: String): UserData? {
        return withContext(Dispatchers.IO) {
            userDao.verifyLoginUser(email, password)
        }
    }

    suspend fun updateLogInStatus(id: Long, isLogin: Int): Int {
        return withContext(Dispatchers.IO) {
            userDao.updateLogInStatus(id, isLogin)
        }
    }

    suspend fun getLoggedInUsers(id: Long): UserData? {
        return withContext(Dispatchers.IO) {
            userDao.getLoggedInUsers(id)
        }
    }

    suspend fun getLoggedInUsersLogInS(): UserData? {
        return withContext(Dispatchers.IO) {
            userDao.getLoggedInUsersLogInS()
        }
    }

    suspend fun delete(id: Long) {
        return withContext(Dispatchers.IO) {
            userDao.delete(id)
        }
    }


    suspend fun addPet(pet: PetDataModel){
        return withContext(Dispatchers.IO){
            petDao.insertPet(pet)
        }
    }

    suspend fun getPetsByUSerId(ownerId : Long):ArrayList<PetDataModel>{
        return withContext(Dispatchers.IO){
            val petsList: List<PetDataModel> = petDao.getPetsByUserId(ownerId)
            ArrayList(petsList)
        }
    }

    suspend fun updatePetLikeStatus(pet: PetDataModel){
        return withContext(Dispatchers.IO){
            petDao.updatePet(pet)
        }
    }

    suspend fun updateUser(user: UserData){
        return withContext(Dispatchers.IO){
            userDao.updateUser(user)
        }
    }

    suspend fun getLikedPets(ownerId : Long, isLiked: Int):ArrayList<PetDataModel>{
        return withContext(Dispatchers.IO){
            val petsList: List<PetDataModel> = petDao.getLikedPetsByUserId(ownerId,isLiked)
            ArrayList(petsList)
        }
    }

    suspend fun getUsersWithPet():List<UserWithPets>{
        return withContext(Dispatchers.IO){
            userDao.getUsersWithPets()
        }
    }
}