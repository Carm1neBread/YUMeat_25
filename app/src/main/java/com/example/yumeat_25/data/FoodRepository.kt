package com.example.yumeat_25.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FoodRepository {
    private val _foods = MutableStateFlow(getFakeFoods())
    val foods: StateFlow<List<Food>> = _foods.asStateFlow()

    fun searchFoods(query: String): List<Food> {
        return _foods.value.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

    private fun getFakeFoods(): List<Food> {
        //Inizializzaizone di cibi di default
        return listOf(
            Food("1", "Mela", FoodType.VEGAN, 52, 14, 0, 0),
            Food("2", "Banana", FoodType.VEGAN, 89, 23, 1, 0),
            Food("3", "Pane integrale", FoodType.VEGAN, 250, 45, 8, 2),
            Food("4", "Yogurt greco", FoodType.VEGETARIAN, 59, 3, 10, 0),
            Food("5", "Pollo", FoodType.ONNIVORE, 165, 0, 31, 3),
            Food("6", "Salmone", FoodType.ONNIVORE, 208, 0, 20, 13),
            Food("7", "Broccoli", FoodType.VEGAN, 34, 7, 2, 0),
            Food("8", "Riso", FoodType.VEGAN, 130, 28, 2, 0),
            Food("9", "Avocado", FoodType.VEGAN, 160, 9, 2, 15),
            Food("10", "Mandorle", FoodType.VEGAN, 579, 22, 21, 50)
        )
    }
}