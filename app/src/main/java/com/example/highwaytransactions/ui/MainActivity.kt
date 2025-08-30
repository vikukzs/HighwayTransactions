package com.example.highwaytransactions.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.highwaytransactions.presentation.MainViewModel
import com.example.highwaytransactions.ui.model.Screen
import com.example.highwaytransactions.ui.model.UiStates
import com.example.highwaytransactions.ui.screens.ConfirmScreen
import com.example.highwaytransactions.ui.screens.CountyVignettesScreen
import com.example.highwaytransactions.ui.screens.ErrorScreen
import com.example.highwaytransactions.ui.screens.LoadingScreen
import com.example.highwaytransactions.ui.screens.MainScreen
import com.example.highwaytransactions.ui.screens.SuccessScreen
import com.example.highwaytransactions.ui.theme.HighwayTransactionsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HighwayTransactionsTheme {
                val mainViewModel by viewModels<MainViewModel>()

                val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

                when (uiState) {
                    UiStates.Loading -> LoadingScreen()
                    is UiStates.Error -> ErrorScreen((uiState as UiStates.Error).message)
                    UiStates.Success -> {
                        Navigation()
                    }
                }
            }
        }
    }
}

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.LandingScreen
    ) {
        composable<Screen.LandingScreen> { backstackEntry ->
            MainScreen(
                onGoToConfirmScreen = { chosenId ->
                    navController.navigate(
                        Screen.ConfirmationScreen(
                            chosenTypes = listOf(chosenId),
                            isCountryVignette = true
                        )
                    )
                },
                onGoToCountyScreen = {
                    navController.navigate(Screen.CountyVignettesScreen)
                }
            )
        }
        composable<Screen.CountyVignettesScreen> { backstackEntry ->
            CountyVignettesScreen(
                onGoToNextScreen = { chosenIds ->
                    navController.navigate(
                        Screen.ConfirmationScreen(
                            chosenTypes = chosenIds,
                            isCountryVignette = false
                        )
                    )
                },
                onGoBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.ConfirmationScreen> { backStackEntry ->
            val confirmScreen: Screen.ConfirmationScreen = backStackEntry.toRoute()

            ConfirmScreen(
                chosenIds = confirmScreen.chosenTypes,
                isCountryVignette = confirmScreen.isCountryVignette,
                onGoToNextScreen = {
                    navController.navigate(Screen.SuccessScreen)
                },
                onGoBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.SuccessScreen> {
            SuccessScreen(
                onGoBack = {
                    navController.navigate(
                        route = Screen.LandingScreen
                    ) {
                        popUpTo(navController.graph.startDestinationId)
                    }
                }
            )
        }
    }
}


