package com.example.stylemate

import android.widget.RadioGroup
import java.util.*

class StyleCalculator {

    companion object {
        // Mood constants
        const val MOOD_CHILL = "😌 Chill"
        const val MOOD_CONFIDENT = "😎 Confident"
        const val MOOD_LAZY = "😴 Lazy"
        const val MOOD_SHARP = "🧠 Sharp"

        // Occasion constants
        const val OCCASION_CASUAL = "Casual"
        const val OCCASION_WORK = "Work"
        const val OCCASION_DATE = "Date"
        const val OCCASION_GYM = "Gym"

        // Time periods of day
        const val MORNING = "morning"
        const val AFTERNOON = "afternoon"
        const val EVENING = "evening"
        const val NIGHT = "night"
    }

    /**
     * Calculate style recommendations based on mood, occasion, and current time
     */
    fun calculateRecommendation(mood: String, occasion: String): StyleRecommendation {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val timeOfDay = when {
            currentHour in 5..11 -> MORNING
            currentHour in 12..16 -> AFTERNOON
            currentHour in 17..21 -> EVENING
            else -> NIGHT
        }

        return StyleRecommendation(
            groomingTip = getGroomingTip(mood, occasion, timeOfDay),
            outfitSuggestion = getOutfitSuggestion(mood, occasion, timeOfDay),
            beardTip = getBeardTip(mood, occasion),
            fragrance = getFragranceSuggestion(mood, occasion, timeOfDay),
            quote = getConfidenceQuote(mood)
        )
    }

    private fun getGroomingTip(mood: String, occasion: String, timeOfDay: String): String {
        return when {
            // Chill mood
            mood == MOOD_CHILL && occasion == OCCASION_CASUAL ->
                "Keep it simple - just wash your face with cold water and apply a light moisturizer for a refreshed look."
            mood == MOOD_CHILL && occasion == OCCASION_WORK ->
                "Use a tinted moisturizer with SPF to look put together with minimal effort."
            mood == MOOD_CHILL && occasion == OCCASION_DATE ->
                "Spritz some water on your hair and rework your hairstyle with your fingers for an effortlessly tousled look."
            mood == MOOD_CHILL && occasion == OCCASION_GYM ->
                "Skip products entirely - just a quick rinse before heading out is all you need."

            // Confident mood
            mood == MOOD_CONFIDENT && occasion == OCCASION_CASUAL ->
                "Try a new hairstyle today - slick back your hair with a medium-hold pomade for a bold look."
            mood == MOOD_CONFIDENT && occasion == OCCASION_WORK ->
                "Go for a clean-shaven look today to appear sharp and commanding in the office."
            mood == MOOD_CONFIDENT && occasion == OCCASION_DATE ->
                "Use a facial scrub before heading out to give your skin a healthy glow that she'll notice."
            mood == MOOD_CONFIDENT && occasion == OCCASION_GYM ->
                "Apply a cooling face wash before your workout to ensure you look fresh even after intense training."

            // Lazy mood
            mood == MOOD_LAZY && occasion == OCCASION_CASUAL ->
                "Dry shampoo is your friend today - a quick spray will refresh your hair with minimal effort."
            mood == MOOD_LAZY && occasion == OCCASION_WORK ->
                "Keep a travel grooming kit at work for quick touch-ups if you're running late."
            mood == MOOD_LAZY && occasion == OCCASION_DATE ->
                "Focus on the essentials: clean teeth, fresh breath, and a quick face wash - sometimes less is more."
            mood == MOOD_LAZY && occasion == OCCASION_GYM ->
                "Just splash some cold water on your face - you'll be sweating soon anyway."

            // Sharp mood
            mood == MOOD_SHARP && occasion == OCCASION_CASUAL ->
                "Try a three-step routine: cleanse, tone, and moisturize for a noticeably improved complexion."
            mood == MOOD_SHARP && occasion == OCCASION_WORK ->
                "Use a matte styling clay for a professional hairstyle that stays put all day without looking overly done."
            mood == MOOD_SHARP && occasion == OCCASION_DATE ->
                "Exfoliate and apply a hydrating face mask before getting ready to ensure your skin looks its absolute best."
            mood == MOOD_SHARP && occasion == OCCASION_GYM ->
                "Apply a pre-workout face cleanser with tea tree oil to prevent post-workout breakouts."

            // Default
            else -> "Wash your face with cold water, moisturize, and style your hair with a small amount of product."
        }
    }

