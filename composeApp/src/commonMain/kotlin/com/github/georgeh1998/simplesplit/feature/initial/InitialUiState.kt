package com.github.georgeh1998.simplesplit.feature.initial

data class InitialUiState(
    val action: InitialAction? = null,
)

enum class InitialAction {
    ToSignUp,
    ToWaitingForConfirmation,
    ToExpanseList,
}
