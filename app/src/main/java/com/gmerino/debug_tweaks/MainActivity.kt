package com.gmerino.debug_tweaks

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
import com.gmerino.debug_tweaks.ui.theme.DebugTweaksTheme
import com.gmerino.tweak.addTweakGraph
import com.gmerino.tweak.domain.*

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
                tweaksGraph = demoTweakGraph(),
                navController = navController,
            )
        }
    }

    private fun demoTweakGraph() = TweaksGraph(
        category = listOf(
            TweakCategory(
                "Screen 1", listOf(
                    TweakGroup(
                        "Group 1", listOf(
                            EditableStringTweakEntry(
                                key = "value1",
                                name = "Value 1",
                            ),
                            EditableBooleanTweakEntry(
                                key = "value2",
                                name = "Value 2",
                            ),
                            EditableIntTweakEntry(
                                key = "value3",
                                name = "Value 3",
                            ),
                            EditableLongTweakEntry(
                                key = "value4",
                                name = "Value 4",
                            )
                        )
                    )
                )
            )
        )
    )

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