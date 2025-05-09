package com.pawkeeperdev.activities.dashboard.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawkeeperdev.entities.PetDataModel
import com.pawkeeperdev.entities.relations.UserWithPets
import com.pawkeeperdev.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {


    private val _pets = MutableLiveData<ArrayList<PetDataModel>>()
    val pets: LiveData<ArrayList<PetDataModel>> get() = _pets

    private val _likedPets = MutableLiveData<ArrayList<PetDataModel>>()
    val likedPets: LiveData<ArrayList<PetDataModel>> get() = _likedPets

    private val _usersWithPets = MutableLiveData<List<UserWithPets>>()
    val usersWithPets: LiveData<List<UserWithPets>> get() = _usersWithPets



    fun getPetsByUserId(ownerId: Long) {
        viewModelScope.launch {
            try {
                val petList = repository.getPetsByUSerId(ownerId)
                _pets.postValue(petList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getLikedPetsByUserId(ownerId: Long,isLiked : Int) {
        viewModelScope.launch {
            try {
                val petList = repository.getLikedPets(ownerId,isLiked)
                _likedPets.postValue(petList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updatePet(pet: PetDataModel) {
        viewModelScope.launch {
            try {
                repository.updatePetLikeStatus(pet)
            } catch (e: Exception) {
               e.printStackTrace()
            }
        }
    }

    fun usersWithPet() {
        viewModelScope.launch {
            try {
              val list =  repository.getUsersWithPet()
                _usersWithPets.postValue(list)
            } catch (e: Exception) {
               e.printStackTrace()
            }
        }
    }
}