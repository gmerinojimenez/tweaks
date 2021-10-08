package com.gmerino.tweak.ui

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmerino.tweak.data.tweaksDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class TweakEntryViewModel<T> : ViewModel() {

    fun getValue(context: Context, key: String): Flow<T?> = context.tweaksDataStore.data
        .map { preferences -> preferences[buildKey(key)] }

    fun setValue(context: Context, key: String, value: String) {
        viewModelScope.launch {
            context.tweaksDataStore.edit {
                it[stringPreferencesKey(key)] = value
            }
        }
    }

    fun clearValue(context: Context, key: String) {
        viewModelScope.launch {
            context.tweaksDataStore.edit { it.remove(stringPreferencesKey(key)) }
        }
    }

    abstract fun buildKey(key: String): Preferences.Key<T>
}

class StringTweakViewModel() : TweakEntryViewModel<String>() {
    override fun buildKey(key: String): Preferences.Key<String> = stringPreferencesKey(key)
}
class BooleanTweakViewModel() : TweakEntryViewModel<Boolean>() {
    override fun buildKey(key: String): Preferences.Key<Boolean> = booleanPreferencesKey(key)
}
class IntTweakViewModel() : TweakEntryViewModel<Int>() {
    override fun buildKey(key: String): Preferences.Key<Int> = intPreferencesKey(key)
}
class LongTweakViewModel() : TweakEntryViewModel<Long>() {
    override fun buildKey(key: String): Preferences.Key<Long> = longPreferencesKey(key)
}