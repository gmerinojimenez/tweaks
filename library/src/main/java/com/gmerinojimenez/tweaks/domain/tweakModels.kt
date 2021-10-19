package com.gmerinojimenez.tweaks.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun tweaksGraph(block: TweaksGraph.Builder.() -> Unit): TweaksGraph {
    val builder = TweaksGraph.Builder()
    builder.block()
    return builder.build()
}

/** The top level node of the tweak graphs. It contains a list of categories (screens)*/
data class TweaksGraph(val categories: List<TweakCategory>) {
    class Builder {
        private val categories = mutableListOf<TweakCategory>()

        fun tweakCategory(title: String, block: TweakCategory.Builder.() -> Unit) {
            val builder = TweakCategory.Builder(title)
            builder.block()
            categories.add(builder.build())
        }

        internal fun build(): TweaksGraph = TweaksGraph(categories)
    }
}

/** A tweak category is a screen, for example your app tweaks can be splitted by features
 *  (chat, video, login...) each one of those can be a category
 *  */
data class TweakCategory(val title: String, val groups: List<TweakGroup>) {
    class Builder(private val title: String) {
        private val groups = mutableListOf<TweakGroup>()

        fun tweakGroup(title: String, block: TweakGroup.Builder.() -> Unit) {
            val builder = TweakGroup.Builder(title)
            builder.block()
            groups.add(builder.build())
        }

        internal fun build(): TweakCategory = TweakCategory(title, groups)
    }
}

/** A bunch of tweaks that are related to each other, for example: domain & port for the backend server configurations*/
data class TweakGroup(val title: String, val entries: List<TweakEntry<*>>) {
    class Builder(private val title: String) {
        private val entries = mutableListOf<TweakEntry<*>>()

        fun addEntry(entry: TweakEntry<*>) {
            entries.add(entry)
        }

        internal fun build(): TweakGroup = TweakGroup(title, entries)
    }
}

sealed class TweakEntry<T>(val key: String, val name: String)

/** A button, with a customizable action*/
class ButtonTweakEntry(key: String, name: String, val action: () -> Unit): TweakEntry<Unit>(key, name)

/** A non editable entry */
class ReadOnlyStringTweakEntry(key: String, name: String, override val value: Flow<String>) :
    TweakEntry<String>(key, name), ReadOnly<String>

/** An editable entry. It can be modified by using long-press*/
class EditableStringTweakEntry(
    key: String,
    name: String,
    override val defaultValue: Flow<String>? = null,
) : TweakEntry<String>(key, name), Editable<String> {
    constructor(
        key: String,
        name: String,
        defaultUniqueValue: String,
    ) : this(key, name, flow { emit(defaultUniqueValue) })
}

/** An editable entry. It can be modified by using long-press*/
class EditableBooleanTweakEntry(
    key: String,
    name: String,
    override val defaultValue: Flow<Boolean>? = null,
) : TweakEntry<Boolean>(key, name), Editable<Boolean> {
    constructor(
        key: String,
        name: String,
        defaultUniqueValue: Boolean,
    ) : this(key, name, flow { emit(defaultUniqueValue) })
}

/** An editable entry. It can be modified by using long-press*/
class EditableIntTweakEntry(
    key: String,
    name: String,
    override val defaultValue: Flow<Int>? = null,
) : TweakEntry<Int>(key, name), Editable<Int> {
    constructor(
        key: String,
        name: String,
        defaultUniqueValue: Int,
    ) : this(key, name, flow { emit(defaultUniqueValue) })
}

/** An editable entry. It can be modified by using long-press*/
class EditableLongTweakEntry(
    key: String,
    name: String,
    override val defaultValue: Flow<Long>? = null,
) : TweakEntry<Long>(key, name), Editable<Long> {
    constructor(
        key: String,
        name: String,
        defaultUniqueValue: Long,
    ) : this(key, name, flow { emit(defaultUniqueValue) })
}

sealed interface Modifiable
interface Editable<T> : Modifiable {
    val defaultValue: Flow<T>?
}

interface ReadOnly<T> : Modifiable {
    val value: Flow<T>
}

internal object Constants {
    const val TWEAK_MAIN_SCREEN = "tweaks-main-screen"
}