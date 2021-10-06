package com.gmerino.tweak.domain

data class TweaksGraph(val category: List<TweakCategory>)
data class TweakCategory(val title: String, val groups: List<TweakGroup>)
data class TweakGroup(val title: String, val entries: List<TweakEntry<*>>)
data class TweakEntry<T>(val key: String, val descriptiveName: String, val value: T?, val defaultValue: T? = null)

internal object Constants {
    const val TWEAKS_NAVIGATION_ENTRYPOINT = "tweaks"
}
