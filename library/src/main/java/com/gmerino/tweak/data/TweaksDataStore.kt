package com.gmerino.tweak.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

internal val Context.tweaksDataStore: DataStore<Preferences> by preferencesDataStore(name = "debug_tweaks")
