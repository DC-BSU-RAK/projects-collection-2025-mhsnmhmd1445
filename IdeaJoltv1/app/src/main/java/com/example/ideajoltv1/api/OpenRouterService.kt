package com.example.ideajoltv1.api

import com.example.ideajoltv1.api.models.OpenRouterRequest
import com.example.ideajoltv1.api.models.OpenRouterResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenRouterService {
    @POST("api/v1/chat/completions")
    suspend fun getCompletion(
        @Body request: OpenRouterRequest,
        @Header("Authorization") authHeader: String,
        @Header("HTTP-Referer") referer: String = "https://example.com",
        @Header("X-Title") title: String = "IdeaJolt Android App"
    ): OpenRouterResponse
}