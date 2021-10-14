package com.gmerinojimenez.tweak.ui

import androidx.lifecycle.ViewModel
import com.gmerinojimenez.tweak.Tweaks
import com.gmerinojimenez.tweak.domain.TweakEntry
import com.gmerinojimenez.tweak.domain.TweaksBusinessLogic
import kotlinx.coroutines.flow.Flow

class ReadOnlyTweakEntryViewModel<T>(
    private val tweaksBusinessLogic: TweaksBusinessLogic = Tweaks.getReference().tweaksBusinessLogic
) : ViewModel() {

    fun getValue(entry: TweakEntry<T>): Flow<T?> = tweaksBusinessLogic.getValue(entry)
}