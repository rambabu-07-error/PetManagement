package com.pawkeeperdev.activities.dashboard

import androidx.lifecycle.ViewModel
import com.pawkeeperdev.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(private val userRepository: UserRepository):ViewModel()  {
    
}