    private fun getOutfitSuggestion(mood: String, occasion: String, timeOfDay: String): String {
        return when {
            // Casual occasion
            occasion == OCCASION_CASUAL && mood == MOOD_CHILL && (timeOfDay == MORNING || timeOfDay == AFTERNOON) ->
                "Light wash jeans with a simple white tee and your favorite sneakers - effortless weekend style."
            occasion == OCCASION_CASUAL && mood == MOOD_CHILL && (timeOfDay == EVENING || timeOfDay == NIGHT) ->
                "Joggers and a soft henley with slip-on shoes - perfect for a relaxed evening."
            occasion == OCCASION_CASUAL && mood == MOOD_CONFIDENT ->
                "Dark jeans, a fitted black t-shirt, and chelsea boots with a leather jacket. Classic and bold."
            occasion == OCCASION_CASUAL && mood == MOOD_LAZY ->
                "Your most comfortable jeans, a plain tee, and a zip-up hoodie. Comfortable but still put-together."
            occasion == OCCASION_CASUAL && mood == MOOD_SHARP ->
                "Chinos, a crisp Oxford shirt with rolled sleeves, and minimalist sneakers. Casual yet refined."

            // Work occasion
            occasion == OCCASION_WORK && mood == MOOD_CHILL ->
                "Navy chinos with a light blue button-down and brown loafers. Comfortable but office-appropriate."
            occasion == OCCASION_WORK && mood == MOOD_CONFIDENT ->
                "Your best-fitting suit, a spread collar shirt, bold tie, and polished oxfords. Command the room."
            occasion == OCCASION_WORK && mood == MOOD_LAZY ->
                "Gray trousers, a white shirt and a simple navy blazer. Minimal effort, maximum professionalism."
            occasion == OCCASION_WORK && mood == MOOD_SHARP ->
                "Tailored charcoal trousers, crisp white shirt, knit tie, and monk strap shoes. Creative yet professional."

            // Date occasion
            occasion == OCCASION_DATE && mood == MOOD_CHILL && (timeOfDay == MORNING || timeOfDay == AFTERNOON) ->
                "Light chinos with a casual button-up shirt and clean white sneakers. Relaxed but thoughtful."
            occasion == OCCASION_DATE && mood == MOOD_CHILL && (timeOfDay == EVENING || timeOfDay == NIGHT) ->
                "Dark jeans, a textured sweater, and Chelsea boots. Effortlessly stylish for evening plans."
            occasion == OCCASION_DATE && mood == MOOD_CONFIDENT ->
                "Slim black jeans, a fitted button-down rolled at the sleeves, and leather boots. Add a blazer if going somewhere upscale."
            occasion == OCCASION_DATE && mood == MOOD_LAZY ->
                "Straight-fit jeans, a solid color polo, and minimalist sneakers. Simple but clean."
            occasion == OCCASION_DATE && mood == MOOD_SHARP ->
                "Tailored slacks, a patterned shirt buttoned to the top, and suede loafers. Distinctive without trying too hard."

            // Gym occasion
            occasion == OCCASION_GYM && mood == MOOD_CHILL ->
                "Classic gray sweatpants, a breathable tee, and supportive running shoes."
            occasion == OCCASION_GYM && mood == MOOD_CONFIDENT ->
                "Fitted performance shorts, a sleeveless athletic shirt to show your progress, and your most supportive trainers."
            occasion == OCCASION_GYM && mood == MOOD_LAZY ->
                "Black joggers, a simple tee, and whatever athletic shoes are by the door. Function over form today."
            occasion == OCCASION_GYM && mood == MOOD_SHARP ->
                "Matching performance wear set in a dark color with reflective details and specialized shoes for your workout."

            // Default
            else -> "Dark jeans with a clean, simple t-shirt and your most comfortable sneakers."
        }
    }

