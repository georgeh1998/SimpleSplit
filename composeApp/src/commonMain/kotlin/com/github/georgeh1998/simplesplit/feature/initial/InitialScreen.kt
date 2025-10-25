package com.github.georgeh1998.simplesplit.feature.initial

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.georgeh1998.simplesplit.feature.navigation.Route
import org.koin.compose.koinInject

@Composable
fun InitialScreen(
    viewModel: InitialViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.action) {
        uiState.action?.let { action ->
            when (action) {
                InitialAction.ToSignUp -> {
                    navController.navigate(Route.SignUp)
                }
                is InitialAction.ToWaitingForConfirmation -> {
                    navController.navigate(Route.WaitingForConfirmation(action.code))
                }
                InitialAction.ToExpanseList -> {
                    navController.navigate(Route.ExpenseList)
                }
                else -> {}
            }
        }
    }
}
