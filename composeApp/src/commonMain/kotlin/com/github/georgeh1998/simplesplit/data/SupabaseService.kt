package com.github.georgeh1998.simplesplit.data

import com.github.georgeh1998.simplesplit.data.dto.TransactionDto

interface SupabaseService {
    suspend fun signUpWith(
        signUpEmail: String,
        signUpPassword: String,
    )

    suspend fun query(): List<TransactionDto>
}
