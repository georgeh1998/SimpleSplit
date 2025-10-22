package com.github.georgeh1998.simplesplit.feature.initial

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.georgeh1998.simplesplit.feature.navigation.Route
import org.koin.compose.koinInject

@Composable
fun InitialScreen(navController: NavController) {
    val viewModel: InitialViewModel = koinInject()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.action) {
        uiState.action?.let { action ->
            when (action) {
                InitialAction.ToSignUp -> {
                    navController.navigate(Route.SignUp)
                }
                InitialAction.ToExpanseList -> {
                    navController.navigate(Route.ExpenseList)
                }
                else -> {}
            }
        }
    }
}
