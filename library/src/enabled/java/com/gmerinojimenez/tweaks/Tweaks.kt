package com.gmerinojimenez.tweaks

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_NORMAL
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.gmerinojimenez.tweaks.Tweaks.Companion.TWEAKS_NAVIGATION_ENTRYPOINT
import com.gmerinojimenez.tweaks.di.DaggerTweaksComponent
import com.gmerinojimenez.tweaks.di.TweaksComponent
import com.gmerinojimenez.tweaks.di.TweaksModule
import com.gmerinojimenez.tweaks.domain.Constants.TWEAK_MAIN_SCREEN
import com.gmerinojimenez.tweaks.domain.TweakCategory
import com.gmerinojimenez.tweaks.domain.TweakEntry
import com.gmerinojimenez.tweaks.domain.TweaksBusinessLogic
import com.gmerinojimenez.tweaks.domain.TweaksGraph
import com.gmerinojimenez.tweaks.ui.TweaksCategoryScreen
import com.gmerinojimenez.tweaks.ui.TweaksScreen
import com.squareup.seismic.ShakeDetector
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


open class Tweaks {

    @Inject
    internal lateinit var tweaksBusinessLogic: TweaksBusinessLogic

    open fun <T>getTweakValue(key: String): StateFlow<T?> = tweaksBusinessLogic.getValue(key)

    open fun <T> getTweakValue(entry: TweakEntry<T>): StateFlow<T?> = tweaksBusinessLogic.getValue(entry)

    private fun initializeGraph(tweaksGraph: TweaksGraph) {
        tweaksBusinessLogic.initialize(tweaksGraph)
    }

    companion object {
        const val TWEAKS_NAVIGATION_ENTRYPOINT = "tweaks"
        private var reference: Tweaks = Tweaks()
        private lateinit var component: TweaksComponent

        fun init(
            application: Application,
            tweaksGraph: TweaksGraph,
        ) {
            reference = Tweaks()
            inject(application)

            reference.initializeGraph(tweaksGraph)
        }

        @JvmStatic
        fun getReference(): Tweaks = reference

        private fun inject(application: Application) {
            component = DaggerTweaksComponent
                .builder()
                .tweaksModule(TweaksModule(application))
                .build()

            component.inject(reference)
        }
    }


}

@Composable
fun NavController.navigateToTweaksOnShake() {
    val context = LocalContext.current
    val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    LaunchedEffect(true) {
        val shakeDetector = ShakeDetector {
            vibrateIfAble(context)
            navigate(TWEAKS_NAVIGATION_ENTRYPOINT)
        }
        shakeDetector.start(sensorManager, SENSOR_DELAY_NORMAL)
    }
}

@SuppressLint("MissingPermission")
private fun vibrateIfAble(context: Context) {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.VIBRATE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, 100))
            } else {
                vibrator.vibrate(200)
            }
        }
    }
}

fun NavGraphBuilder.addTweakGraph(
    navController: NavController,
    customComposableScreens: NavGraphBuilder.() -> Unit = {}
) {
    val tweaksGraph = Tweaks.getReference().tweaksBusinessLogic.tweaksGraph

    val onNavigationEvent: (String) -> Unit = { route ->
        navController.navigate(route)
    }

    navigation(
        startDestination = TWEAK_MAIN_SCREEN,
        route = TWEAKS_NAVIGATION_ENTRYPOINT,
    ) {

        composable(TWEAK_MAIN_SCREEN) {
            TweaksScreen(
                tweaksGraph = tweaksGraph,
                onCategoryButtonClicked = { navController.navigate(it.navigationRoute())},
                onNavigationEvent = onNavigationEvent,
            )
        }

        tweaksGraph.categories.forEach { category ->
            composable(category.navigationRoute()) {
                TweaksCategoryScreen(
                    tweakCategory = category,
                    onNavigationEvent = onNavigationEvent,
                )
            }
        }
        customComposableScreens()
    }
}

private fun TweakCategory.navigationRoute(): String = "${this.title.replace(" ", "")}-tweak-screen"