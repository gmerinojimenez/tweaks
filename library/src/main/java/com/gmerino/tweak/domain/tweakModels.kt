package com.gmerino.tweak.domain

data class TweaksGraph(val category: List<TweakCategory>)
data class TweakCategory(val title: String, val groups: List<TweakGroup>)
data class TweakGroup(val title: String, val entries: List<TweakEntry<*>>)

sealed class TweakEntry<T>(val key: String, val name: String, val defaultValue: T? = null)
class StringTweakEntry(key: String, name: String, defaultValue: String? = null): TweakEntry<String>(key, name, defaultValue)
class BooleanTweakEntry(key: String, name: String, defaultValue: Boolean? = null): TweakEntry<Boolean>(key, name, defaultValue)
class IntTweakEntry(key: String, name: String, defaultValue: Int? = null): TweakEntry<Int>(key, name, defaultValue)
class LongTweakEntry(key: String, name: String, defaultValue: Long? = null): TweakEntry<Long>(key, name, defaultValue)

internal object Constants {
    const val TWEAKS_NAVIGATION_ENTRYPOINT = "tweaks"
    const val TWEAK_MAIN_SCREEN = "tweaks-main-screen"
}
