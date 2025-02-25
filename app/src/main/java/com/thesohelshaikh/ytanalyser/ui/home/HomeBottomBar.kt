package com.thesohelshaikh.ytanalyser.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.thesohelshaikh.ytanalyser.R

@Composable
fun HomeBottomBar(
    navController: NavHostController,
    onBottomTabClick: (Int) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val shouldShowBottomBar =
        homeTabs.any { currentDestination?.hasRoute(it.screen::class) == true }
    AnimatedVisibility(
        visible = shouldShowBottomBar,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = spring(
                stiffness = Spring.StiffnessHigh,
                visibilityThreshold = IntOffset.VisibilityThreshold
            )
        ),
    ) {
        NavigationBar {
            homeTabs.forEachIndexed { index, tab ->
                NavigationBarItem(
                    selected =
                    currentDestination?.hierarchy?.any { it.hasRoute(tab.screen::class) } == true,
                    onClick = { onBottomTabClick(index) },
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = stringResource(tab.title)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = tab.title),
                            style = MaterialTheme.typography.bodyMedium
                                .copy(fontWeight = FontWeight.Bold)
                        )
                    }
                )
            }
        }
    }

}

val homeTabs = listOf(
    BottomTab(Screen.Home, Icons.Filled.Home, R.string.screen_home),
    BottomTab(Screen.History, Icons.Filled.History, R.string.screen_history)
)

data class BottomTab(
    val screen: Screen,
    val icon: ImageVector,
    val title: Int,
)