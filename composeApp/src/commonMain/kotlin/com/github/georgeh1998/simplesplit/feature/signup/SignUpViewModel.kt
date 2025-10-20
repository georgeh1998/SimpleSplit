package com.github.georgeh1998.simplesplit.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.georgeh1998.simplesplit.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(inputtingEmail = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(inputtingPassword = password) }
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val user =
                    userRepository.signUpWithEmail(
                        email = _uiState.value.inputtingEmail,
                        password = _uiState.value.inputtingPassword,
                    )
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }

    fun signUpComplete(code: String) {
        viewModelScope.launch {
            userRepository.exchangeCodeForSession(code)
        }
    }
}
