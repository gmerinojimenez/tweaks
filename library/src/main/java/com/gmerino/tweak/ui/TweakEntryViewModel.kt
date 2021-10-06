package com.gmerino.tweak.ui

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmerino.tweak.GetTweaksData
import com.gmerino.tweak.domain.SetTweakData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TweakEntryViewModel @Inject constructor(
    private val getTweaksData: GetTweaksData,
    private val setTweakData: SetTweakData,
): ViewModel() {

    fun getValue(key: String): Flow<String?> = getTweaksData(stringPreferencesKey(key))
    fun setValue(key: String, value: String?) {
        viewModelScope.launch {
            setTweakData(stringPreferencesKey(key), value!!)
        }
    }
}