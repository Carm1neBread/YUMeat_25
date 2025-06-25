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
    val allergies: List<String> = emptyList(),
    val avoidRedMeat: Boolean = false,
    val avoidSugar: Boolean = false
)

@Immutable
data class UserGoals(
    val primaryGoal: String = "",
    val secondaryGoals: List<String> = emptyList(),
    val safeMode: Boolean = false
)

enum class FoodType(val displayName: String) {
    ONNIVORE("Onnivoro"),
    VEGETARIAN("Vegetariano"),
    VEGAN("Vegano")
}

@Immutable
data class Food(
    val id: String,
    val name: String,
    val type: FoodType,
    val calories: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int
)

@Immutable
data class Meal(
    val id: String,
    val name: String,
    val type: FoodType,
    val calories: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val imageRes: Int? = null,
    val emoji: String? = null,
    val ingredientTitles: List<String>? = null,
    val ingredientRows: List<List<String>>? = null,
    val notes: String = ""
)

@Immutable
data class UserProfile(
    val personalData: PersonalData = PersonalData(),
    val dietaryPreferences: DietaryPreferences = DietaryPreferences(),
    val goals: UserGoals = UserGoals(),
    val isOnboardingComplete: Boolean = false,
    val caloriesGoal: Int = 2000,
    val carbsGoal: Int = 250,
    val proteinGoal: Int = 120,
    val fatGoal: Int = 60,
    val breakfast: List<Food> = emptyList(),
    val lunch: List<Food> = emptyList(),
    val dinner: List<Food> = emptyList()
) {
    fun getCompletionStatus(): Map<String, Boolean> {
        return mapOf(
            "personal_data" to personalData.name.isNotEmpty(),
            "dietary_preferences" to true, // Always considered complete
            "goals" to goals.primaryGoal.isNotEmpty()
        )
    }

    fun getCurrentCalories(): Int =
        breakfast.sumOf { it.calories } + lunch.sumOf { it.calories } + dinner.sumOf { it.calories }

    fun getCurrentCarbs(): Int =
        breakfast.sumOf { it.carbs } + lunch.sumOf { it.carbs } + dinner.sumOf { it.carbs }

    fun getCurrentProtein(): Int =
        breakfast.sumOf { it.protein } + lunch.sumOf { it.protein } + dinner.sumOf { it.protein }

    fun getCurrentFat(): Int =
        breakfast.sumOf { it.fat } + lunch.sumOf { it.fat } + dinner.sumOf { it.fat }
}