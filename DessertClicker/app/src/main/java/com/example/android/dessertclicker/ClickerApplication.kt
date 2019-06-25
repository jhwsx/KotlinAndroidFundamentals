package com.example.android.dessertclicker

import android.app.Application
import timber.log.Timber

/**
 *
 * @author wzc
 * @date 2019/6/25
 */
class ClickerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}