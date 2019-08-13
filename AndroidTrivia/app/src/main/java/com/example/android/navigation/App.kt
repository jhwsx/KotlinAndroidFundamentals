package com.example.android.navigation

import android.app.Application
import timber.log.Timber

/**
 *
 * @author wzc
 * @date 2019/6/25
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}