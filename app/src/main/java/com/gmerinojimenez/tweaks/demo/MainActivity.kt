package com.gmerinojimenez.tweaks.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gmerinojimenez.tweaks.demo.theme.DebugTweaksTheme
import com.gmerinojimenez.tweak.addTweakGraph

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DebugTweaksTheme {
                val navController = rememberNavController()

                Surface(color = MaterialTheme.colors.background) {
                    Scaffold { innerPadding ->
                        DemoNavHost(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding),
                            initialScreen = "tweaks",
                        )
                    }

                }
            }
        }
    }

    @Composable
    private fun DemoNavHost(
        navController: NavHostController,
        initialScreen: String,
        modifier: Modifier = Modifier,
    ) {
        NavHost(
            navController = navController,
            startDestination = initialScreen,
            modifier = modifier,
        ) {
            addTweakGraph(
                navController = navController,
            )
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DebugTweaksTheme {
        Greeting("Android")
    }
}