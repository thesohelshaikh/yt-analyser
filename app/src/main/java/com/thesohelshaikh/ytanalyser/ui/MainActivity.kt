package com.thesohelshaikh.ytanalyser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.databinding.ActivityMainBinding
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.thesohelshaikh.ytanalyser.UtilitiesManger
import com.thesohelshaikh.ytanalyser.ui.details.DetailsScreen


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContent {
            MaterialTheme {
                MyApp()
            }
        }

        setupToolbar()

    }

    private fun setupToolbar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    @Composable
    fun MyApp(
        navController: NavHostController = rememberNavController(),
        startDestination: String = Screen.Home.route
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
        Column(
            modifier = Modifier.fillMaxSize(),
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

    enum class Screen(val route: String) {
        Home("home"),
        Details("details/{videoId}")
    }

}