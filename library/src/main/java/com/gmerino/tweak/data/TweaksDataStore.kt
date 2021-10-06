package com.gmerino.tweak.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "debug_tweaks")

@Singleton
class TweaksDataStore @Inject constructor(
    @ApplicationContext context: Context
): DataStore<Preferences> by context.dataStore