package com.example.stylemate

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Process
import kotlin.system.exitProcess

class ApplicationManager {
    companion object {
        // Shared instance
        private lateinit var sharedPreferences: SharedPreferences

        // Initialize with application context
        fun initialize(context: Context) {
            sharedPreferences = context.getSharedPreferences("StyleMatePrefs", Context.MODE_PRIVATE)
        }

        // Remove all saved preferences when the app is shut down
        fun resetAllOnExit() {
            sharedPreferences.edit().clear().apply()
        }

        // Completely exit the app (use carefully)
        fun exitApplication() {
            // Clear all preferences
            resetAllOnExit()

            // Kill the app process
            Process.killProcess(Process.myPid())
            exitProcess(0)
        }
    }
}