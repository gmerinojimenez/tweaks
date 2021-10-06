package com.gmerino.tweak

import androidx.datastore.preferences.core.Preferences
import com.gmerino.tweak.data.TweaksDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTweaksData @Inject constructor(
    private val tweaksDataStore: TweaksDataStore,
){
    operator fun invoke(key: Preferences.Key<String>): Flow<String?> =
        tweaksDataStore.data
            .map { preferences -> preferences[key] }
}