package com.example.yumeat_25.ui.screens.recipes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.yumeat_25.data.Meal
import com.example.yumeat_25.data.MealRepository
import com.example.yumeat_25.data.FoodType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen(
    navController: NavController,
    mealRepository: MealRepository,
    onRecipeClick: (Meal) -> Unit = { },
    safeMode: Boolean = false // <--- AGGIUNTO parametro
) {
    val meals by mealRepository.meals.collectAsState()
    var selectedTab by remember { mutableStateOf(FoodType.ONNIVORE) }

    // Emoji for tabs
    val emojiOnnivore = "\uD83E\uDD69"
    val emojiVegetarian = "\uD83C\uDF31"
    val emojiVegan = "\uD83C\uDF31"

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
                }
                Text(
                    "Ricette consigliate",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .weight(1f),
                    textAlign = TextAlign.Start
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs by FoodType with emoji
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                FoodType.values().forEach { type ->
                    val isSelected = selectedTab == type
                    val labelWithEmoji = when (type) {
                        FoodType.ONNIVORE -> type.displayName + " $emojiOnnivore"
                        FoodType.VEGETARIAN -> type.displayName + " $emojiVegetarian"
                        FoodType.VEGAN -> type.displayName + " $emojiVegan"
                    }
                    TextButton(
                        onClick = { selectedTab = type },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = labelWithEmoji,
                            color = if (isSelected) Color.Black else Color.Gray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 15.sp
                        )
                    }
                }
            }
            Divider(modifier = Modifier.padding(bottom = 8.dp))

            // Recipes list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 6.dp)
            ) {
                items(meals.filter { it.type == selectedTab }) { meal ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        onClick = { onRecipeClick(meal) }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    meal.name,
                                    fontSize = 17.sp,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (!safeMode) {
                                    Text(
                                        "${meal.calories} kcal • C: ${meal.carbs}g P: ${meal.protein}g F: ${meal.fat}g",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                            Text(
                                meal.emoji ?: "",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}