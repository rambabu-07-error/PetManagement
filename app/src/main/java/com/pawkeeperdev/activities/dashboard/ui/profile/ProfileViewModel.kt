package com.pawkeeperdev.activities.dashboard.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawkeeperdev.entities.UserData
import com.pawkeeperdev.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _logOutResult = MutableLiveData<Result<String>>()
    val logOutResult: LiveData<Result<String>> get() = _logOutResult

    private val _deleteResult = MutableLiveData<Result<String>>()
    val deleteResult: LiveData<Result<String>> get() = _deleteResult

    private val _updateUserResult = MutableLiveData<Result<String>>()
    val updateUserResult: LiveData<Result<String>> get() = _updateUserResult


    private val _user = MutableLiveData<UserData>()
    val user: LiveData<UserData> get() = _user

    fun logOut(id: Long, isLogIn: Int) = viewModelScope.launch {
        try {
            userRepository.updateLogInStatus(id, isLogIn)
            _logOutResult.value = Result.success("Logout Successfully!")
        } catch (e: Exception) {
            _logOutResult.value = Result.failure(Throwable("Something Went Wrong!"))
        }
    }

    fun deleteSpecificUser(userId: Long) = viewModelScope.launch {
        try {
            userRepository.delete(userId)
            _deleteResult.value = Result.success("User Deleted Successfully!")
        } catch (e: Exception) {
            _deleteResult.value = Result.failure(Throwable("Something Went Wrong!"))
        }
    }

    fun getUserDataById(id: Long) = viewModelScope.launch {
        try {
            val user = userRepository.getLoggedInUsers(id)
            _user.postValue(user!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun updateUser(user: UserData) = viewModelScope.launch {
        try {
            userRepository.updateUser(user)
            _updateUserResult.value = Result.success("User updated")
        } catch (e: Exception) {
            e.printStackTrace()
            _updateUserResult.value = Result.failure(Throwable("Something Went Wrong!"))
        }
    }
}