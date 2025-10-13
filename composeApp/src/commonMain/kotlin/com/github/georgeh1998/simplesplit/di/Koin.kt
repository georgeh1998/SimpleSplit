package com.github.georgeh1998.simplesplit.di

import org.koin.core.context.startKoin

fun initKoin() =
    startKoin {
        modules(supabaseModule)
    }
