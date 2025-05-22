package com.example.ideajoltv1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ideajoltv1.databinding.ActivityIdeaResultBinding
import com.example.ideajoltv1.api.models.Idea
import com.example.ideajoltv1.repository.IdeaRepository
import com.example.ideajoltv1.repository.SavedIdeasRepository
import com.example.ideajoltv1.utils.UserPreferencesManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class IdeaResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIdeaResultBinding
    private lateinit var ideaRepository: IdeaRepository
    private lateinit var savedIdeasRepository: SavedIdeasRepository
    private lateinit var userPreferencesManager: UserPreferencesManager

    private var topic: String = ""
    private var mood: String = ""
    private var projectType: String = ""

    companion object {
        private const val EXTRA_TOPIC = "extra_topic"
        private const val EXTRA_MOOD = "extra_mood"
        private const val EXTRA_PROJECT_TYPE = "extra_project_type"

        fun newIntent(context: AppCompatActivity, topic: String, mood: String, projectType: String): Intent {
            return Intent(context, IdeaResultActivity::class.java).apply {
                putExtra(EXTRA_TOPIC, topic)
                putExtra(EXTRA_MOOD, mood)
                putExtra(EXTRA_PROJECT_TYPE, projectType)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdeaResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ideaRepository = IdeaRepository()
        userPreferencesManager = UserPreferencesManager(this)
        savedIdeasRepository = SavedIdeasRepository(this, userPreferencesManager)

        // Get extras from intent
        topic = intent.getStringExtra(EXTRA_TOPIC) ?: ""
        mood = intent.getStringExtra(EXTRA_MOOD) ?: ""
        projectType = intent.getStringExtra(EXTRA_PROJECT_TYPE) ?: ""

        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        // Display parameters
        binding.tvTopic.text = topic
        binding.tvMood.text = mood
        binding.tvProjectType.text = projectType

        // Setup click listeners
        binding.btnSave.setOnClickListener { saveIdea() }
        binding.btnNew.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        // Generate idea
        generateIdea()
    }

    private fun generateIdea() {
        setLoadingState(true)

        lifecycleScope.launch {
            try {
                // Get the AI tone from user preferences
                val userPreferencesManager = UserPreferencesManager(this@IdeaResultActivity)
                val aiTone = userPreferencesManager.aiToneFlow.first()

                val result = ideaRepository.generateIdea(topic, mood, projectType, aiTone)

                result.onSuccess { idea ->
                    binding.tvIdeaContent.text = idea
                    setLoadingState(false)
                }

                result.onFailure { error ->
                    Toast.makeText(
                        this@IdeaResultActivity,
                        "Failed to generate idea: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    setLoadingState(false)
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@IdeaResultActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                setLoadingState(false)
            }
        }
    }

    private fun saveIdea() {
        lifecycleScope.launch {
            try {
                // Check if user is logged in
                if (!savedIdeasRepository.isUserLoggedIn()) {
                    // User is not logged in, show dialog to redirect to login
                    showLoginRequiredDialog()
                    return@launch
                }

                // Create idea object
                val idea = Idea(
                    topic = topic,
                    mood = mood,
                    projectType = projectType,
                    content = binding.tvIdeaContent.text.toString()
                )

                // Save the idea
                val success = savedIdeasRepository.saveIdea(idea)

                if (success) {
                    Toast.makeText(this@IdeaResultActivity, "Idea saved successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    // This shouldn't happen since we've already checked login status
                    Toast.makeText(this@IdeaResultActivity, "Failed to save idea.", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@IdeaResultActivity,
                    "Error saving idea: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showLoginRequiredDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Login Required")
            .setMessage("You need to be logged in to save ideas. Would you like to log in now?")
            .setPositiveButton("Log In") { _, _ ->
                // Navigate to login screen
                val intent = Intent(this, LoginActivity::class.java).apply {
                    // Clear back stack to prevent going back to this screen
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.cardIdea.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.btnSave.isEnabled = !isLoading
        binding.btnNew.isEnabled = !isLoading
    }
}