package com.gmerinojimenez.tweaks.domain

import androidx.datastore.preferences.core.*
import com.gmerinojimenez.tweaks.data.TweaksDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TweaksBusinessLogic @Inject constructor(
    private val tweaksDataStore: TweaksDataStore,
) {

    internal lateinit var tweaksGraph: TweaksGraph
    private val keyToEntryValueMap: MutableMap<String, TweakEntry<*>> = mutableMapOf()

    internal fun initialize(tweaksGraph: TweaksGraph) {
        this.tweaksGraph = tweaksGraph
        val alreadyIntroducedKeys = mutableSetOf<String>()
        val allEntries: MutableList<TweakEntry<*>> = tweaksGraph.categories
            .flatMap { category ->
                val groupsAndCover = if (tweaksGraph.cover != null) {
                    category.groups.plus(tweaksGraph.cover)
                } else {
                    category.groups
                }
                groupsAndCover.flatMap { group ->
                    group.entries
                }
            }.toMutableList()

        allEntries.forEach { entry ->
            checkIfRepeatedKey(alreadyIntroducedKeys, entry)
            keyToEntryValueMap[entry.key] = entry
        }
    }

    private fun checkIfRepeatedKey(
        alreadyIntroducedKeys: MutableSet<String>,
        entry: TweakEntry<*>
    ) {
        if (alreadyIntroducedKeys.contains(entry.key)) {
            throw IllegalStateException("There is a repeated key in the tweaks, review your graph")
        }

        alreadyIntroducedKeys.add(entry.key)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getValue(key: String): StateFlow<T?> {
        val tweakEntry = keyToEntryValueMap[key] as TweakEntry<T>
        return getValue(tweakEntry)
    }

    fun <T> getValue(entry: TweakEntry<T>): StateFlow<T?> = when (entry as Modifiable) {
        is ReadOnly<*> -> (entry as ReadOnly<T>).value
        is Editable<*> -> getEditableValue(entry)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun <T> getEditableValue(entry: TweakEntry<T>): StateFlow<T?> {
        val editableCasted = entry as Editable<T>
        val defaultValueFlow: StateFlow<T>? = editableCasted.defaultValue
        val initialValue = defaultValueFlow?.value

        val mergedFlow: Flow<T?> = if (defaultValueFlow != null) {
            merge(
                getFromStorage(entry)
                    .filter { it != null },
                defaultValueFlow
            )
        } else {
            getFromStorage(entry)
        }

        return mergedFlow.stateIn(
            scope = CoroutineScope(Dispatchers.Default),
            started = SharingStarted.Lazily,
            initialValue = initialValue
        )
    }

    private fun <T> getFromStorage(entry: TweakEntry<T>) =
        tweaksDataStore.data
            .map { preferences -> preferences[buildKey(entry)] }

    suspend fun <T> setValue(entry: TweakEntry<T>, value: T?) {
        tweaksDataStore.edit {
            if (value != null) {
                it[buildKey(entry)] = value
            } else {
                it.remove(buildKey(entry))
            }
        }
    }

    suspend fun <T> setValue(key: String, value: T?) {
        val tweakEntry = keyToEntryValueMap[key] as TweakEntry<T>
        setValue(tweakEntry, value)
    }

    suspend fun <T> clearValue(entry: TweakEntry<T>) {
        tweaksDataStore.edit {
            it.remove(buildKey(entry))
        }
    }

    suspend fun <T> clearValue(key: String) {
        val tweakEntry = keyToEntryValueMap[key] as TweakEntry<T>
        clearValue(tweakEntry)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> buildKey(entry: TweakEntry<T>): Preferences.Key<T> = when (entry) {
        is ReadOnlyStringTweakEntry -> stringPreferencesKey(entry.key) as Preferences.Key<T>
        is EditableStringTweakEntry -> stringPreferencesKey(entry.key) as Preferences.Key<T>
        is EditableBooleanTweakEntry -> booleanPreferencesKey(entry.key) as Preferences.Key<T>
        is EditableIntTweakEntry -> intPreferencesKey(entry.key) as Preferences.Key<T>
        is EditableLongTweakEntry -> longPreferencesKey(entry.key) as Preferences.Key<T>
        is ButtonTweakEntry -> throw java.lang.IllegalStateException("Buttons doesn't have keys")
        is RouteButtonTweakEntry -> throw java.lang.IllegalStateException("Buttons doesn't have keys")
    }
}