package com.chikegam.henwoldir.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Cards : Screen("cards")
    object Collection : Screen("collection")
    object Search : Screen("search")
    object Settings : Screen("settings")
    object BreedDetail : Screen("breed_detail/{breedId}") {
        fun createRoute(breedId: String) = "breed_detail/$breedId"
    }
}

