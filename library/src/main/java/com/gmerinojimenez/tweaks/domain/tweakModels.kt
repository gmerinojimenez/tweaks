package com.gmerinojimenez.tweaks.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun tweaksGraph(block: TweaksGraph.Builder.() -> Unit): TweaksGraph {
    val builder = TweaksGraph.Builder()
    builder.block()
    return builder.build()
}

/** The top level node of the tweak graphs. It contains a list of categories (screens)*/
data class TweaksGraph(val cover: TweakGroup?, val categories: List<TweakCategory>) {
    class Builder {
        private val categories = mutableListOf<TweakCategory>()
        private var cover: TweakGroup? = null

        fun cover(title: String, block: TweakGroup.Builder.() -> Unit) {
            val builder = TweakGroup.Builder(title)
            builder.block()
            cover = builder.build()
        }

        fun category(title: String, block: TweakCategory.Builder.() -> Unit) {
            val builder = TweakCategory.Builder(title)
            builder.block()
            categories.add(builder.build())
        }

        internal fun build(): TweaksGraph = TweaksGraph(cover, categories)
    }
}

/** A tweak category is a screen, for example your app tweaks can be splitted by features
 *  (chat, video, login...) each one of those can be a category
 *  */
data class TweakCategory(val title: String, val groups: List<TweakGroup>) {
    class Builder(private val title: String) {
        private val groups = mutableListOf<TweakGroup>()

        fun group(title: String, block: TweakGroup.Builder.() -> Unit) {
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

        fun tweak(entry: TweakEntry<*>) {
            entries.add(entry)
        }

        fun button(
            key: String,
            name: String,
            action: () -> Unit,
        ) {
            tweak(ButtonTweakEntry(key, name, action))
        }

        fun routeButton(
            key: String,
            name: String,
            route: String,
        ) {
            tweak(RouteButtonTweakEntry(key, name, route))
        }

        fun label(
            key: String,
            name: String,
            value: () -> Flow<String>,
        ) {
            tweak(ReadOnlyStringTweakEntry(key, name, value()))
        }

        fun editableString(
            key: String,
            name: String,
            defaultValue: Flow<String>? = null,
        ) {
            tweak(EditableStringTweakEntry(key, name, defaultValue))
        }

        fun editableString(
            key: String,
            name: String,
            defaultValue: String,
        ) {
            tweak(EditableStringTweakEntry(key, name, defaultValue))
        }

        fun editableBoolean(
            key: String,
            name: String,
            defaultValue: Flow<Boolean>? = null,
        ) {
            tweak(EditableBooleanTweakEntry(key, name, defaultValue))
        }

        fun editableBoolean(
            key: String,
            name: String,
            defaultValue: Boolean,
        ) {
            tweak(EditableBooleanTweakEntry(key, name, defaultValue))
        }

        fun editableInt(
            key: String,
            name: String,
            defaultValue: Flow<Int>? = null,
        ) {
            tweak(EditableIntTweakEntry(key, name, defaultValue))
        }

        fun editableInt(
            key: String,
            name: String,
            defaultValue: Int,
        ) {
            tweak(EditableIntTweakEntry(key, name, defaultValue))
        }

        fun editableLong(
            key: String,
            name: String,
            defaultValue: Flow<Long>? = null,
        ) {
            tweak(EditableLongTweakEntry(key, name, defaultValue))
        }

        fun editableLong(
            key: String,
            name: String,
            defaultValue: Long,
        ) {
            tweak(EditableLongTweakEntry(key, name, defaultValue))
        }

        internal fun build(): TweakGroup = TweakGroup(title, entries)
    }
}

sealed class TweakEntry<T>(val key: String, val name: String)

/** A button, with a customizable action*/
class ButtonTweakEntry(key: String, name: String, val action: () -> Unit) :
    TweakEntry<Unit>(key, name)

/** A button, that when tapped navigates to a route*/
class RouteButtonTweakEntry(key: String, name: String, val route: String) :
    TweakEntry<Unit>(key, name)

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