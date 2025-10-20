package com.github.georgeh1998.simplesplit.repository

import com.github.georgeh1998.simplesplit.data.SupabaseService
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val supabaseService: SupabaseService,
) {
    val sessionStatus: Flow<SessionStatus> = supabaseService.sessionStatus

    suspend fun signUpWithEmail(
        email: String,
        password: String,
    ) {
        supabaseService.signUpWith(
            signUpEmail = email,
            signUpPassword = password,
        )
    }

    suspend fun exchangeCodeForSession(code: String) {
        supabaseService.exchangeCodeForSession(code)
    }
}
