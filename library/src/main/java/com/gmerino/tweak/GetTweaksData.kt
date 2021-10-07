package com.gmerino.tweak

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.gmerino.tweak.data.tweaksDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTweaksData constructor(
    private val context: Context,
) {
    operator fun invoke(key: Preferences.Key<String>): Flow<String?> =
        context.tweaksDataStore.data
            .map { preferences -> preferences[key] }
}