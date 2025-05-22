/*package com.example.ideajoltv1.api

import com.example.ideajoltv1.api.models.ApiRequest
import com.example.ideajoltv1.api.models.ApiResponse
import com.example.ideajoltv1.api.models.Choice
import com.example.ideajoltv1.api.models.Message
import com.example.ideajoltv1.api.models.Usage
import kotlinx.coroutines.delay

class MockOpenAIService : OpenAIService {
    override suspend fun getCompletion(request: ApiRequest): ApiResponse {
        // Simulate network delay
        delay(1500)

        // Extract the topic, mood, and project type from the request
        val content = request.messages.last().content
        val topic = extractTopic(content)
        val mood = extractMood(content)
        val projectType = extractProjectType(content)

        // Generate a mock response based on the inputs
        val ideaContent = generateMockIdea(topic, mood, projectType)

        return ApiResponse(
            choices = listOf(
                Choice(
                    finish_reason = "stop",
                    index = 0,
                    message = Message(
                        content = ideaContent,
                        role = "assistant"
                    )
                )
            ),
            created = System.currentTimeMillis() / 1000,
            id = "mock-id-${System.currentTimeMillis()}",
            model = "gpt-3.5-turbo-mock",
            usage = Usage(
                completion_tokens = ideaContent.length / 4,
                prompt_tokens = content.length / 4,
                total_tokens = (ideaContent.length + content.length) / 4
            )
        )
    }

    private fun extractTopic(content: String): String {
        val pattern = "Topic: (.*?)(\\.|\n|,)".toRegex()
        val match = pattern.find(content)
        return match?.groupValues?.get(1) ?: "technology"
    }

    private fun extractMood(content: String): String {
        val pattern = "Mood: (.*?)(\\.|\n|,)".toRegex()
        val match = pattern.find(content)
        return match?.groupValues?.get(1) ?: "creative"
    }

    private fun extractProjectType(content: String): String {
        val pattern = "Project Type: (.*?)(\\.|\n|,)".toRegex()
        val match = pattern.find(content)
        return match?.groupValues?.get(1) ?: "technology"
    }

    private fun generateMockIdea(topic: String, mood: String, projectType: String): String {
        // List of templates for different project types and moods
        val templates = mapOf(
            "business" to listOf(
                "A subscription service for $topic enthusiasts that delivers monthly curated products.",
                "An on-demand marketplace connecting $topic experts with clients who need help.",
                "A SaaS platform that helps businesses manage their $topic needs more efficiently."
            ),
            "technology" to listOf(
                "A mobile app that uses AI to optimize $topic-related tasks for users.",
                "An IoT device that tracks and analyzes $topic data in real-time.",
                "A blockchain solution for verifying authenticity in the $topic industry."
            ),
            "art" to listOf(
                "An immersive exhibition that explores $topic through interactive installations.",
                "A collaborative online platform where artists can create $topic-themed works together.",
                "A series of workshops teaching innovative techniques inspired by $topic."
            ),
            "education" to listOf(
                "A gamified learning platform focused on teaching $topic to beginners.",
                "A peer-to-peer knowledge exchange program centered around $topic expertise.",
                "An augmented reality tool that makes learning about $topic more engaging."
            )
        )

        // Get templates for the project type or default to technology
        val projectTemplates = templates[projectType.toLowerCase()] ?: templates["technology"]!!

        // Select a random template
        val template = projectTemplates.random()

        // Generate additional details based on mood
        val moodDetails = when (mood.toLowerCase()) {
            "creative" -> "This innovative idea pushes boundaries by combining unexpected elements to create something truly original in the $topic space."
            "serious" -> "This professional solution addresses critical challenges in the $topic industry with a methodical, evidence-based approach."
            "fun" -> "This playful concept makes $topic more enjoyable and accessible to broader audiences through gamification and social elements."
            "innovative" -> "This cutting-edge approach leverages emerging technologies to revolutionize how people interact with $topic."
            else -> "This idea offers a fresh perspective on $topic that could appeal to various stakeholders."
        }

        // Generate benefits
        val benefits = listOf(
            "Increases engagement with $topic by up to 45%",
            "Reduces time spent on $topic-related tasks by half",
            "Creates new opportunities for collaboration in the $topic space",
            "Makes $topic more accessible to underserved communities",
            "Transforms how people perceive and interact with $topic"
        ).shuffled().take(2).joinToString("\n- ", prefix = "\n\nKey Benefits:\n- ")

        // Generate next steps
        val nextSteps = listOf(
            "Conduct market research to validate interest in this $topic concept",
            "Develop a minimum viable product (MVP) focusing on core $topic features",
            "Identify potential partners or collaborators in the $topic industry",
            "Create a prototype to test user interaction with the $topic solution",
            "Outline a business model that monetizes the $topic idea sustainably"
        ).shuffled().take(3).joinToString("\n2. ", prefix = "\n\nNext Steps:\n1. ").plus("\n3. Launch and iterate based on user feedback")

        // Return the completed idea
        return "${template}\n\n${moodDetails}${benefits}${nextSteps}"
    }
}*/