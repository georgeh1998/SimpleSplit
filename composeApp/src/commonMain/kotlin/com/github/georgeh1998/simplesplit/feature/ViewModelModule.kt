package com.github.georgeh1998.simplesplit.feature

import com.github.georgeh1998.simplesplit.feature.expenseList.ExpenseListViewModel
import com.github.georgeh1998.simplesplit.feature.initial.InitialViewModel
import com.github.georgeh1998.simplesplit.feature.signup.SignUpViewModel
import com.github.georgeh1998.simplesplit.feature.waitingConfirmation.WaitingConfirmationViewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        factory {
            InitialViewModel(get())
        }
        factory {
            SignUpViewModel(get())
        }
        factory {
            WaitingConfirmationViewModel(get())
        }
        factory {
            ExpenseListViewModel(get(), get())
        }
    }
