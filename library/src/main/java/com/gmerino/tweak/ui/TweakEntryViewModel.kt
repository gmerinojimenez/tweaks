package com.gmerino.tweak.ui

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmerino.tweak.GetTweaksData
import com.gmerino.tweak.data.tweaksDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TweakEntryViewModel : ViewModel() {

    fun getStringValue(context: Context, key: String): Flow<String?> = context.tweaksDataStore.data
        .map { preferences -> preferences[stringPreferencesKey(key)] }

    fun setStringValue(context: Context, key: String, value: String) {
        viewModelScope.launch {
            context.tweaksDataStore.edit {
                it[stringPreferencesKey(key)] = value!!
            }
        }
    }

    fun clearStringValue(context: Context, key: String, value: String) {
        viewModelScope.launch {
            context.tweaksDataStore.edit { it.remove(stringPreferencesKey(key)) }
        }
    }
}