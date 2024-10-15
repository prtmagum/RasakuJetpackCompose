package com.dicoding.rasakuapp.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object Favorite : Screen("favorite")
    object DetailRasaku : Screen("home/{rasakuId}") {
        fun createRoute(rasakuId: Long) = "home/$rasakuId"
    }
}