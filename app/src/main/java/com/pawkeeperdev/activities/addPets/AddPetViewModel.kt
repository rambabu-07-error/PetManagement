package com.pawkeeperdev.activities.addPets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawkeeperdev.entities.PetDataModel
import com.pawkeeperdev.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(val repository: UserRepository) : ViewModel(){
    private val _addPetResult = MutableLiveData<Result<String>>()
    val addPetResult: LiveData<Result<String>> get() = _addPetResult

    fun addPet(pet: PetDataModel) = viewModelScope.launch {
        try {
            repository.addPet(pet)
            _addPetResult.value = Result.success("${pet.petName} added to Your List")
        } catch (e: Exception) {
            _addPetResult.value = Result.failure(e)
        }
    }

}