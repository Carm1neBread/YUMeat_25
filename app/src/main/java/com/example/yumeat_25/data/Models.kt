package com.example.yumeat_25.data

import androidx.compose.runtime.Immutable

@Immutable
data class PersonalData(
    val name: String = "",
    val age: String = "",
    val height: String = "",
    val weight: String = ""
)

@Immutable
data class DietaryPreferences(
    val vegetarian: Boolean = false,
    val vegan: Boolean = false,
    val glutenFree: Boolean = false,
    val dairyFree: Boolean = false,
    val allergies: List<String> = emptyList()
)

@Immutable
data class UserGoals(
    val primaryGoal: String = "",
    val secondaryGoals: List<String> = emptyList(),
    val safeMode: Boolean = false
)

@Immutable
data class UserProfile(
    val personalData: PersonalData = PersonalData(),
    val dietaryPreferences: DietaryPreferences = DietaryPreferences(),
    val goals: UserGoals = UserGoals(),
    val isOnboardingComplete: Boolean = false
) {
    fun getCompletionStatus(): Map<String, Boolean> {
        return mapOf(
            "personal_data" to personalData.name.isNotEmpty(),
            "dietary_preferences" to true, // Always considered complete
            "goals" to goals.primaryGoal.isNotEmpty()
        )
    }
}

enum class MealType(val displayName: String) {
    BREAKFAST("Colazione"),
    LUNCH("Pranzo"),
    DINNER("Cena"),
    SNACK("Spuntino")
}

@Immutable
data class Meal(
    val id: String,
    val name: String,
    val type: MealType,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUrl: String? = null,
    val notes: String = ""
)

@Immutable
data class Food(
    val id: String,
    val name: String,
    val category: String,
    val isHealthy: Boolean = true
)