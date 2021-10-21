[![Platform](https://img.shields.io/badge/Platform-Android-brightgreen)](https://github.com/gmerinojimenez/tweaks)
[![Version](https://maven-badges.herokuapp.com/maven-central/io.github.gmerinojimenez/tweaks/badge.png)](https://search.maven.org/artifact/io.github.gmerinojimenez/tweaks)
[![Support](https://img.shields.io/badge/Support-%3E%3D%20Android%205.0-brightgreen)](https://github.com/Telefonica/mistica-android)

# Tweaks
A customizable debug screen to view and edit flags that can be used for development in **Jetpack Compose** applications

<img src="https://user-images.githubusercontent.com/4595241/136670126-10564d0c-9cc8-4758-bcdb-8ce7246b654e.gif" data-canonical-src="https://user-images.githubusercontent.com/4595241/136670126-10564d0c-9cc8-4758-bcdb-8ce7246b654e.gif" width="200" />

To include the library add to your app's `build.gradle`:

```gradle
implementation 'io.github.gmerinojimenez:tweaks:0.0.6'
```

Or, in case you want to don't add the library in release builds:
```gradle
debugImplementation 'io.github.gmerinojimenez:tweaks:0.0.6'
releaseImplementation 'io.github.gmerinojimenez:tweaks-no-op:0.0.6'
```

Then initialize the library in your app's `onCreate`:
```kotlin
override fun onCreate() {
    super.onCreate()
    Tweaks.init(this@TweakDemoApplication, demoTweakGraph())
}
```

where `demoTweakGraph` is the structure you want to be rendered:
```kotlin
    private fun demoTweakGraph() = TweaksGraph(
        category = listOf(
            TweakCategory(
                "Screen 1", listOf(
                    TweakGroup(
                        "Group 1", listOf(
                            ReadOnlyStringTweakEntry(
                                key = "timestamp",
                                name = "Current timestamp",
                                value = flow {
                                    while (true) {
                                        emit("${System.currentTimeMillis() / 1000}")
                                        delay(1000)
                                    }
                                }),
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
                                defaultUniqueValue = 0,
                            ),
                            ButtonTweakEntry(
                                key = "button1",
                                name = "Demo button"
                            ) {
                                Toast.makeText(this, "Demo button", Toast.LENGTH_LONG).show()
                            }
                        )
                    )
                )
            )
        )
    )
```

And then, in your NavHost setup, use the extension function `NavGraphBuilder.addTweakGraph` to fill the navigation graph with the tweak components:
```kotlin
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
```

Please review the app module for configuration examples. Check the available nodes in the graph [here](https://github.com/gmerinojimenez/tweaks/blob/main/library/src/enabled/java/com/gmerino/tweak/domain/tweakModels.kt)

## Special thanks to contributors:
* [Yamal Al Mahamid](https://github.com/yamal-coding)
