package com.github.georgeh1998.simplesplit.feature.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.georgeh1998.simplesplit.feature.navigation.NavRoot
import com.github.georgeh1998.simplesplit.feature.navigation.Route
import org.koin.compose.koinInject

@Composable
fun AppRoot() {
    val viewModel: AppRootViewModel = koinInject()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.initialRoute != InitialRoute.WAITING) {
        val startDestination =
            when (uiState.initialRoute) {
                InitialRoute.EXPENSE_LIST -> Route.ExpenseList
                InitialRoute.SIGN_UP -> Route.SignUp
                else -> null
            }
        if (startDestination != null) {
            NavRoot(startDestination = startDestination)
        }
    }
}
