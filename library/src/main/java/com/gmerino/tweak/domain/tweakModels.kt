package com.gmerino.tweak.domain

import kotlinx.coroutines.flow.Flow

data class TweaksGraph(val category: List<TweakCategory>)
data class TweakCategory(val title: String, val groups: List<TweakGroup>)
data class TweakGroup(val title: String, val entries: List<TweakEntry<*>>)

sealed class TweakEntry<T>(val key: String, val name: String)

class ReadOnlyStringTweakEntry(key: String, name: String, override val value: () -> Flow<String>) : TweakEntry<String>(key, name), ReadOnly<String>
class EditableStringTweakEntry(key: String, name: String) : TweakEntry<String>(key, name), Editable
class EditableBooleanTweakEntry(key: String, name: String) : TweakEntry<Boolean>(key, name), Editable
class EditableIntTweakEntry(key: String, name: String) : TweakEntry<Int>(key, name), Editable
class EditableLongTweakEntry(key: String, name: String) : TweakEntry<Long>(key, name), Editable

sealed interface Modifiable
interface Editable: Modifiable
interface ReadOnly<T>: Modifiable {
    val value: () -> Flow<T>
}

internal object Constants {
    const val TWEAKS_NAVIGATION_ENTRYPOINT = "tweaks"
    const val TWEAK_MAIN_SCREEN = "tweaks-main-screen"
}