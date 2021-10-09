package com.gmerino.tweak.domain

import androidx.datastore.preferences.core.*
import com.gmerino.tweak.data.TweaksDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TweaksBusinessLogic @Inject constructor(
    private val tweaksDataStore: TweaksDataStore,
) {

    private val keyToEntryValueMap: MutableMap<String, TweakEntry<*>> = mutableMapOf()

    internal fun initialize(tweaksGraph: TweaksGraph) {
        val alreadyIntroducedKeys = mutableSetOf<String>()
        val allEntries: List<TweakEntry<*>> = tweaksGraph.category
            .flatMap { category ->
                category.groups.flatMap { group ->
                    group.entries
                }
            }
        allEntries.forEach { entry ->
            if (alreadyIntroducedKeys.contains(entry.key)) {
                throw IllegalStateException("There is a repeated key in the tweaks, review your graph")
            }

            alreadyIntroducedKeys.add(entry.key)
            keyToEntryValueMap[entry.key] = entry
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getValue(key: String): Flow<T?> = tweaksDataStore.data
        .map { preferences -> preferences[buildKey<T>(keyToEntryValueMap[key] as TweakEntry<T>)] }

    fun <T> getValue(entry: TweakEntry<T>): Flow<T?> = when (entry as Modifiable) {
        is ReadOnly<*> -> (entry as ReadOnly<T>).value()
        is Editable -> getFromStorage(entry)
    }

    private fun <T> getFromStorage(entry: TweakEntry<T>) =
        tweaksDataStore.data
            .map { preferences -> preferences[buildKey(entry)] }

    suspend fun <T> setValue(entry: TweakEntry<T>, value: T) {
        tweaksDataStore.edit {
            it[buildKey(entry)] = value
        }
    }

    suspend fun <T> clearValue(entry: TweakEntry<T>) {
        tweaksDataStore.edit {
            it.remove(buildKey(entry))
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> buildKey(entry: TweakEntry<T>): Preferences.Key<T> = when (entry) {
        is ReadOnlyStringTweakEntry -> stringPreferencesKey(entry.key) as Preferences.Key<T>
        is EditableStringTweakEntry -> stringPreferencesKey(entry.key) as Preferences.Key<T>
        is EditableBooleanTweakEntry -> booleanPreferencesKey(entry.key) as Preferences.Key<T>
        is EditableIntTweakEntry -> intPreferencesKey(entry.key) as Preferences.Key<T>
        is EditableLongTweakEntry -> longPreferencesKey(entry.key) as Preferences.Key<T>
    }
}