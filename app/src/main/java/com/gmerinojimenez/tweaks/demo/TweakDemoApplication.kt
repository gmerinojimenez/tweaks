package com.gmerinojimenez.tweaks.demo

import android.app.Application
import android.widget.Toast
import com.gmerinojimenez.tweaks.Tweaks
import com.gmerinojimenez.tweaks.domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TweakDemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Tweaks.init(this@TweakDemoApplication, demoTweakGraph())
    }

    var timestampState = MutableStateFlow("0")

    init {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                timestampState.value = "${System.currentTimeMillis() / 1000}"
                delay(1000)
            }
        }
    }

    private fun demoTweakGraph() = tweaksGraph {
        cover("Tweaks Demo") {
            label("cover-key", "Current user ID:") { MutableStateFlow("1") }
        }
        category("Screen 1") {
            group("Group 1") {
                label(
                    key = "timestamp",
                    name = "Current timestamp",
                ) {
                    timestampState
                }
                editableString(
                    key = "value1",
                    name = "Value 1",
                )
                editableBoolean(
                    key = "value2",
                    name = "Value 2",
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