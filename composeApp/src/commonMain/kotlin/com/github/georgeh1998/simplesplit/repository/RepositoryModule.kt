package com.github.georgeh1998.simplesplit.repository

import org.koin.dsl.module

val repositoryModule =
    module {
        single {
            UserRepository(get())
        }
    }
