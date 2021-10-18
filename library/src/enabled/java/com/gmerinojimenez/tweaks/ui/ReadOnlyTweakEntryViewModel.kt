package com.gmerinojimenez.tweaks.ui

import androidx.lifecycle.ViewModel
import com.gmerinojimenez.tweaks.Tweaks
import com.gmerinojimenez.tweaks.domain.TweakEntry
import com.gmerinojimenez.tweaks.domain.TweaksBusinessLogic
import kotlinx.coroutines.flow.Flow

class ReadOnlyTweakEntryViewModel<T>(
    private val tweaksBusinessLogic: TweaksBusinessLogic = Tweaks.getReference().tweaksBusinessLogic
) : ViewModel() {

    fun getValue(entry: TweakEntry<T>): Flow<T?> = tweaksBusinessLogic.getValue(entry)
}