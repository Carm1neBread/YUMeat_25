package com.example.yumeat_25.data

import com.example.yumeat_25.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MealRepository {
    //Inizializzazione di ricette di default
    private val initialMeals = listOf(
        Meal(
            id = "1",
            name = "Toast tris",
            type = FoodType.ONNIVORE,
            calories = 420,
            carbs = 45,
            protein = 24,
            fat = 18,
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
            type = FoodType.VEGETARIAN,
            calories = 350,
            carbs = 60,
            protein = 12,
            fat = 8,
            emoji = "\uD83C\uDF31"
        ),
        Meal(
            id = "3",
            name = "Poke",
            type = FoodType.ONNIVORE,
            calories = 480,
            carbs = 55,
            protein = 22,
            fat = 17,
            emoji = "\uD83E\uDD69"
        ),
        Meal(
            id = "4",
            name = "Cesar salad",
            type = FoodType.VEGETARIAN,
            calories = 310,
            carbs = 12,
            protein = 12,
            fat = 20,
            emoji = "\uD83C\uDF31"
        ),
        Meal(
            id = "5",
            name = "Hamburger",
            type = FoodType.ONNIVORE,
            calories = 550,
            carbs = 40,
            protein = 29,
            fat = 32,
            emoji = "\uD83E\uDD69"
        ),
        Meal(
            id = "6",
            name = "Macedonia di frutta",
            type = FoodType.VEGAN,
            calories = 120,
            carbs = 28,
            protein = 2,
            fat = 0,
            emoji = "\uD83C\uDF31"
        )
    )

    private val _meals = MutableStateFlow<List<Meal>>(initialMeals)
    val meals: StateFlow<List<Meal>> = _meals.asStateFlow()
}