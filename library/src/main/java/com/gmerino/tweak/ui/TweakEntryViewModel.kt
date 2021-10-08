package com.gmerino.tweak.ui

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmerino.tweak.data.tweaksDataStore
import com.gmerino.tweak.domain.TweakEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class TweakEntryViewModel<T> : ViewModel() {

    fun getValue(context: Context, key: String): Flow<T?> = context.tweaksDataStore.data
        .map { preferences -> preferences[buildKey(key)] }

    fun setValue(context: Context, entry: TweakEntry<T>, value: T) {
        viewModelScope.launch {
            context.tweaksDataStore.edit {
                it[buildKey(entry.key)] = value
            }
        }
    }

    fun clearValue(context: Context, entry: TweakEntry<T>) {
        viewModelScope.launch {
            context.tweaksDataStore.edit {
                it.remove(buildKey(entry.key))
            }
        }
    }

//    fun initializeValue(context: Context, entry: TweakEntry<T>) {
//        viewModelScope.launch {
//            context.tweaksDataStore.edit {
//                val key = buildKey(entry.key)
//                if (!it.contains(key)) {
//                    it[key] = entry.defaultValue()
//                }
//            }
//        }
//    }

    abstract fun buildKey(key: String): Preferences.Key<T>
}

class StringTweakViewModel : TweakEntryViewModel<String>() {
    override fun buildKey(key: String): Preferences.Key<String> = stringPreferencesKey(key)
}
class BooleanTweakViewModel : TweakEntryViewModel<Boolean>() {
    override fun buildKey(key: String): Preferences.Key<Boolean> = booleanPreferencesKey(key)
}
class IntTweakViewModel : TweakEntryViewModel<Int>() {
    override fun buildKey(key: String): Preferences.Key<Int> = intPreferencesKey(key)
}
class LongTweakViewModel : TweakEntryViewModel<Long>() {
    override fun buildKey(key: String): Preferences.Key<Long> = longPreferencesKey(key)
}

object EditableDefaultValues{
    const val STRING_DEFAULT_REPRESENTATION = "[Not set]"
}