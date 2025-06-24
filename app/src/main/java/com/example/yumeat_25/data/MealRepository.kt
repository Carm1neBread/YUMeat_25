package com.example.yumeat_25.data

import com.example.yumeat_25.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MealRepository {
    private val initialMeals = listOf(
        Meal(
            id = "1",
            name = "Toast tris",
            type = MealType.LUNCH,
            imageRes = R.drawable.toast_tris,
            emoji = "\uD83E\uDD69",
            ingredientTitles = listOf("Toast 1", "Toast 2", "Toast 3"),
            ingredientRows = listOf(
                listOf("Philadelphia", "Philadelphia", "Pesto"),
                listOf("Cipolla rossa", "Salmone", "Pomodori"),
                listOf("Avocado", "Avocado", "Mozzarella"),
                listOf("Uova", "Semi", "Rucola")
            )
        ),
        Meal(
            id = "2",
            name = "Pasta primavera",
            type = MealType.LUNCH,
            emoji = "\uD83C\uDF31"
        ),
        Meal(
            id = "3",
            name = "Poke",
            type = MealType.LUNCH,
            emoji = "\uD83E\uDD69"
        ),
        Meal(
            id = "4",
            name = "Cesar salad",
            type = MealType.LUNCH,
            emoji = "\uD83C\uDF31"
        ),
        Meal(
            id = "5",
            name = "Hamburger",
            type = MealType.LUNCH,
            emoji = "\uD83E\uDD69"
        ),
        Meal(
            id = "6",
            name = "Macedonia di frutta",
            type = MealType.SNACK,
            emoji = "\uD83C\uDF31"
        )
    )

    private val _meals = MutableStateFlow<List<Meal>>(initialMeals)
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

    fun getMealByName(mealName: String): Meal? {
        return _meals.value.find { it.name == mealName }
    }
}