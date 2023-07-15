package com.thesohelshaikh.ytanalyser.ui.home

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.UtilitiesManger
import com.thesohelshaikh.ytanalyser.ui.details.DetailsScreen
import com.thesohelshaikh.ytanalyser.ui.history.HistoryScreen
import com.thesohelshaikh.ytanalyser.ui.settings.SettingsScreen
import com.thesohelshaikh.ytanalyser.ui.theme.AppTheme

@Composable
fun MyApp(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    AppTheme {
        HomeScreen(navController, startDestination)
    }
}


@Composable
fun HomeScreen(navController: NavHostController, startDestination: String) {
    Scaffold(
        topBar = {
            HomeTopAppBar(navController)
        },
        bottomBar = {
            HomeBottomBar(
                navController
            ) { index ->
                navController.navigate(homeTabs[index].first.route) {
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
        AppNavHost(navController, startDestination, contentPadding)
    }
}

@Composable
private fun AppNavHost(
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


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeTopAppBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val shouldShowBack = homeTabs.none { it.first.route == currentDestination?.route }
    val shouldShowSettings = homeTabs.any { it.first.route == currentDestination?.route }

    CenterAlignedTopAppBar(
        title = {
            Text(text = "YT Analyser", color = Color.White)
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            AnimatedVisibility(
                visible = shouldShowBack,
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally()
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.cd_back),
                        tint = Color.White
                    )
                }
            }
        },
        actions = {
            AnimatedVisibility(
                visible = shouldShowSettings,
                enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
                exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it })
            ) {
                IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(id = R.string.screen_settings),
                        tint = Color.White,
                    )
                }
            }
        }
    )
}

@Composable
private fun HomeBottomBar(
    navController: NavHostController,
    onBottomTabClick: (Int) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val shouldShowBottomBar = homeTabs.any { it.first.route == currentDestination?.route }
    AnimatedVisibility(
        visible = shouldShowBottomBar,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        NavigationBar {
            homeTabs.forEachIndexed { index, tab ->
                NavigationBarItem(
                    selected =
                    currentDestination?.hierarchy?.any { it.route == tab.first.route } == true,
                    onClick = { onBottomTabClick(index) },
                    icon = {
                        Icon(
                            imageVector = tab.second,
                            contentDescription = stringResource(id = tab.first.title)
                        )
                    },
                    label = {
                        Text(text = stringResource(id = tab.first.title))
                    }
                )
            }
        }
    }

}

@Composable
private fun HomeContent(
    onClickAnalyse: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = stringResource(
                id = R.string.cd_app_logo
            ),
            modifier = Modifier
                .padding(16.dp)
                .size(200.dp)
        )

        var urlInput by remember { mutableStateOf("") }

        OutlinedTextField(
            value = urlInput,
            onValueChange = { urlInput = it },
            label = { Text(stringResource(id = R.string.hint_playlist_id_video_id_url)) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            trailingIcon = {
                if (urlInput.isEmpty()) return@OutlinedTextField

                Icon(
                    imageVector = Icons.Outlined.Cancel,
                    contentDescription = "Clear text",
                    modifier = Modifier.clickable { urlInput = "" }
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(
                onGo = {
                    val videoId = validateUrl(urlInput) ?: return@KeyboardActions
                    onClickAnalyse(videoId)
                }
            )
        )

        Button(
            onClick = {
                val videoId = validateUrl(urlInput) ?: return@Button
                onClickAnalyse(videoId)
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.5f)
        ) {
            Text(text = stringResource(id = R.string.button_analyse))
        }
    }
}

fun validateUrl(urlInput: String): String? {
    if (urlInput.isBlank() || urlInput.isEmpty()) return null
    return UtilitiesManger.getIDfromURL(urlInput)
}


@Preview
@Composable
fun PreviewMainScreen() {
    HomeContent(onClickAnalyse = {})
}

sealed class Screen(val route: String, @StringRes val title: Int) {
    object Home : Screen("home", R.string.screen_home)
    object Details : Screen("details/{videoId}", R.string.screen_details)
    object History : Screen("history", R.string.screen_history)
    object Settings : Screen("settings", R.string.screen_settings)
}

val homeTabs = listOf(
    Pair(Screen.Home, Icons.Filled.Home),
    Pair(Screen.History, Icons.Filled.History)
)