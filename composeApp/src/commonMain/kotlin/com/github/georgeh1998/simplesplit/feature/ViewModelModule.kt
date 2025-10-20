package com.github.georgeh1998.simplesplit.feature

import com.github.georgeh1998.simplesplit.feature.app.AppRootViewModel
import com.github.georgeh1998.simplesplit.feature.expenseList.ExpenseListViewModel
import com.github.georgeh1998.simplesplit.feature.navigation.SignUpCompleteViewModel
import com.github.georgeh1998.simplesplit.feature.signup.SignUpViewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        factory {
            AppRootViewModel(get())
        }
        factory {
            AppRootViewModel(get())
        }
        factory {
            SignUpViewModel(get())
        }
        factory {
            SignUpCompleteViewModel(get())
        }
        factory {
            ExpenseListViewModel(get(), get())
        }
    }
