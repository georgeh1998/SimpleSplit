package com.github.georgeh1998.simplesplit.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

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
}
