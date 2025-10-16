package com.github.georgeh1998.simplesplit.data

import io.github.jan.supabase.SupabaseClient

class SupabaseServiceImpl(
    private val supabaseClient: SupabaseClient,
) : SupabaseService
