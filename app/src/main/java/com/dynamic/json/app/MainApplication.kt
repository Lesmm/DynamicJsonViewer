package com.dynamic.json.app

import com.dynamic.json.viewer.DyRenderApi

class MainApplication : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        DyRenderApi.application = this
        DyRenderApi.topActivity = { MainAppLifeCycle.instance.currentActivity }
        MainAppLifeCycle.instance.application = this

    }
}