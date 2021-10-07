package com.gmerino.tweak

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.gmerino.tweak.ui.TweaksCategoryScreen
import com.gmerino.tweak.ui.TweaksScreen
import com.gmerino.tweak.domain.Constants.TWEAKS_NAVIGATION_ENTRYPOINT
import com.gmerino.tweak.domain.Constants.TWEAK_MAIN_SCREEN
import com.gmerino.tweak.domain.TweakCategory
import com.gmerino.tweak.domain.TweaksGraph

fun NavGraphBuilder.addTweakGraph(
    tweaksGraph: TweaksGraph,
    navController: NavController,
) {
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
