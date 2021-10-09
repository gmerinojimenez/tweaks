package com.gmerino.tweak.di

import com.gmerino.tweak.Tweaks
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [TweaksModule::class]
)
internal interface TweaksComponent {
    fun inject(tweaks: Tweaks)
}