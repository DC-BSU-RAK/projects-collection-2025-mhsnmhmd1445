package com.example.ideajoltv1.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ideajoltv1.api.models.Idea
import com.example.ideajoltv1.utils.UserPreferencesManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Create a per-user DataStore
private val Context.savedIdeasDataStore: DataStore<Preferences> by preferencesDataStore(name = "saved_ideas")

class SavedIdeasRepository(
    private val context: Context,
    private val userPreferencesManager: UserPreferencesManager
) {
    companion object {
        private val SAVED_IDEAS_KEY_PREFIX = "saved_ideas_"
    }

    private val gson = Gson()

    // Get the current user's ID
    private suspend fun getUserId(): String? {
        return userPreferencesManager.userIdFlow.first()
    }

    // Create a key specific to the current user
    private suspend fun getSavedIdeasKey(): Preferences.Key<String> {
        val userId = getUserId() ?: "guest"
        return stringPreferencesKey("${SAVED_IDEAS_KEY_PREFIX}${userId}")
    }

    // Get all saved ideas for the current user as a Flow
    val savedIdeasFlow: Flow<List<Idea>> = context.savedIdeasDataStore.data
        .map { preferences ->
            val key = getSavedIdeasKey()
            val ideasJson = preferences[key] ?: "[]"
            val type = object : TypeToken<List<Idea>>() {}.type
            gson.fromJson(ideasJson, type) ?: emptyList()
        }

    // Check if user is logged in
    suspend fun isUserLoggedIn(): Boolean {
        return userPreferencesManager.loginStatusFlow.first()
    }

    // Save a new idea for the current user
    suspend fun saveIdea(idea: Idea): Boolean {
        // Check if user is logged in
        if (!isUserLoggedIn()) {
            return false
        }

        val key = getSavedIdeasKey()

        context.savedIdeasDataStore.edit { preferences ->
            val currentIdeasJson = preferences[key] ?: "[]"
            val type = object : TypeToken<List<Idea>>() {}.type
            val currentIdeas: List<Idea> = gson.fromJson(currentIdeasJson, type) ?: emptyList()

            val updatedIdeas = currentIdeas.toMutableList()
            val existingIndex = updatedIdeas.indexOfFirst { it.id == idea.id }

            if (existingIndex >= 0) {
                // Update existing idea
                updatedIdeas[existingIndex] = idea
            } else {
                // Add new idea
                updatedIdeas.add(idea)
            }

            preferences[key] = gson.toJson(updatedIdeas)
        }

        return true
    }

    // Delete an idea by ID
    suspend fun deleteIdea(ideaId: String) {
        val key = getSavedIdeasKey()

        context.savedIdeasDataStore.edit { preferences ->
            val currentIdeasJson = preferences[key] ?: "[]"
            val type = object : TypeToken<List<Idea>>() {}.type
            val currentIdeas: List<Idea> = gson.fromJson(currentIdeasJson, type) ?: emptyList()

            val updatedIdeas = currentIdeas.filter { it.id != ideaId }
            preferences[key] = gson.toJson(updatedIdeas)
        }
    }

    // Update notes for an idea
    suspend fun updateIdeaNotes(ideaId: String, notes: String) {
        val key = getSavedIdeasKey()

        context.savedIdeasDataStore.edit { preferences ->
            val currentIdeasJson = preferences[key] ?: "[]"
            val type = object : TypeToken<List<Idea>>() {}.type
            val currentIdeas: List<Idea> = gson.fromJson(currentIdeasJson, type) ?: emptyList()

            val updatedIdeas = currentIdeas.map {
                if (it.id == ideaId) it.copy(notes = notes) else it
            }

            preferences[key] = gson.toJson(updatedIdeas)
        }
    }
}