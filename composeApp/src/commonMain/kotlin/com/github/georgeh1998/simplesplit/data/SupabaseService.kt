package com.github.georgeh1998.simplesplit.data

interface SupabaseService {
    suspend fun signUpWith(
        signUpEmail: String,
        signUpPassword: String,
    )
}
