package com.github.georgeh1998.simplesplit.data

import com.github.georgeh1998.simplesplit.data.dto.TransactionDto
import com.github.georgeh1998.simplesplit.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow

class SupabaseServiceImpl(
    private val supabaseClient: SupabaseClient,
) : SupabaseService {
    override val sessionStatus: Flow<SessionStatus>
        get() = supabaseClient.auth.sessionStatus

    override suspend fun signUpWith(
        signUpEmail: String,
        signUpPassword: String,
    ) {
        supabaseClient.auth.signInAnonymously()
        supabaseClient.auth.signUpWith(
            provider = Email,
            redirectUrl = "simplisplit://georgeh1998-github-com/signUpComplete",
        ) {
            email = signUpEmail
            password = signUpPassword
        }
    }

    override suspend fun exchangeCodeForSession(code: String) {
        supabaseClient.auth.exchangeCodeForSession(code)
    }

    override suspend fun query(): List<TransactionDto> {
        val result = supabaseClient.from(SupabaseTable.TRANSACTIONS).select().decodeList<TransactionDto>()
        return result
    }
}
