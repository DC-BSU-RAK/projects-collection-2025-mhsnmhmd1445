package com.example.ideajoltv1

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ideajoltv1.adapters.SavedIdeasAdapter
import com.example.ideajoltv1.api.models.Idea
import com.example.ideajoltv1.databinding.ActivitySavedIdeasBinding
import com.example.ideajoltv1.repository.SavedIdeasRepository
import com.example.ideajoltv1.utils.UserPreferencesManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SavedIdeasActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedIdeasBinding
    private lateinit var savedIdeasRepository: SavedIdeasRepository
    private lateinit var userPreferencesManager: UserPreferencesManager
    private lateinit var adapter: SavedIdeasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedIdeasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferencesManager = UserPreferencesManager(this)
        savedIdeasRepository = SavedIdeasRepository(this, userPreferencesManager)

        setupToolbar()

        // Check if user is logged in
        lifecycleScope.launch {
            if (!savedIdeasRepository.isUserLoggedIn()) {
                showLoginRequiredDialog()
            } else {
                setupRecyclerView()
                loadSavedIdeas()
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupRecyclerView() {
        adapter = SavedIdeasAdapter(
            onIdeaClicked = { showIdeaDetailsDialog(it) },
            onDeleteClicked = { showDeleteConfirmationDialog(it) },
            onEditNotesClicked = { showEditNotesDialog(it) }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun loadSavedIdeas() {
        lifecycleScope.launch {
            try {
                val ideas = savedIdeasRepository.savedIdeasFlow.first()

                if (ideas.isEmpty()) {
                    binding.tvNoIdeas.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.tvNoIdeas.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    adapter.submitList(ideas.sortedByDescending { it.timestamp })
                }
            } catch (e: Exception) {
                binding.tvNoIdeas.text = "Error loading saved ideas"
                binding.tvNoIdeas.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }
    }

    private fun showLoginRequiredDialog() {
        AlertDialog.Builder(this)
            .setTitle("Login Required")
            .setMessage("You need to be logged in to view saved ideas.")
            .setPositiveButton("Log In") { _, _ ->
                // Navigate to login screen
                val intent = Intent(this, LoginActivity::class.java).apply {
                    // Clear back stack to prevent going back to this screen
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            .setCancelable(false) // User must log in or go back
            .show()
    }

    private fun showIdeaDetailsDialog(idea: Idea) {
        MaterialAlertDialogBuilder(this)
            .setTitle(idea.topic)
            .setMessage(idea.content)
            .setPositiveButton("Close", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(idea: Idea) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Idea")
            .setMessage("Are you sure you want to delete this idea about ${idea.topic}?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    savedIdeasRepository.deleteIdea(idea.id)
                    loadSavedIdeas()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditNotesDialog(idea: Idea) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_notes, null)
        val etNotes = dialogView.findViewById<TextInputEditText>(R.id.et_notes)
        etNotes.setText(idea.notes)

        AlertDialog.Builder(this)
            .setTitle("Edit Notes")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val notes = etNotes.text.toString().trim()
                lifecycleScope.launch {
                    savedIdeasRepository.updateIdeaNotes(idea.id, notes)
                    loadSavedIdeas()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}