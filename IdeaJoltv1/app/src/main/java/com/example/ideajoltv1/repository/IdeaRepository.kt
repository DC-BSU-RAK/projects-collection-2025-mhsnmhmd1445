package com.example.ideajoltv1.repository

import android.util.Log
import com.example.ideajoltv1.api.OpenRouterClient
import com.example.ideajoltv1.api.models.OpenRouterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class IdeaRepository {

    suspend fun generateIdea(topic: String, mood: String, projectType: String, aiTone: String = "Professional"): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                // Log the request parameters
                Log.d("IdeaGeneration", "Generating idea for topic: $topic, mood: $mood, projectType: $projectType, aiTone: $aiTone")

                // Create messages for the API request with tone setting
                val messages = listOf(
                    OpenRouterRequest.Message(
                        role = "system",
                        content = "You are an idea generator with a $aiTone tone. Generate creative project ideas based on the given topic, mood, and project type. Be concise and practical."
                    ),
                    OpenRouterRequest.Message(
                        role = "user",
                        content = "Generate a $mood idea for a $projectType project about $topic. Include benefits and next steps."
                    )
                )

                // Create the request
                val request = OpenRouterRequest(
                    model = "deepseek/deepseek-chat-v3-0324:free", // Free DeepSeek chat model
                    messages = messages,
                    temperature = 0.7,
                    max_tokens = 800
                )

                // Make the API call
                val response = OpenRouterClient.openRouterService.getCompletion(
                    request = request,
                    authHeader = OpenRouterClient.getAuthHeader()
                )

                // Extract the generated text from the response
                val generatedIdea = response.choices.firstOrNull()?.message?.content
                    ?: throw Exception("No response received")

                Result.success(generatedIdea)

            } catch (e: HttpException) {
                // Handle HTTP errors specifically
                Log.e("IdeaGeneration", "HTTP Error: ${e.code()} - ${e.message()}")

                val errorMessage = when (e.code()) {
                    401 -> "Authentication failed. Please check your API key."
                    429 -> "API quota exceeded. Please try again later."
                    500, 502, 503, 504 -> "Server error. Please try again later."
                    else -> "API error (${e.code()}): ${e.message()}"
                }

                Result.failure(Exception(errorMessage))

            } catch (e: Exception) {
                Log.e("IdeaGeneration", "Error generating idea", e)
                Result.failure(e)
            }
        }
    }
}