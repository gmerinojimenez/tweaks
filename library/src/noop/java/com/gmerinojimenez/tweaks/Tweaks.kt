package com.gmerinojimenez.tweaks

import android.app.Application
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.gmerinojimenez.tweaks.domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Tweaks {

    internal lateinit var tweaksGraph: TweaksGraph
    private val keyToEntryValueMap: MutableMap<String, TweakEntry<*>> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T> getTweakValue(key: String): Flow<T?> {
        val entry= keyToEntryValueMap[key] as TweakEntry<T>
        return getTweakValue(entry)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getTweakValue(entry: TweakEntry<T>): Flow<T?> = when (entry as Modifiable) {
        is ReadOnly<*> -> (entry as ReadOnly<T>).value
        is Editable<*> -> (entry as Editable<T>).defaultValue ?: flow { emit(null) }
    }

    private fun initialize(tweaksGraph: TweaksGraph) {
        val allEntries: List<TweakEntry<*>> = tweaksGraph.categories
            .flatMap { category ->
                category.groups.flatMap { group ->
                    group.entries
                }
            }
        allEntries.forEach { entry ->
            keyToEntryValueMap[entry.key] = entry
        }
    }

    companion object {
        const val TWEAKS_NAVIGATION_ENTRYPOINT = "tweaks"
        private var reference: Tweaks? = null

        @JvmStatic
        fun init(
            application: Application,
            tweaksGraph: TweaksGraph,
        ) {
            reference = Tweaks()
            reference!!.initialize(tweaksGraph)
        }

        @JvmStatic
        fun getReference() = reference!!
    }


}

fun NavGraphBuilder.addTweakGraph(
    navController: NavController,
    customComposableScreens: NavGraphBuilder.() -> Unit = {}
) {
}
