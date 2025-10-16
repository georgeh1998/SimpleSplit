package com.github.georgeh1998.simplesplit.feature.signup

import androidx.lifecycle.ViewModel
import com.github.georgeh1998.simplesplit.repository.UserRepository

class SignUpViewModel(
    private val userRepository: UserRepository,
) : ViewModel()
