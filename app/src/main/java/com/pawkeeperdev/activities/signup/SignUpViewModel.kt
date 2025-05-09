package com.pawkeeperdev.activities.signup


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
class SignUpViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {


    private val _signUpResult = MutableLiveData<Result<Long>>()
    val signUpResult: LiveData<Result<Long>> get() = _signUpResult

    fun addUser(user: UserData) = viewModelScope.launch {
        try {
            val existingUser = userRepository.getUserByUserEmail(user.email)
            if (existingUser != null) {
                _signUpResult.value =
                    Result.failure(Throwable("User with this email already exists,please try different email!"))
            } else {
                val userId = userRepository.insertUser(user )
                _signUpResult.value = Result.success(userId)
            }
        } catch (e: Exception) {
            _signUpResult.value = Result.failure(e)
        }
    }

}