package com.github.georgeh1998.simplesplit.data

import SimpleSplit.composeApp.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModule =
    module {
        single {
            createSupabaseClient(BuildConfig.SUPABASE_URL, BuildConfig.SUPABASE_KEY) {
                defaultSerializer = KotlinXSerializer(Json)
                install(Auth)
                install(Postgrest)
            }
        }
        single<SupabaseService> {
            SupabaseServiceImpl(get())
        }
    }
