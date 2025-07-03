package com.example.yumeat_25.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserProfileRepository {
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun updatePersonalData(personalData: PersonalData) {
        _userProfile.value = _userProfile.value.copy(personalData = personalData)
    }

    fun updateDietaryPreferences(preferences: DietaryPreferences) {
        _userProfile.value = _userProfile.value.copy(dietaryPreferences = preferences)
    }

    fun updateGoals(goals: UserGoals) {
        _userProfile.value = _userProfile.value.copy(goals = goals)
    }

    fun completeOnboarding() {
        _userProfile.value = _userProfile.value.copy(isOnboardingComplete = true)
    }

    fun addFoodToMeal(food: Food, meal: String) {
        _userProfile.value = when (meal) {
            "breakfast" -> _userProfile.value.copy(breakfast = _userProfile.value.breakfast + food)
            "lunch" -> _userProfile.value.copy(lunch = _userProfile.value.lunch + food)
            "dinner" -> _userProfile.value.copy(dinner = _userProfile.value.dinner + food)
            else -> _userProfile.value
        }
    }

    fun addMealToMealTime(meal: Meal, mealTime: String) {
        // Convert Meal to Food
        val food = Food(
            id = meal.id,
            name = meal.name,
            type = meal.type,
            calories = meal.calories,
            carbs = meal.carbs,
            protein = meal.protein,
            fat = meal.fat
        )
        addFoodToMeal(food, mealTime)
    }

    /**
     * Rimuove SOLO la prima occorrenza di food dal pasto selezionato (in base all'id e NON solo al nome).
     */
    fun removeFoodFromMeal(mealName: String, food: Food) {
        _userProfile.value = when (mealName) {
            "Colazione", "breakfast" -> _userProfile.value.copy(
                breakfast = _userProfile.value.breakfast.removeFirstOccurrence(food)
            )
            "Pranzo", "lunch" -> _userProfile.value.copy(
                lunch = _userProfile.value.lunch.removeFirstOccurrence(food)
            )
            "Cena", "dinner" -> _userProfile.value.copy(
                dinner = _userProfile.value.dinner.removeFirstOccurrence(food)
            )
            else -> _userProfile.value
        }
    }

    /**
     * Estensione per rimuovere la PRIMA occorrenza di un Food da una lista, confrontando l'id.
     */
    private fun List<Food>.removeFirstOccurrence(food: Food): List<Food> {
        val idx = indexOfFirst { it.id == food.id }
        return if (idx == -1) this else toMutableList().apply { removeAt(idx) }
    }
}