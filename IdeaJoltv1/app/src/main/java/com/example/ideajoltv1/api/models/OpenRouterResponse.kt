package com.example.ideajoltv1.api.models

data class OpenRouterResponse(
    val id: String,
    val choices: List<Choice>,
    val created: Long,
    val model: String,
    val usage: Usage
) {
    data class Choice(
        val finish_reason: String,
        val index: Int,
        val message: Message
    )

    data class Message(
        val content: String,
        val role: String
    )

    data class Usage(
        val completion_tokens: Int,
        val prompt_tokens: Int,
        val total_tokens: Int
    )
}