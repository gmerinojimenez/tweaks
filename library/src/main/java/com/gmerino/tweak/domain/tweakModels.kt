package com.gmerino.tweak.domain

data class TweaksGraph(val category: List<TweakCategory>)
data class TweakCategory(val title: String, val groups: List<TweakGroup>)
data class TweakGroup(val title: String, val entries: List<TweakEntry<*>>)

sealed class TweakEntry<T>(val key: String, val name: String, val value: T?, val defaultValue: T? = null)
class StringTweakEntry(key: String, name: String, value: String?, defaultValue: String? = null): TweakEntry<String>(key, name, value, defaultValue)
class BooleanTweakEntry(key: String, name: String, value: Boolean?, defaultValue: Boolean? = null): TweakEntry<Boolean>(key, name, value, defaultValue)
class IntTweakEntry(key: String, name: String, value: Int?, defaultValue: Int? = null): TweakEntry<Int>(key, name, value, defaultValue)
class LongTweakEntry(key: String, name: String, value: Long?, defaultValue: Long? = null): TweakEntry<Long>(key, name, value, defaultValue)

internal object Constants {
    const val TWEAKS_NAVIGATION_ENTRYPOINT = "tweaks"
    const val TWEAK_MAIN_SCREEN = "tweaks-main-screen"
}
