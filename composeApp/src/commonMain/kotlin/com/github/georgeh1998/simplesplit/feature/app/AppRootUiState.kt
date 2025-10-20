package com.github.georgeh1998.simplesplit.feature.app

data class AppRouteUiState(
    val initialRoute: InitialRoute = InitialRoute.WAITING,
)

enum class InitialRoute {
    WAITING,
    SIGN_UP,
    MAIL_CONFIRMATION,
    EXPENSE_LIST,
}
