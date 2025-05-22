package com.example.ideajoltv1.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object OpenRouterClient {
    private const val API_KEY = "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" // Replace with your actual OpenRouter API key
    private const val BASE_URL = "https://openrouter.ai/"

    // Create OkHttp Client
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    // Create Retrofit Instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create Service
    val openRouterService: OpenRouterService = retrofit.create(OpenRouterService::class.java)

    // Get authorization header
    fun getAuthHeader(): String = "Bearer $API_KEY"
}