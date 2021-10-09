package com.gmerino.debug_tweaks

import android.app.Application
import android.widget.Toast
import com.gmerino.tweak.Tweaks
import com.gmerino.tweak.domain.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.time.Duration

class TweakDemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Tweaks.init(this@TweakDemoApplication, demoTweakGraph())
    }

    var counter = 0
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
                                defaultValue = flow {
                                    while (true) {
                                        counter += 1
                                        emit(counter)
                                        delay(10000)
                                    }
                                },
                            ),
                            EditableLongTweakEntry(
                                key = "value4",
                                name = "Value 4",
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

}