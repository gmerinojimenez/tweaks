package com.gmerinojimenez.tweak

import android.app.Application
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.gmerinojimenez.tweak.Tweaks.Companion.TWEAKS_NAVIGATION_ENTRYPOINT
import com.gmerinojimenez.tweak.di.TweaksComponent
import com.gmerinojimenez.tweak.di.DaggerTweaksComponent
import com.gmerinojimenez.tweak.di.TweaksModule
import com.gmerinojimenez.tweak.domain.Constants.TWEAK_MAIN_SCREEN
import com.gmerinojimenez.tweak.domain.TweakCategory
import com.gmerinojimenez.tweak.domain.TweakEntry
import com.gmerinojimenez.tweak.domain.TweaksBusinessLogic
import com.gmerinojimenez.tweak.domain.TweaksGraph
import com.gmerinojimenez.tweak.ui.TweaksCategoryScreen
import com.gmerinojimenez.tweak.ui.TweaksScreen
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Tweaks {

    @Inject
    internal lateinit var tweaksBusinessLogic: TweaksBusinessLogic

    fun <T>getTweakValue(key: String): Flow<T?> = tweaksBusinessLogic.getValue(key)
    
    fun <T> getTweakValue(entry: TweakEntry<T>): Flow<T?> = tweaksBusinessLogic.getValue(entry)

    private fun initializeGraph(tweaksGraph: TweaksGraph) {
        tweaksBusinessLogic.initialize(tweaksGraph)
    }

    companion object {
        const val TWEAKS_NAVIGATION_ENTRYPOINT = "tweaks"
        private var reference: Tweaks? = null
        internal lateinit var component: TweaksComponent

        fun init(
            application: Application,
            tweaksGraph: TweaksGraph,
        ) {
            reference = Tweaks()
            inject(application)

            reference!!.initializeGraph(tweaksGraph)
        }

        @JvmStatic
        fun getReference() = reference!!

        private fun inject(application: Application) {
            component = DaggerTweaksComponent
                .builder()
                .tweaksModule(TweaksModule(application))
                .build()

            component.inject(reference!!)
        }
    }


}

fun NavGraphBuilder.addTweakGraph(
    navController: NavController,
) {
    val tweaksGraph = Tweaks.getReference().tweaksBusinessLogic.tweaksGraph
    navigation(
        startDestination = TWEAK_MAIN_SCREEN,
        route = TWEAKS_NAVIGATION_ENTRYPOINT,
    ) {

        composable(TWEAK_MAIN_SCREEN) {
            TweaksScreen(
                tweaksGraph = tweaksGraph,
                onCategoryButtonClicked = { navController.navigate(it.navigationRoute()) })
        }

        tweaksGraph.category.forEach { category ->
            composable(category.navigationRoute()) {
                TweaksCategoryScreen(tweakCategory = category)
            }
        }
    }
}

private fun TweakCategory.navigationRoute(): String = "${this.title.replace(" ", "")}-tweak-screen"