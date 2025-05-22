package com.example.ideajoltv1

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.ideajoltv1.utils.UserPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class IdeaJoltApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        // Apply saved theme on app start
        applicationScope.launch {
            try {
                val userPreferencesManager = UserPreferencesManager(this@IdeaJoltApplication)
                val themeMode = userPreferencesManager.themeModeFlow.first()

                val nightMode = when (themeMode) {
                    UserPreferencesManager.THEME_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                    UserPreferencesManager.THEME_DARK -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }

                AppCompatDelegate.setDefaultNightMode(nightMode)
            } catch (e: Exception) {
                // Default to dark theme if there's an error
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }
}