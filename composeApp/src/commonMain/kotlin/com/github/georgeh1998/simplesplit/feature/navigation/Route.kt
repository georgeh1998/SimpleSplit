package com.github.georgeh1998.simplesplit.feature.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Initial : Route

    @Serializable
    data object SignUp : Route

    @Serializable
    data class SignUpComplete(
        val code: String,
    ) : Route

    @Serializable
    data object ExpenseList : Route
}
