package com.github.georgeh1998.simplesplit.feature.initial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.georgeh1998.simplesplit.repository.UserRepository
import com.github.georgeh1998.simplesplit.util.Log
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class InitialViewModel(
    userRepository: UserRepository,
) : ViewModel() {
    val uiState =
        userRepository.sessionStatus
            .map { sessionStatus ->
                Log.i("hasegawa", "sessionStatus: $sessionStatus")
                val action =
                    when (sessionStatus) {
                        is SessionStatus.Authenticated -> {
                            Log.i("hasegawa", "${sessionStatus.session}")
                            InitialAction.ToExpanseList
                        }
                        is SessionStatus.NotAuthenticated -> InitialAction.ToSignUp
                        else -> null
                    }
                InitialUiState(action)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = InitialUiState(null),
            )
}
