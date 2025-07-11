package com.example.yumeat_25.data

import androidx.compose.runtime.Immutable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Immutable
data class PersonalData(
    val name: String = "",
    val age: String = "",
    val height: String = "",
    val weight: String = ""
)

@Immutable
data class DietaryPreferences(
    //Non inseriamo l'attributo di onnivoro, se vegetarian e vegan sono a false l'utente viene considerato automaticamente onnivoro
    val vegetarian: Boolean = false,
    val vegan: Boolean = false,
    val glutenFree: Boolean = false,
    val dairyFree: Boolean = false,
    val avoidRedMeat: Boolean = false,
    val avoidSugar: Boolean = false
)

@Immutable
data class UserGoals(
    val primaryGoal: String = "",
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
data class DiaryEntry(
    val id: String = System.currentTimeMillis().toString(),
    val date: LocalDate = LocalDate.now(),
    val emoji: MoodEmoji = MoodEmoji.NEUTRAL,
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    // Formatta la data come "GG Mese YYYY" (es. "10 Luglio 2025")
    fun getFormattedDate(): String {
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
        return date.format(formatter)
    }
}

enum class MoodEmoji(val unicode: String, val description: String) {
    VERY_HAPPY("\uD83D\uDE00", "Molto felice"),
    HAPPY("\uD83D\uDE42", "Felice"),
    NEUTRAL("\uD83D\uDE10", "Neutrale"),
    SAD("\uD83D\uDE41", "Triste")
}


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

    fun getCurrentCalories(): Int =
        breakfast.sumOf { it.calories } + lunch.sumOf { it.calories } + dinner.sumOf { it.calories }

    fun getCurrentCarbs(): Int =
        breakfast.sumOf { it.carbs } + lunch.sumOf { it.carbs } + dinner.sumOf { it.carbs }

    fun getCurrentProtein(): Int =
        breakfast.sumOf { it.protein } + lunch.sumOf { it.protein } + dinner.sumOf { it.protein }

    fun getCurrentFat(): Int =
        breakfast.sumOf { it.fat } + lunch.sumOf { it.fat } + dinner.sumOf { it.fat }
}