package com.pawkeeperdev.activities.signIn

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
class SignInViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _signInResult = MutableLiveData<Result<UserData>>()
    val signInResult: LiveData<Result<UserData>> get() = _signInResult


    fun signInUser(email: String, password: String) = viewModelScope.launch {
        try {
            val user = userRepository.verifyLoginUser(email, password)
            if (user != null) {
                _signInResult.value = Result.success(user)
                userRepository.updateLogInStatus(user.id, isLogin = 1)
            } else {
                _signInResult.value = Result.failure(Throwable("Invalid Credentials!"))
            }
        } catch (e: Exception) {
            _signInResult.value = Result.failure(e)
        }
    }
}