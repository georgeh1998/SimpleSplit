package com.github.georgeh1998.simplesplit.repository

import com.github.georgeh1998.simplesplit.data.SupabaseService

class UserRepository(
    private val supabaseService: SupabaseService,
) {
    suspend fun signUpWithEmail(
        email: String,
        password: String,
    ) {
        supabaseService.signUpWith(
            signUpEmail = email,
            signUpPassword = password,
        )
    }
}
