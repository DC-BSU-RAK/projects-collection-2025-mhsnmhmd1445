package com.example.ideajoltv1.api.models

import java.util.UUID

data class Idea(
    val id: String = UUID.randomUUID().toString(),
    val topic: String,
    val mood: String,
    val projectType: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)