    private fun getBeardTip(mood: String, occasion: String): String {
        return when {
            // Based on mood
            mood == MOOD_CHILL ->
                "Let your beard grow naturally today, just run some beard oil through it with your fingers."
            mood == MOOD_CONFIDENT ->
                "Define your beard lines with sharp edges for a more commanding look. Focus on a clean neckline."
            mood == MOOD_LAZY ->
                "A quick comb-through with a beard brush will make your beard look maintained with minimal effort."
            mood == MOOD_SHARP ->
                "Trim your beard precisely to maintain shape, focusing on symmetry and clean lines."

            // Based on occasion
            occasion == OCCASION_WORK ->
                "Keep your beard neat and trimmed to a professional length - about 1/2 inch is ideal for most workplaces."
            occasion == OCCASION_DATE ->
                "Use a beard balm to tame any flyaways and add a subtle masculine scent that will be noticed up close."

            // Default
            else -> "Keep your beard trimmed at the neckline (one finger above your Adam's apple) and use a beard oil to maintain softness."
        }
    }

    private fun getFragranceSuggestion(mood: String, occasion: String, timeOfDay: String): String {
        return when {
            // Morning/Afternoon
            (timeOfDay == MORNING || timeOfDay == AFTERNOON) && mood == MOOD_CHILL ->
                "Skip cologne and just use a scented deodorant - clean and understated."
            (timeOfDay == MORNING || timeOfDay == AFTERNOON) && mood == MOOD_CONFIDENT ->
                "A single spray of a citrus-based cologne like Acqua di Parma Colonia - bright, confident, but not overpowering."
            (timeOfDay == MORNING || timeOfDay == AFTERNOON) && mood == MOOD_LAZY ->
                "A refreshing body spray with mint notes - quick, easy, and awakening."
            (timeOfDay == MORNING || timeOfDay == AFTERNOON) && mood == MOOD_SHARP ->
                "A light application of a green tea or bergamot fragrance for a crisp, alert vibe."

            // Evening/Night
            (timeOfDay == EVENING || timeOfDay == NIGHT) && occasion == OCCASION_DATE ->
                "Two sprays of a warm, woody fragrance with subtle vanilla notes - inviting but not overwhelming."
            (timeOfDay == EVENING || timeOfDay == NIGHT) && mood == MOOD_CONFIDENT ->
                "A sophisticated oud or leather-based fragrance - commanding and memorable."
            (timeOfDay == EVENING || timeOfDay == NIGHT) ->
                "A warm spicy fragrance with amber notes - perfect for evening and nighttime."

            // Special occasions
            occasion == OCCASION_WORK ->
                "A light, clean scent with subtle citrus notes - professional and inoffensive in close quarters."
            occasion == OCCASION_GYM ->
                "Skip cologne entirely and focus on a good antiperspirant - clean is the best scent for working out."

            // Default
            else -> "A versatile cologne with notes of citrus and wood - one spray on your chest under your shirt."
        }
    }

    private fun getConfidenceQuote(mood: String): String {
        return when (mood) {
            MOOD_CHILL -> "\"Being comfortable with who you are is the ultimate style statement.\" - Unknown"
            MOOD_CONFIDENT -> "\"A man who stands confident in his style owns every room he walks into.\" - Tom Ford"
            MOOD_LAZY -> "\"Simplicity is the ultimate sophistication.\" - Leonardo da Vinci"
            MOOD_SHARP -> "\"Looking good isn't self-importance; it's self-respect.\" - Charles Hix"
            else -> "\"Style is a way to say who you are without having to speak.\" - Rachel Zoe"
        }
    }
}