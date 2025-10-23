package com.github.georgeh1998.simplesplit.feature.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.github.georgeh1998.simplesplit.feature.expenseList.ExpenseListScreen
import com.github.georgeh1998.simplesplit.feature.expenseList.ExpenseListViewModel
import com.github.georgeh1998.simplesplit.feature.initial.InitialScreen
import com.github.georgeh1998.simplesplit.feature.signup.SignUpScreen
import com.github.georgeh1998.simplesplit.feature.signup.SignUpViewModel
import com.github.georgeh1998.simplesplit.feature.waitingConfirmation.WaitingConfirmationScreen
import com.github.georgeh1998.simplesplit.feature.waitingConfirmation.WaitingConfirmationViewModel
import com.github.georgeh1998.simplesplit.repository.UserRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun NavRoot() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.Initial,
    ) {
        composable<Route.Initial>(
            deepLinks =
                listOf(
                    navDeepLink {
                        this.uriPattern = "simplisplit://georgeh1998-github-com/signUpComplete?code={code}"
                    },
                ),
        ) {
            InitialScreen(navController)
        }
        composable<Route.SignUp> {
            val viewModel: SignUpViewModel = koinInject()
            SignUpScreen(
                viewModel = viewModel,
                modifier = Modifier,
            )
        }
        composable<Route.WaitingForConfirmation> {
            val viewModel: WaitingConfirmationViewModel = koinInject()
            WaitingConfirmationScreen(
                viewModel = viewModel,
            )
        }
        composable<Route.ExpenseList> { navBackStackEntry ->
            val viewModel: ExpenseListViewModel = koinInject()
            ExpenseListScreen(
                viewModel = viewModel,
                modifier = Modifier,
            )
        }
    }
}
