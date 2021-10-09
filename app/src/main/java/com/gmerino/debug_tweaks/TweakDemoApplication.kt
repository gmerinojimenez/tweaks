package com.gmerino.debug_tweaks

import android.app.Application
import com.gmerino.tweak.Tweaks
import com.gmerino.tweak.domain.TweaksGraph

class TweakDemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Tweaks.init(this, TweaksGraph(emptyList()))
    }
}