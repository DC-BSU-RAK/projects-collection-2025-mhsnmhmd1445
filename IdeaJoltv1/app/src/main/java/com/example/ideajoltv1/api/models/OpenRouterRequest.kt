package com.example.ideajoltv1.api.models

data class OpenRouterRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double = 0.7,
    val max_tokens: Int = 800
) {
    data class Message(
        val role: String,
        val content: String
    )
}