package com.dramafy.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dramafy.app.ui.screens.bookmall.BrowseScreen
import com.dramafy.app.ui.screens.detail.DetailScreen
import com.dramafy.app.ui.screens.home.HomeScreen
import com.dramafy.app.ui.screens.library.LibraryScreen
import com.dramafy.app.ui.screens.player.PlayerScreen
import com.dramafy.app.ui.screens.search.SearchScreen

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onDramaClick = { dramaId ->
                    navController.navigate(Screen.Detail.createRoute(dramaId))
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onDramaClick = { dramaId ->
                    navController.navigate(Screen.Detail.createRoute(dramaId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Browse.route) {
            BrowseScreen(
                onDramaClick = { dramaId ->
                    navController.navigate(Screen.Detail.createRoute(dramaId))
                }
            )
        }

        composable(Screen.Library.route) {
            LibraryScreen()
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("dramaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val dramaId = backStackEntry.arguments?.getString("dramaId") ?: ""
            DetailScreen(
                dramaId = dramaId,
                onBack = { navController.popBackStack() },
                onPlayClick = { episodeIndex ->
                    navController.navigate(Screen.Player.createRoute(dramaId, episodeIndex))
                }
            )
        }

        composable(
            route = Screen.Player.route,
            arguments = listOf(
                navArgument("dramaId") { type = NavType.StringType },
                navArgument("episodeIndex") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val dramaId = backStackEntry.arguments?.getString("dramaId") ?: ""
            val episodeIndex = backStackEntry.arguments?.getInt("episodeIndex") ?: 0
            PlayerScreen(
                dramaId = dramaId,
                startEpisode = episodeIndex,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
