package com.github.georgeh1998.simplesplit.feature.signup

data class SignUpUiState(
    val inputtingEmail: String = "",
    val inputtingPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
