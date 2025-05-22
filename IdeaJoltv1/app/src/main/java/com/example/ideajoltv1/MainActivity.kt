package com.example.ideajoltv1

import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ideajoltv1.databinding.ActivityMainBinding
import com.example.ideajoltv1.utils.UserPreferencesManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userPreferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            userPreferencesManager = UserPreferencesManager(this)

            setupUI()
            setupClickListeners()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error during onCreate", e)
            Toast.makeText(this, "Error initializing app: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupUI() {
        try {
            // Set up the toolbar
            setSupportActionBar(binding.toolbar)

            // Get and display username and load default settings
            lifecycleScope.launch {
                try {
                    // Display username
                    val username = userPreferencesManager.usernameFlow.first()
                    binding.tvUsername.text = username ?: "Guest"

                    // Load default project type
                    val defaultProjectType = userPreferencesManager.preferredProjectTypeFlow.first()
                    when (defaultProjectType) {
                        UserPreferencesManager.PROJECT_TYPE_BUSINESS ->
                            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_business).isChecked = true
                        UserPreferencesManager.PROJECT_TYPE_ART ->
                            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_art).isChecked = true
                        UserPreferencesManager.PROJECT_TYPE_TECHNOLOGY ->
                            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_technology).isChecked = true
                        UserPreferencesManager.PROJECT_TYPE_EDUCATION ->
                            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_education).isChecked = true
                    }

                    // Load default mood
                    val defaultMood = userPreferencesManager.preferredMoodFlow.first()
                    when (defaultMood) {
                        UserPreferencesManager.MOOD_CREATIVE ->
                            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_creative).isChecked = true
                        UserPreferencesManager.MOOD_SERIOUS ->
                            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_serious).isChecked = true
                        UserPreferencesManager.MOOD_FUN ->
                            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_fun).isChecked = true
                        UserPreferencesManager.MOOD_INNOVATIVE ->
                            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_innovative).isChecked = true
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error loading default settings", e)
                    binding.tvUsername.text = "Guest"
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in setupUI", e)
        }
    }

    private fun setupClickListeners() {
        try {
            // Logout button
            binding.btnLogout.setOnClickListener {
                logout()
            }

            // Generate button
            binding.cardInput.findViewById<Button>(R.id.btn_generate).setOnClickListener {
                generateIdea()
            }
            binding.btnViewSavedIdeas.setOnClickListener {
                lifecycleScope.launch {
                    if (userPreferencesManager.loginStatusFlow.first()) {
                        // User is logged in
                        startActivity(Intent(this@MainActivity, SavedIdeasActivity::class.java))
                    } else {
                        // Show login required dialog
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("Login Required")
                            .setMessage("You need to be logged in to view saved ideas. Would you like to log in now?")
                            .setPositiveButton("Log In") { _, _ ->
                                // Navigate to login screen
                                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            }
                            .setNegativeButton("Cancel", null)
                            .show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in setupClickListeners", e)
        }

    }


    private fun generateIdea() {
        // Get the topic from input
        val topic = binding.cardInput.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_topic).text.toString().trim()

        if (topic.isEmpty()) {
            Toast.makeText(this, "Please enter a topic", Toast.LENGTH_SHORT).show()
            return
        }

        // Get selected mood
        val mood = when {
            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_creative).isChecked -> "Creative"
            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_serious).isChecked -> "Serious"
            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_fun).isChecked -> "Fun"
            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_innovative).isChecked -> "Innovative"
            else -> {
                Toast.makeText(this, "Please select a mood", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Get selected project type
        val projectType = when {
            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_business).isChecked -> "Business"
            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_art).isChecked -> "Art"
            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_technology).isChecked -> "Technology"
            binding.cardInput.findViewById<com.google.android.material.chip.Chip>(R.id.chip_education).isChecked -> "Education"
            else -> {
                Toast.makeText(this, "Please select a project type", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Navigate to idea result activity
        val intent = IdeaResultActivity.newIntent(this, topic, mood, projectType)
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            // User wants to generate a new idea, clear the form
            binding.cardInput.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_topic).text?.clear()

            // Uncheck all chips
            binding.cardInput.findViewById<com.google.android.material.chip.ChipGroup>(R.id.cg_mood).clearCheck()
            binding.cardInput.findViewById<com.google.android.material.chip.ChipGroup>(R.id.cg_project_type).clearCheck()
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            try {
                userPreferencesManager.clearUserData()

                Toast.makeText(this@MainActivity, "Logged out successfully", Toast.LENGTH_SHORT).show()

                // Navigate back to login screen
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Log.e("MainActivity", "Error during logout", e)
                Toast.makeText(this@MainActivity, "Error logging out: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // In MainActivity.kt
    override fun onCreateOptionsMenu(menu:  Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}