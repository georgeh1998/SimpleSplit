package com.github.georgeh1998.simplesplit.repository

import com.github.georgeh1998.simplesplit.data.SupabaseService
import com.github.georgeh1998.simplesplit.util.Log

class TransactionRepository(
    private val supabaseService: SupabaseService,
) {
    suspend fun getTransactionsByGroupId(groupId: String) {
        try {
            Log.i("hasegawa", "getTransactionsByGroupId1")
            val transactions = supabaseService.query()
            Log.i("hasegawa", "getTransactionsByGroupId2")
            transactions.forEach {
                Log.i("hasegawa", "getTransactionsByGroupId: $it")
            }
        } catch (e: Exception) {
            Log.i("hasegawa", "getTransactionsByGroupId: ${e.message}")
        }
    }
}
