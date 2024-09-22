package com.thesohelshaikh.ytanalyser.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.ui.theme.AppTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeTopAppBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val shouldShowBack = homeTabs.none { currentDestination?.hasRoute(it.screen::class) == true }
    val shouldShowSettings = homeTabs.any { currentDestination?.hasRoute(it.screen::class) == true }

    CenterAlignedTopAppBar(
        title = {
            val title = getScreenTitle(currentDestination?.hierarchy?.firstOrNull())?.let {
                stringResource(id = it)
            }
            Text(
                text = title ?: "",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
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
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.cd_back),
                        tint = MaterialTheme.colorScheme.onPrimary
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
                IconButton(onClick = { navController.navigate(Screen.Settings) }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(id = R.string.screen_settings),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    )
}

private fun getScreenTitle(route: NavDestination?): Int? {
    return when {
        null == route -> null
        route.hasRoute<Screen.Home>() -> R.string.app_name
        route.hasRoute<Screen.History>() -> R.string.screen_history
        route.hasRoute<Screen.Settings>() -> R.string.screen_settings
        else -> null
    }
}

@Preview
@Composable
fun HomeTopAppBarPreview() {
    AppTheme {
        HomeTopAppBar(navController = rememberNavController())
    }
}