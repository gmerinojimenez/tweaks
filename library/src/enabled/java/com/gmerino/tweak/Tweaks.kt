package com.gmerino.tweak

import android.app.Application
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.gmerino.tweak.di.TweaksComponent
import com.gmerino.tweak.di.DaggerTweaksComponent
import com.gmerino.tweak.di.TweaksModule
import com.gmerino.tweak.domain.Constants.TWEAKS_NAVIGATION_ENTRYPOINT
import com.gmerino.tweak.domain.Constants.TWEAK_MAIN_SCREEN
import com.gmerino.tweak.domain.TweakCategory
import com.gmerino.tweak.domain.TweaksBusinessLogic
import com.gmerino.tweak.domain.TweaksGraph
import com.gmerino.tweak.ui.TweaksCategoryScreen
import com.gmerino.tweak.ui.TweaksScreen
import javax.inject.Inject

class Tweaks {

    @Inject
    internal lateinit var tweaksBusinessLogic: TweaksBusinessLogic

    private fun initializeGraph(tweaksGraph: TweaksGraph) {
        tweaksBusinessLogic.initialize(tweaksGraph)
    }

    companion object {
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