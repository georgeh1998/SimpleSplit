package com.github.georgeh1998.simplesplit.feature.waitingConfirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.georgeh1998.simplesplit.repository.UserRepository
import kotlinx.coroutines.launch

class WaitingConfirmationViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    fun exchangeCodeForSession(code: String) {
        viewModelScope.launch {
            userRepository.exchangeCodeForSession(code)
        }
    }
}
