package com.gmerino.tweak.domain

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.gmerino.tweak.data.TweaksDataStore
import javax.inject.Inject

class SetTweakData @Inject constructor(
    private val tweaksDataStore: TweaksDataStore,
) {
    suspend operator fun invoke(key: Preferences.Key<String>, value: String?) {
        tweaksDataStore.edit {
            it[key] = value ?: "" //TODO gmerino
        }
    }
}