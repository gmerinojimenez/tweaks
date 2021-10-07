package com.gmerino.tweak.domain

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.gmerino.tweak.ui.TweaksCategoryScreen
import com.gmerino.tweak.ui.TweaksScreen
import com.gmerino.tweak.domain.Constants.TWEAKS_NAVIGATION_ENTRYPOINT

fun NavGraphBuilder.addTweakGraph(
    tweaksGraph: TweaksGraph,
    navController: NavController,
) {
    navigation(
        startDestination = "tweaks-main-screen",
        route = TWEAKS_NAVIGATION_ENTRYPOINT,
    ) {

        composable("tweaks-main-screen") {
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
