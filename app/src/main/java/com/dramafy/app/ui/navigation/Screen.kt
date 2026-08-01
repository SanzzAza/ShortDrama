package com.dramafy.app.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Browse : Screen("browse")
    data object Library : Screen("library")
    data object Detail : Screen("detail/{dramaId}") {
        fun createRoute(dramaId: String) = "detail/$dramaId"
    }
    data object Player : Screen("player/{dramaId}/{episodeIndex}") {
        fun createRoute(dramaId: String, episodeIndex: Int = 0) = "player/$dramaId/$episodeIndex"
    }
}
