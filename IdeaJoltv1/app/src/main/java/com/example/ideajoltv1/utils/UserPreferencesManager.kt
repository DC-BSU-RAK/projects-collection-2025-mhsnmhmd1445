package com.example.ideajoltv1.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create a DataStore instance at the top level
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesManager(private val context: Context) {

    companion object {
        // Existing keys
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")

        // New settings keys
        private val AI_TONE_KEY = stringPreferencesKey("ai_tone")
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        private val PREFERRED_PROJECT_TYPE_KEY = stringPreferencesKey("preferred_project_type")
        private val PREFERRED_MOOD_KEY = stringPreferencesKey("preferred_mood")

        // Default values
        const val AI_TONE_PROFESSIONAL = "Professional"
        const val AI_TONE_FUNNY = "Funny"
        const val AI_TONE_INSPIRING = "Inspiring"

        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"

        // Project types
        const val PROJECT_TYPE_BUSINESS = "Business"
        const val PROJECT_TYPE_ART = "Art"
        const val PROJECT_TYPE_TECHNOLOGY = "Technology"
        const val PROJECT_TYPE_EDUCATION = "Education"

        // Moods
        const val MOOD_CREATIVE = "Creative"
        const val MOOD_SERIOUS = "Serious"
        const val MOOD_FUN = "Fun"
        const val MOOD_INNOVATIVE = "Innovative"
    }

    // Existing Flows
    val usernameFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USERNAME_KEY] }

    val userIdFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_ID_KEY] }

    val emailFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[EMAIL_KEY] }

    val loginStatusFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_LOGGED_IN_KEY] ?: false }

    // New settings Flows
    val aiToneFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[AI_TONE_KEY] ?: AI_TONE_PROFESSIONAL
        }

    val themeModeFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_MODE_KEY] ?: THEME_DARK
        }

    val preferredProjectTypeFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PREFERRED_PROJECT_TYPE_KEY] ?: PROJECT_TYPE_TECHNOLOGY
        }

    val preferredMoodFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PREFERRED_MOOD_KEY] ?: MOOD_CREATIVE
        }

    // Existing save methods
    suspend fun saveUserData(username: String, userId: String, email: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
            preferences[USER_ID_KEY] = userId
            preferences[EMAIL_KEY] = email
        }
    }

    suspend fun saveLoginStatus(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }

    suspend fun saveUserDetails(userId: String, email: String, username: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[EMAIL_KEY] = email
            preferences[USERNAME_KEY] = username
        }
    }

    // New settings save methods
    suspend fun saveAiTone(tone: String) {
        context.dataStore.edit { preferences ->
            preferences[AI_TONE_KEY] = tone
        }
    }

    suspend fun saveThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode
        }
    }

    suspend fun savePreferredProjectType(type: String) {
        context.dataStore.edit { preferences ->
            preferences[PREFERRED_PROJECT_TYPE_KEY] = type
        }
    }

    suspend fun savePreferredMood(mood: String) {
        context.dataStore.edit { preferences ->
            preferences[PREFERRED_MOOD_KEY] = mood
        }
    }

    // Clear user data on logout
    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.remove(USERNAME_KEY)
            preferences.remove(USER_ID_KEY)
            preferences.remove(EMAIL_KEY)
            preferences[IS_LOGGED_IN_KEY] = false
            // We don't clear settings on logout
        }
    }
}