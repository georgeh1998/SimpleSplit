package com.github.georgeh1998.simplesplit.feature.initial

data class InitialUiState(
    val action: InitialAction? = null,
)

sealed interface InitialAction {
    data object ToSignUp : InitialAction

    data class ToWaitingForConfirmation(
        val code: String,
    ) : InitialAction

    data object ToExpanseList
}
