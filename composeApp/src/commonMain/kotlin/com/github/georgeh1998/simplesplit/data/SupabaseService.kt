package com.github.georgeh1998.simplesplit.data

import com.github.georgeh1998.simplesplit.data.dto.TransactionDto
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

interface SupabaseService {
    val sessionStatus: Flow<SessionStatus>

    suspend fun signUpWith(
        signUpEmail: String,
        signUpPassword: String,
    )

    suspend fun exchangeCodeForSession(code: String)

    suspend fun query(): List<TransactionDto>
}
