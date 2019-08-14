package com.example.android.guesstheword

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree



/**
 * <pre>
 *     author : wangzhichao
 *     e-mail : wangzhichao@adups.com
 *     time   : 2019/08/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
    }
}