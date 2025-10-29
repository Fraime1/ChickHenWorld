package com.chikegam.henwoldir.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chikegam.henwoldir.ui.screens.*
import com.chikegam.henwoldir.ui.viewmodel.BreedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: BreedViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute !in listOf(Screen.BreedDetail.route)) {
                NavigationBar {
                    BottomNavItem.items.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToBreed = { breedId ->
                        navController.navigate(Screen.BreedDetail.createRoute(breedId))
                    }
                )
            }
            composable(Screen.Cards.route) {
                CardsScreen(
                    viewModel = viewModel,
                    onNavigateToBreed = { breedId ->
                        navController.navigate(Screen.BreedDetail.createRoute(breedId))
                    }
                )
            }
            composable(Screen.Collection.route) {
                CollectionScreen(
                    viewModel = viewModel,
                    onNavigateToBreed = { breedId ->
                        navController.navigate(Screen.BreedDetail.createRoute(breedId))
                    }
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    viewModel = viewModel,
                    onNavigateToBreed = { breedId ->
                        navController.navigate(Screen.BreedDetail.createRoute(breedId))
                    }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(Screen.BreedDetail.route) { backStackEntry ->
                val breedId = backStackEntry.arguments?.getString("breedId")
                breedId?.let {
                    BreedDetailScreen(
                        breedId = it,
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

