package com.example.ideajoltv1

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.ideajoltv1.databinding.ActivityLoginBinding
import com.example.ideajoltv1.utils.UserPreferencesManager
import com.example.ideajoltv1.MainActivity
import kotlinx.coroutines.launch
import java.util.UUID

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userPreferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferencesManager = UserPreferencesManager(this)

        setupTextListeners()
        setupClickListeners()
    }

    private fun setupTextListeners() {
        binding.etEmail.doOnTextChanged { _, _, _, _ ->
            binding.tilEmail.error = null
        }
        binding.etPassword.doOnTextChanged { _, _, _, _ ->
            binding.tilPassword.error = null
        }
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            if (validateInputs()) {
                login()
            }
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.btnContinueGuest.setOnClickListener {
            continueAsGuest()
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Forgot password functionality coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.error_field_required)
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.error_invalid_email)
            isValid = false
        }

        val password = binding.etPassword.text.toString()
        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.error_field_required)
            isValid = false
        }

        return isValid
    }

    private fun login() {
        setLoadingState(true)

        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        lifecycleScope.launch {
            try {
                kotlinx.coroutines.delay(1500)

                if (password.length < 6) {
                    binding.tilPassword.error = getString(R.string.error_password_short)
                    setLoadingState(false)
                    return@launch
                }

                val userId = UUID.randomUUID().toString()
                val username = email.substringBefore('@')

                userPreferencesManager.saveLoginStatus(true)
                userPreferencesManager.saveUserDetails(userId, email, username)

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } catch (e: Exception) {
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.error_login_failed),
                    Toast.LENGTH_SHORT
                ).show()
                setLoadingState(false)
            }
        }
    }

    private fun continueAsGuest() {
        setLoadingState(true)

        lifecycleScope.launch {
            try {
                kotlinx.coroutines.delay(800)

                val guestId = "guest_${System.currentTimeMillis()}"
                userPreferencesManager.saveLoginStatus(true)
                userPreferencesManager.saveUserDetails(
                    userId = guestId,
                    email = "guest@ideajolt.com",
                    username = "Guest"
                )

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error continuing as guest", Toast.LENGTH_SHORT).show()
                setLoadingState(false)
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.btnLogin.isEnabled = !isLoading
        binding.btnContinueGuest.isEnabled = !isLoading
        binding.tvSignup.isClickable = !isLoading
        binding.tvForgotPassword.isClickable = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
