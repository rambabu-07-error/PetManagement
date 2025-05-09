package com.pawkeeperdev.activities.splashscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawkeeperdev.entities.UserData
import com.pawkeeperdev.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(val userRepository: UserRepository): ViewModel(){
    val userData = MutableLiveData<UserData?>()

    fun getWhoAlreadyLogIn() = viewModelScope.launch {
        try {
            val user = userRepository.getLoggedInUsersLogInS()
            userData.value = user
        } catch (e: Exception) {
            userData.value = null
        }
    }
}