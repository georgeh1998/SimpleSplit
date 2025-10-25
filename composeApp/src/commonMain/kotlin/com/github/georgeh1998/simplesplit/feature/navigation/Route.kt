package com.github.georgeh1998.simplesplit.feature.navigation

import kotlinx.serialization.Serializable


sealed interface Route {
    @Serializable
    data class Initial(
        val code: String? = null
    ) : Route

    @Serializable
    data object SignUp : Route

    @Serializable
    data class WaitingForConfirmation(
        val code: String,
    ) : Route

    @Serializable
    data object ExpenseList : Route
}
