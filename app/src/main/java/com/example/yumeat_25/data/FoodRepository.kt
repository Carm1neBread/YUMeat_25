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
        return listOf(
            Food("1", "Mela", "Frutta", true),
            Food("2", "Banana", "Frutta", true),
            Food("3", "Pane integrale", "Cereali", true),
            Food("4", "Yogurt greco", "Latticini", true),
            Food("5", "Pollo", "Proteine", true),
            Food("6", "Salmone", "Pesce", true),
            Food("7", "Broccoli", "Verdure", true),
            Food("8", "Riso", "Cereali", true),
            Food("9", "Avocado", "Grassi sani", true),
            Food("10", "Mandorle", "Frutta secca", true)
        )
    }
}