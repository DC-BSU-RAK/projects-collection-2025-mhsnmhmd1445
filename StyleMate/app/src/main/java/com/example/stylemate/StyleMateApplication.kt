package com.example.stylemate

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Process
import kotlin.system.exitProcess

class StyleMateApplication : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize application manager
        ApplicationManager.initialize(applicationContext)

        // Register activity lifecycle callbacks
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: android.app.Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: android.app.Activity) {}

            override fun onActivityResumed(activity: android.app.Activity) {}

            override fun onActivityPaused(activity: android.app.Activity) {}

            override fun onActivityStopped(activity: android.app.Activity) {}

            override fun onActivitySaveInstanceState(activity: android.app.Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: android.app.Activity) {
                // If the main activity is destroyed, it means the app is being shut down
                if (activity is MainActivity) {
                    ApplicationManager.resetAllOnExit()
                }
            }
        })
    }

    override fun onTerminate() {
        // Reset all preferences on app termination
        ApplicationManager.resetAllOnExit()
        super.onTerminate()
    }
}