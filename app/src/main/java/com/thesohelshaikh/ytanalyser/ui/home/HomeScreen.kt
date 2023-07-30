package com.thesohelshaikh.ytanalyser.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.thesohelshaikh.ytanalyser.ui.details.DetailsScreen
import com.thesohelshaikh.ytanalyser.ui.history.HistoryScreen
import com.thesohelshaikh.ytanalyser.ui.settings.SettingsScreen
import com.thesohelshaikh.ytanalyser.ui.theme.AppTheme

@Composable
fun MyApp(
    receivedUrl: String?,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    AppTheme {
        HomeScreen(
            receivedUrl,
            navController,
            startDestination
        )
    }
}

@Composable
fun HomeScreen(
    receivedUrl: String?,
    navController: NavHostController,
    startDestination: String
) {
    Scaffold(
        topBar = {
            HomeTopAppBar(navController)
        },
        bottomBar = {
            HomeBottomBar(
                navController
            ) { index ->
                navController.navigate(homeTabs[index].screen.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
            }
        }
    ) { contentPadding ->
        AppNavHost(receivedUrl, navController, startDestination, contentPadding)
    }
}

@Composable
private fun AppNavHost(
    receivedUrl: String?,
    navController: NavHostController,
    startDestination: String,
    contentPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(contentPadding)
    ) {
        composable(Screen.Home.route) {
            HomeContent(
                receivedUrl,
                onClickAnalyse = {
                    navController.navigate("details/$it")
                }
            )
        }
        composable(
            Screen.Details.route,
            arguments = listOf(
                navArgument("videoId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            DetailsScreen(
                backStackEntry.arguments?.getString("videoId") ?: ""
            )
        }
        composable(Screen.History.route) {
            HistoryScreen(onVideoClick = {
                navController.navigate("details/$it")
            })
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Details : Screen("details/{videoId}")
    object History : Screen("history")
    object Settings : Screen("settings")
}

@Preview
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            receivedUrl = "",
            navController = rememberNavController(),
            startDestination = Screen.Home.route
        )
    }
}