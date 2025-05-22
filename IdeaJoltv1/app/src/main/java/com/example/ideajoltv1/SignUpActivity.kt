package com.example.ideajoltv1


import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.ideajoltv1.databinding.ActivitySignupBinding
import com.example.ideajoltv1.utils.UserPreferencesManager
import kotlinx.coroutines.launch
import java.util.UUID

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var userPreferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferencesManager = UserPreferencesManager(this)

        setupTextListeners()
        setupClickListeners()
    }

    private fun setupTextListeners() {
        // Clear errors on text change
        binding.etUsername.doOnTextChanged { _, _, _, _ ->
            binding.tilUsername.error = null
        }
        binding.etEmail.doOnTextChanged { _, _, _, _ ->
            binding.tilEmail.error = null
        }
        binding.etPassword.doOnTextChanged { _, _, _, _ ->
            binding.tilPassword.error = null
        }
        binding.etConfirmPassword.doOnTextChanged { _, _, _, _ ->
            binding.tilConfirmPassword.error = null
        }
    }

    private fun setupClickListeners() {
        // Sign up button click
        binding.btnSignup.setOnClickListener {
            if (validateInputs()) {
                signUp()
            }
        }

        // Login text click
        binding.tvLogin.setOnClickListener {
            finish() // Go back to login screen
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Validate username
        val username = binding.etUsername.text.toString().trim()
        if (username.isEmpty()) {
            binding.tilUsername.error = getString(R.string.error_field_required)
            isValid = false
        }

        // Validate email
        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.error_field_required)
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.error_invalid_email)
            isValid = false
        }

        // Validate password
        val password = binding.etPassword.text.toString()
        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.error_field_required)
            isValid = false
        } else if (password.length < 6) {
            binding.tilPassword.error = getString(R.string.error_password_short)
            isValid = false
        }

        // Validate confirm password
        val confirmPassword = binding.etConfirmPassword.text.toString()
        if (confirmPassword.isEmpty()) {
            binding.tilConfirmPassword.error = getString(R.string.error_field_required)
            isValid = false
        } else if (password != confirmPassword) {
            binding.tilConfirmPassword.error = getString(R.string.error_passwords_dont_match)
            isValid = false
        }

        return isValid
    }

    private fun signUp() {
        // Show loading state
        setLoadingState(true)

        // Get input values
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        // For demo purposes (without Firebase), we'll just simulate a successful sign-up
        // Normally, you would create account with Firebase here
        lifecycleScope.launch {
            try {
                // Simulate network delay
                kotlinx.coroutines.delay(1500)

                // Generate a random user ID (would normally come from Firebase)
                val userId = UUID.randomUUID().toString()

                // Save user data in DataStore
                userPreferencesManager.saveLoginStatus(true)
                userPreferencesManager.saveUserDetails(userId, email, username)

                // Navigate to Main Activity
                startActivity(Intent(this@SignupActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                finish()
            } catch (e: Exception) {
                // Handle error
                Toast.makeText(this@SignupActivity, getString(R.string.error_signup_failed), Toast.LENGTH_SHORT).show()
                setLoadingState(false)
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.btnSignup.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}