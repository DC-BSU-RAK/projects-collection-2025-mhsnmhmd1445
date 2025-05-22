// Top-level build file where you can add configuration options common to all sub-projects/modules.
import java.util.Properties

plugins {
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

// Read the local.properties file
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}
val openAiApiKey: String = localProperties.getProperty("OPENAI_API_KEY") ?: ""

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}