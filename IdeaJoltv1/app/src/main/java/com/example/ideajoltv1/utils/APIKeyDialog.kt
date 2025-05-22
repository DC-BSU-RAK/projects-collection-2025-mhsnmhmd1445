package com.example.ideajoltv1.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.ideajoltv1.R
import com.google.android.material.textfield.TextInputEditText

class APIKeyDialog {
    companion object {
        fun show(context: Context, onApiKeyEntered: (String) -> Unit) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_api_key, null)
            val apiKeyEditText = dialogView.findViewById<TextInputEditText>(R.id.et_api_key)

            AlertDialog.Builder(context)
                .setTitle("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")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    val apiKey = apiKeyEditText.text.toString().trim()
                    if (apiKey.isNotEmpty()) {
                        onApiKeyEntered(apiKey)
                    }
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }
}