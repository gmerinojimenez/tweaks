package com.gmerino.tweak

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.gmerino.tweak.data.tweaksDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTweaksData @Inject constructor(
    private val context: Context,
) {
    fun getStringValue(key: Preferences.Key<String>): Flow<String?> =
        context.tweaksDataStore.data
            .map { preferences -> preferences[key] }

    fun getBooleanValue(key: Preferences.Key<Boolean>): Flow<Boolean?> =
        context.tweaksDataStore.data
            .map { preferences -> preferences[key] }

    fun getIntegerValue(key: Preferences.Key<Int>): Flow<Int?> =
        context.tweaksDataStore.data
            .map { preferences -> preferences[key] }

    fun getLongValue(key: Preferences.Key<Long>): Flow<Long?> =
        context.tweaksDataStore.data
            .map { preferences -> preferences[key] }
}