package com.thesohelshaikh.ytanalyser.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.thesohelshaikh.ytanalyser.data.model.UserData
import com.thesohelshaikh.ytanalyser.ui.details.DetailsScreen
import com.thesohelshaikh.ytanalyser.ui.history.HistoryScreen
import com.thesohelshaikh.ytanalyser.ui.settings.SettingsScreen
import com.thesohelshaikh.ytanalyser.ui.theme.AppTheme

@Composable
fun MyApp(
    receivedUrl: String?,
    userData: UserData,
    navController: NavHostController = rememberNavController(),
    startDestination: Screen = Screen.Home
) {
    AppTheme(userData.shouldUseDarkTheme()) {
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
    startDestination: Screen,
) {
    Scaffold(
        topBar = {
            HomeTopAppBar(navController)
        },
        bottomBar = {
            HomeBottomBar(
                navController
            ) { index ->
                navController.navigate(homeTabs[index].screen) {
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
    startDestination: Screen,
    contentPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(contentPadding)
    ) {
        composable<Screen.Home> {
            HomeContent(
                receivedUrl = receivedUrl,
                onClickAnalyse = {
                    navController.navigate(Screen.Details(it))
                },
                onViewHistory = {
                    navController.navigate(Screen.History)
                }
            )
        }
        composable<Screen.Details> { backStackEntry ->
            DetailsScreen(
                backStackEntry.toRoute<Screen.Details>().videoId
            )
        }
        composable<Screen.History> {
            HistoryScreen(onVideoClick = {
                navController.navigate(Screen.Details(it))
            })
        }
        composable<Screen.Settings> {
            SettingsScreen()
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            receivedUrl = "",
            navController = rememberNavController(),
            startDestination = Screen.Home
        )
    }
}