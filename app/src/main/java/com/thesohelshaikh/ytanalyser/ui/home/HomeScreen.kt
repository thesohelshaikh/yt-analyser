package com.thesohelshaikh.ytanalyser.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.UtilitiesManger
import com.thesohelshaikh.ytanalyser.ui.details.DetailsScreen

@Composable
fun MyApp(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    MaterialTheme {
        AppNavHost(navController = navController, startDestination = startDestination)
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Home.route) {
            MainScreen(
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
    }
}


@Composable
fun MainScreen(onClickAnalyse: (String) -> Unit) {
    Scaffold(
        topBar = {
            HomeTopAppBar()
        },
        bottomBar = {
            HomeBottomBar()
        }
    ) { contentPadding ->
        HomeContent(contentPadding, onClickAnalyse)
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeTopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(text = "YT Analyser", color = Color.White)
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorResource(
                id = R.color.colorPrimary
            )
        )
    )
}

@Composable
private fun HomeBottomBar() {
    var selectedItem by remember { mutableStateOf(0) }
    NavigationBar {
        homeTabs.forEachIndexed { index, tab ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = stringResource(id = tab.title)
                    )
                },
                label = {
                    Text(text = stringResource(id = tab.title))
                }
            )
        }
    }
}

@Composable
private fun HomeContent(
    contentPadding: PaddingValues,
    onClickAnalyse: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
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
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = {
                if (urlInput.isBlank() || urlInput.isEmpty()) return@Button


                val videoId = UtilitiesManger.getIDfromURL(urlInput)
                onClickAnalyse(videoId)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.button_analyse))
        }
    }
}


@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen {}
}

sealed class Screen(val route: String, @StringRes val title: Int) {
    object Home : Screen("home", R.string.screen_home)
    object Details : Screen("details/{videoId}", R.string.screen_details)
}

val homeTabs = listOf(
    Screen.Home
)