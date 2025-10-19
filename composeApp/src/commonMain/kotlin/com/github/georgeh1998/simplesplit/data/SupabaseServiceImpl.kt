package com.github.georgeh1998.simplesplit.data

import com.github.georgeh1998.simplesplit.data.dto.TransactionDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from

class SupabaseServiceImpl(
    private val supabaseClient: SupabaseClient,
) : SupabaseService {
    override suspend fun signUpWith(
        signUpEmail: String,
        signUpPassword: String,
    ) {
        val result =
            supabaseClient.auth.signUpWith(Email) {
                email = signUpEmail
                password = signUpPassword
            }
    }

    override suspend fun query(): List<TransactionDto> =
        supabaseClient.from(SupabaseTable.TRANSACTIONS).select().decodeList<TransactionDto>()
}
