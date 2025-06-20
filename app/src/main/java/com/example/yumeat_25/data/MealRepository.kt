package com.example.yumeat_25.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MealRepository {
    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals.asStateFlow()

    fun addMeal(meal: Meal) {
        _meals.value = _meals.value + meal
    }

    fun getMealsByType(type: MealType): List<Meal> {
        return _meals.value.filter { it.type == type }
    }

    fun getTodaysMeals(): List<Meal> {
        val today = System.currentTimeMillis()
        val startOfDay = today - (today % (24 * 60 * 60 * 1000))
        return _meals.value.filter { it.timestamp >= startOfDay }
    }
}