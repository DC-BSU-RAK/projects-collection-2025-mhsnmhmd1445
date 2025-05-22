package com.example.ideajoltv1.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Log the API key status
        Log.d("ApiKeyInterceptor", "API Key: ${apiKey.take(5)}... (length: ${apiKey.length})")

        // Create new request with headers
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .build()

        return chain.proceed(newRequest)
    }
}