package com.gmerinojimenez.tweaks.demo

import android.app.Application
import android.widget.Toast
import com.gmerinojimenez.tweaks.Tweaks
import com.gmerinojimenez.tweaks.domain.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class TweakDemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Tweaks.init(this@TweakDemoApplication, demoTweakGraph())
    }

    var counter = 0

    private fun demoTweakGraph() = tweaksGraph {
        cover("Tweaks Demo") {
            label("cover-key", "Current user ID:") { flow { emit("1")} }
        }
        category("Screen 1") {
            group("Group 1") {
                label(
                    key = "timestamp",
                    name = "Current timestamp",
                ) {
                    flow {
                        while (true) {
                            emit("${System.currentTimeMillis() / 1000}")
                            delay(1000)
                        }
                    }
                }
                editableString(
                    key = "value1",
                    name = "Value 1",
                )
                editableBoolean(
                    key = "value2",
                    name = "Value 2",
                )
                editableInt(
                    key = "value3",
                    name = "Value 3",
                    defaultValue = flow {
                        while (true) {
                            counter += 1
                            emit(counter)
                            delay(1000)
                        }
                    }
                )
                editableLong(
                    key = "value4",
                    name = "Value 4",
                    defaultValue = 0L,
                )

                button(
                    key = "button1",
                    name = "Demo button"
                ) {
                    Toast.makeText(this@TweakDemoApplication, "Demo button", Toast.LENGTH_LONG)
                        .show()
                }

                routeButton(
                    key = "button2",
                    name = "Custom screen button",
                    route = "custom-screen"
                )
            }
        }
    }
}