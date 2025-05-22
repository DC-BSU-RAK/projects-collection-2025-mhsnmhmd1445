package com.example.ideajoltv1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.ideajoltv1.databinding.ActivitySettingsBinding
import com.example.ideajoltv1.utils.UserPreferencesManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var userPreferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferencesManager = UserPreferencesManager(this)

        setupToolbar()
        loadCurrentSettings()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun loadCurrentSettings() {
        lifecycleScope.launch {
            try {
                // Set current AI tone
                val aiTone = userPreferencesManager.aiToneFlow.first()
                when (aiTone) {
                    UserPreferencesManager.AI_TONE_PROFESSIONAL -> binding.rgAiTone.check(R.id.rb_professional)
                    UserPreferencesManager.AI_TONE_FUNNY -> binding.rgAiTone.check(R.id.rb_funny)
                    UserPreferencesManager.AI_TONE_INSPIRING -> binding.rgAiTone.check(R.id.rb_inspiring)
                }

                // Set current theme mode
                val themeMode = userPreferencesManager.themeModeFlow.first()
                binding.switchTheme.isChecked = themeMode == UserPreferencesManager.THEME_LIGHT

                // Set current preferred project type
                val projectType = userPreferencesManager.preferredProjectTypeFlow.first()
                when (projectType) {
                    UserPreferencesManager.PROJECT_TYPE_BUSINESS -> binding.rgProjectType.check(R.id.rb_business)
                    UserPreferencesManager.PROJECT_TYPE_ART -> binding.rgProjectType.check(R.id.rb_art)
                    UserPreferencesManager.PROJECT_TYPE_TECHNOLOGY -> binding.rgProjectType.check(R.id.rb_technology)
                    UserPreferencesManager.PROJECT_TYPE_EDUCATION -> binding.rgProjectType.check(R.id.rb_education)
                }

                // Set current preferred mood
                val mood = userPreferencesManager.preferredMoodFlow.first()
                when (mood) {
                    UserPreferencesManager.MOOD_CREATIVE -> binding.rgMood.check(R.id.rb_creative)
                    UserPreferencesManager.MOOD_SERIOUS -> binding.rgMood.check(R.id.rb_serious)
                    UserPreferencesManager.MOOD_FUN -> binding.rgMood.check(R.id.rb_fun)
                    UserPreferencesManager.MOOD_INNOVATIVE -> binding.rgMood.check(R.id.rb_innovative)
                }

            } catch (e: Exception) {
                Toast.makeText(this@SettingsActivity, "Error loading settings", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupClickListeners() {
        // AI Tone radio group
        binding.rgAiTone.setOnCheckedChangeListener { _, checkedId ->
            val tone = when (checkedId) {
                R.id.rb_professional -> UserPreferencesManager.AI_TONE_PROFESSIONAL
                R.id.rb_funny -> UserPreferencesManager.AI_TONE_FUNNY
                R.id.rb_inspiring -> UserPreferencesManager.AI_TONE_INSPIRING
                else -> UserPreferencesManager.AI_TONE_PROFESSIONAL
            }
            lifecycleScope.launch {
                userPreferencesManager.saveAiTone(tone)
                Toast.makeText(this@SettingsActivity, "AI tone updated", Toast.LENGTH_SHORT).show()
            }
        }

        // Theme switch
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            val theme = if (isChecked) {
                UserPreferencesManager.THEME_LIGHT
            } else {
                UserPreferencesManager.THEME_DARK
            }

            lifecycleScope.launch {
                userPreferencesManager.saveThemeMode(theme)
                applyThemeAndRestart(theme)
            }
        }

        // Project type radio group
        binding.rgProjectType.setOnCheckedChangeListener { _, checkedId ->
            val projectType = when (checkedId) {
                R.id.rb_business -> UserPreferencesManager.PROJECT_TYPE_BUSINESS
                R.id.rb_art -> UserPreferencesManager.PROJECT_TYPE_ART
                R.id.rb_technology -> UserPreferencesManager.PROJECT_TYPE_TECHNOLOGY
                R.id.rb_education -> UserPreferencesManager.PROJECT_TYPE_EDUCATION
                else -> UserPreferencesManager.PROJECT_TYPE_TECHNOLOGY
            }
            lifecycleScope.launch {
                userPreferencesManager.savePreferredProjectType(projectType)
                Toast.makeText(this@SettingsActivity, "Default project type updated", Toast.LENGTH_SHORT).show()
            }
        }

        // Mood radio group
        binding.rgMood.setOnCheckedChangeListener { _, checkedId ->
            val mood = when (checkedId) {
                R.id.rb_creative -> UserPreferencesManager.MOOD_CREATIVE
                R.id.rb_serious -> UserPreferencesManager.MOOD_SERIOUS
                R.id.rb_fun -> UserPreferencesManager.MOOD_FUN
                R.id.rb_innovative -> UserPreferencesManager.MOOD_INNOVATIVE
                else -> UserPreferencesManager.MOOD_CREATIVE
            }
            lifecycleScope.launch {
                userPreferencesManager.savePreferredMood(mood)
                Toast.makeText(this@SettingsActivity, "Default mood updated", Toast.LENGTH_SHORT).show()
            }
        }

        // Save button
        binding.btnSaveSettings.setOnClickListener {
            Toast.makeText(this, "All settings saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun applyThemeAndRestart(theme: String) {
        val nightMode = when (theme) {
            UserPreferencesManager.THEME_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            UserPreferencesManager.THEME_DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        // Set the theme
        AppCompatDelegate.setDefaultNightMode(nightMode)

        // Restart the activity to apply the new theme
        val intent = intent
        finish()
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}