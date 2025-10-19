package com.github.georgeh1998.simplesplit.feature

import com.github.georgeh1998.simplesplit.feature.expenseList.ExpenseListViewModel
import com.github.georgeh1998.simplesplit.feature.signup.SignUpViewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        factory {
            SignUpViewModel(get(), get())
        }
        factory {
            ExpenseListViewModel()
        }
    }
