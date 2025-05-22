package com.example.stylemate

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val splashTimeOut: Long = 2000 // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Log to verify activity starting
        Log.d("SplashActivity", "Starting SplashActivity")

        // Set content view
        setContentView(R.layout.activity_splash)

        // Get references to views
        val logoText = findViewById<TextView>(R.id.logo_text)
        val taglineText = findViewById<TextView>(R.id.tagline_text)

        // Make text fully visible first (in case animations don't work)
        logoText.alpha = 1.0f
        taglineText.alpha = 1.0f

        // Log view status
        Log.d("SplashActivity", "logoText found: ${logoText != null}, text: ${logoText.text}")
        Log.d("SplashActivity", "taglineText found: ${taglineText != null}, text: ${taglineText.text}")

        // Create fade-in animation
        try {
            val fadeIn = AlphaAnimation(0.0f, 1.0f).apply {
                duration = 1000
                fillAfter = true
            }

            // Start animation
            logoText.startAnimation(fadeIn)

            // Wait for logo animation to finish before starting tagline animation
            fadeIn.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    val taglineFadeIn = AlphaAnimation(0.0f, 1.0f).apply {
                        duration = 500
                        fillAfter = true
                    }
                    taglineText.startAnimation(taglineFadeIn)
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        } catch (e: Exception) {
            // If animation fails, log the error but continue
            Log.e("SplashActivity", "Animation error", e)
        }

        // Navigate to MainActivity after timeout
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("SplashActivity", "Starting MainActivity")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTimeOut)
    }
}