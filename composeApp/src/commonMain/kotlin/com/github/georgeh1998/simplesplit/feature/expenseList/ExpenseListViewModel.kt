package com.github.georgeh1998.simplesplit.feature.expenseList

import androidx.lifecycle.ViewModel
import com.github.georgeh1998.simplesplit.repository.TransactionRepository
import com.github.georgeh1998.simplesplit.repository.UserRepository

class ExpenseListViewModel(
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository,
) : ViewModel()
