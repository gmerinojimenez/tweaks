package com.gmerino.tweak.domain

data class TweaksGraph(val category: List<TweakCategory>)
data class TweakCategory(val title: String, val groups: List<TweakGroup>)
data class TweakGroup(val title: String, val entries: List<TweakEntry<*>>)

sealed class TweakEntry<T>(val key: String, val name: String)

class EditableStringTweakEntry(key: String, name: String) : TweakEntry<String>(key, name)
class EditableBooleanTweakEntry(key: String, name: String) : TweakEntry<Boolean>(key, name)
class EditableIntTweakEntry(key: String, name: String) : TweakEntry<Int>(key, name)
class EditableLongTweakEntry(key: String, name: String) : TweakEntry<Long>(key, name)

internal object Constants {
    const val TWEAKS_NAVIGATION_ENTRYPOINT = "tweaks"
    const val TWEAK_MAIN_SCREEN = "tweaks-main-screen"
}
