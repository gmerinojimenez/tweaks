package com.gmerino.tweak.ui

import androidx.lifecycle.ViewModel
import com.gmerino.tweak.Tweaks
import com.gmerino.tweak.domain.TweakEntry
import com.gmerino.tweak.domain.TweaksBusinessLogic
import kotlinx.coroutines.flow.Flow

class ReadOnlyTweakEntryViewModel<T>(
    private val tweaksBusinessLogic: TweaksBusinessLogic = Tweaks.getReference().tweaksBusinessLogic
) : ViewModel() {

    fun getValue(entry: TweakEntry<T>): Flow<T?> = tweaksBusinessLogic.getValue(entry)
}