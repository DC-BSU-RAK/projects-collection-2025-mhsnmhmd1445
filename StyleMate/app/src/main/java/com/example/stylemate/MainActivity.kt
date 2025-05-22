package com.example.stylemate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import com.google.gson.Gson
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    companion object {
        const val RECOMMENDATION_REQUEST_CODE = 1001
    }

    // UI elements
    private lateinit var moodChipGroup: ChipGroup
    private lateinit var occasionToggleGroup: MaterialButtonToggleGroup
    private lateinit var styleButton: MaterialButton
    private lateinit var fabInfo: FloatingActionButton
    private lateinit var styleSlider: Slider

    // Business logic
    private val styleCalculator = StyleCalculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        initViews()

        // Set up listeners
        setUpListeners()
    }

    private fun initViews() {
        moodChipGroup = findViewById(R.id.mood_chip_group)
        occasionToggleGroup = findViewById(R.id.radio_group_occasion)
        styleButton = findViewById(R.id.btn_style_me)
        fabInfo = findViewById(R.id.fab_info)
        styleSlider = findViewById(R.id.slider_style_level)

        // Select first mood chip by default
        val firstChip = moodChipGroup.getChildAt(0) as Chip
        firstChip.isChecked = true

        // Set default occasion
        occasionToggleGroup.check(R.id.radio_casual)

        // Call the enhance selection state method to set up the listeners for selection changes
        enhanceSelectionState()
    }

    private fun setUpListeners() {
        // Style Me button click listener
        styleButton.setOnClickListener {
            generateStyleRecommendation()
        }

        // Info button click listener
        fabInfo.setOnClickListener {
            showInfoModal()
        }
    }

    private fun generateStyleRecommendation() {
        // Get selected mood
        val selectedMoodChipId = moodChipGroup.checkedChipId
        if (selectedMoodChipId == View.NO_ID) {
            Toast.makeText(this, "Please select your mood", Toast.LENGTH_SHORT).show()
            return
        }
        val selectedMoodChip = findViewById<Chip>(selectedMoodChipId)
        val selectedMood = selectedMoodChip.text.toString()

        // Get selected occasion from MaterialButtonToggleGroup
        val selectedOccasionId = occasionToggleGroup.checkedButtonId
        if (selectedOccasionId == View.NO_ID) {
            Toast.makeText(this, "Please select an occasion", Toast.LENGTH_SHORT).show()
            return
        }
        val selectedOccasionButton = findViewById<MaterialButton>(selectedOccasionId)
        val selectedOccasion = selectedOccasionButton.text.toString()

        // Calculate recommendations
        val recommendation = styleCalculator.calculateRecommendation(selectedMood, selectedOccasion)

        // Convert to JSON for passing to the next activity
        val gson = Gson()
        val recommendationJson = gson.toJson(recommendation)

        // Launch the result activity with the recommendation data
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("recommendation", recommendationJson)
        startActivityForResult(intent, RECOMMENDATION_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Reset UI when returning from the result activity
        if (requestCode == RECOMMENDATION_REQUEST_CODE) {
            resetUI()
        }
    }

    private fun resetUI() {
        // Reset mood selection to first chip
        val firstChip = moodChipGroup.getChildAt(0) as Chip
        moodChipGroup.clearCheck()
        firstChip.isChecked = true

        // Reset occasion to "Casual"
        occasionToggleGroup.check(R.id.radio_casual)

        // Reset style level to middle (3)
        styleSlider.value = 3f
    }

    private fun showInfoModal() {
        val infoModalFragment = InfoModelFragment.newInstance()
        infoModalFragment.show(supportFragmentManager, InfoModelFragment.TAG)
    }

    override fun onResume() {
        super.onResume()
        // Reset UI when activity is resumed
        resetUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Tell the Application Manager to reset everything when the app is being closed
        if (isFinishing) {
            ApplicationManager.resetAllOnExit()
        }
    }

    private fun enhanceSelectionState() {
        // For mood chips - make selection more visible
        moodChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            // Reset all chips to unselected style
            for (i in 0 until group.childCount) {
                val chip = group.getChildAt(i) as Chip
                chip.elevation = 0f
                // Make sure chip uses the background color selector
                chip.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.card_background)
                )
            }

            // Enhance selected chip
            if (checkedIds.isNotEmpty()) {
                val selectedChip = findViewById<Chip>(checkedIds[0])
                selectedChip.elevation = 4f // Add elevation to selected chip
                // Set the background to the selected color
                selectedChip.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.chip_selected_background)
                )
            }
        }

        // For occasion buttons - completely different approach
        // Set individual listeners for each button for more precise control
        for (i in 0 until occasionToggleGroup.childCount) {
            val button = occasionToggleGroup.getChildAt(i) as MaterialButton

            // Clear any existing listeners
            button.setOnClickListener(null)

            // Set a new click listener
            button.setOnClickListener {
                // Manually select this button in the toggle group
                occasionToggleGroup.check(button.id)

                // Update the appearance of ALL buttons
                updateOccasionButtonAppearance()
            }
        }

        // Initial setup of appearances
        updateOccasionButtonAppearance()

        // Apply initial selection for mood chips
        if (moodChipGroup.checkedChipId != View.NO_ID) {
            val selectedChip = findViewById<Chip>(moodChipGroup.checkedChipId)
            selectedChip.elevation = 4f
            selectedChip.chipBackgroundColor = ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.chip_selected_background)
            )
        }
    }

    private fun updateOccasionButtonAppearance() {
        val checkedId = occasionToggleGroup.checkedButtonId

        for (i in 0 until occasionToggleGroup.childCount) {
            val button = occasionToggleGroup.getChildAt(i) as MaterialButton

            if (button.id == checkedId) {
                // Selected button
                button.setStrokeColorResource(R.color.transparent)
                button.strokeWidth = 0

                // No background color change
                button.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)

                // Change icon and text color to primary
                button.iconTint = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.primary)
                )
                button.setTextColor(ContextCompat.getColor(this, R.color.primary))

                // Slight scale up
                button.scaleX = 1.05f
                button.scaleY = 1.05f
            } else {
                // Unselected button
                button.setStrokeColorResource(R.color.transparent)
                button.strokeWidth = 0

                // Transparent background
                button.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)

                // Reset icon and text color
                button.iconTint = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.text_primary)
                )
                button.setTextColor(ContextCompat.getColor(this, R.color.text_primary))

                // Reset scale
                button.scaleX = 1.0f
                button.scaleY = 1.0f
            }
        }
    }
}