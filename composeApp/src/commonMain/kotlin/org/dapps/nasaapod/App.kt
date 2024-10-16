package org.dapps.nasaapod

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.dapps.nasaapod.presentation.ApodStack
import org.dapps.nasaapod.presentation.ApodView
import org.dapps.nasaapod.presentation.ApodViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        KoinContext {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "home"
            )
            {
                composable(route = "home") {
                    ApodStack(
                        viewModel = koinViewModel<ApodViewModel>()
                    )
                }
            }
        }
    }
}