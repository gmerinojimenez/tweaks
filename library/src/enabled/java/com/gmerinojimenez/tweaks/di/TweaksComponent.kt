package com.gmerinojimenez.tweaks.di

import com.gmerinojimenez.tweaks.Tweaks
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [TweaksModule::class]
)
internal interface TweaksComponent {
    fun inject(tweaks: Tweaks)
}