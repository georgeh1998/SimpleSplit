package com.github.georgeh1998.simplesplit.feature.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.georgeh1998.simplesplit.repository.UserRepository
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AppRootViewModel(
    userRepository: UserRepository,
) : ViewModel() {
    private val sessionStatus = userRepository.sessionStatus

    val uiState =
        sessionStatus
            .map {
                val initialRoute =
                    when (it) {
                        is SessionStatus.Authenticated -> InitialRoute.EXPENSE_LIST
                        is SessionStatus.NotAuthenticated -> InitialRoute.SIGN_UP
                        else -> InitialRoute.WAITING
                    }
                AppRouteUiState(
                    initialRoute = initialRoute,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AppRouteUiState(),
            )
}
