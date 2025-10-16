package com.github.georgeh1998.simplesplit.di

import com.github.georgeh1998.simplesplit.data.dataModule
import com.github.georgeh1998.simplesplit.feature.viewModelModule
import com.github.georgeh1998.simplesplit.repository.repositoryModule
import org.koin.core.context.startKoin

fun initKoin() =
    startKoin {
        modules(viewModelModule, repositoryModule, dataModule)
    }
