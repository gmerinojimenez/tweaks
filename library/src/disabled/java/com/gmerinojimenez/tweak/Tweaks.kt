package com.gmerinojimenez.tweak

import android.app.Application
import androidx.navigation.NavController

class Tweaks {

    companion object {
        const val TWEAKS_NAVIGATION_ENTRYPOINT = "tweaks"
        private var reference: Tweaks? = null

        @JvmStatic
        fun init(
            application: Application,
            tweaksGraph: TweaksGraph,
        ) {
           reference = Tweaks()
        }

        @JvmStatic
        fun getReference() = reference!!
    }


}

fun NavGraphBuilder.addTweakGraph(
    navController: NavController,
) {

}
