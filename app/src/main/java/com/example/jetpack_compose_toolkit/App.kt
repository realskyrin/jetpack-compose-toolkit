package com.example.jetpack_compose_toolkit

import android.app.Application
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        val debug = object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                return String.format(
                    "[%s:%s]",
                    element.fileName,
                    element.lineNumber,
                )
            }
        }
        Timber.plant(debug)
    }
}
