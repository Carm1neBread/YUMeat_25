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

    //Metodo per aggiungere cibi al pasto (colazione, pranzo, cena)
    fun addFoodToMeal(food: Food, meal: String) {
        _userProfile.value = when (meal) {
            "breakfast" -> _userProfile.value.copy(breakfast = _userProfile.value.breakfast + food)
            "lunch" -> _userProfile.value.copy(lunch = _userProfile.value.lunch + food)
            "dinner" -> _userProfile.value.copy(dinner = _userProfile.value.dinner + food)
            else -> _userProfile.value
        }
    }

    //Metodo per aggiungere una ricetta al pasto (colazione, pranzo, cena)
    fun addMealToMealTime(meal: Meal, mealTime: String) {
        // Converitamo la ricetta in un cibo per utilizzare il metodo precedente
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

    //Metodo per rimuovere un cibo dalla lista di un pastop
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

    //Metodo che permette di eliminare la prima occorrenza del cibo in base all'id
    private fun List<Food>.removeFirstOccurrence(food: Food): List<Food> {
        val idx = indexOfFirst { it.id == food.id }
        return if (idx == -1) this else toMutableList().apply { removeAt(idx) }
    }
}