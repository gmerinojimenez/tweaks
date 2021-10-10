package com.gmerino.tweak

import android.app.Application
import androidx.navigation.NavController

class Tweaks {

    companion object {
        private var reference: Tweaks? = null

        fun init(
            application: Application,
            tweaksGraph: TweaksGraph,
        ) {
           reference = Tweaks()
        }

        fun getReference() = reference!!
    }


}

fun NavGraphBuilder.addTweakGraph(
    navController: NavController,
) {

}
