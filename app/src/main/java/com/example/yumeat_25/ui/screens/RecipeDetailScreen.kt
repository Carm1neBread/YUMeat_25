package com.example.yumeat_25.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yumeat_25.R
import com.example.yumeat_25.data.Meal
import com.example.yumeat_25.data.MealRepository

@Composable
fun RecipeDetailScreen(
    navController: NavController,
    mealName: String,
    mealRepository: MealRepository
) {
    val meals by mealRepository.meals.collectAsState()
    val meal = meals.find { it.name == mealName }

    val recipeImage = meal?.imageRes ?: R.drawable.placeholder
    val hasIngredients = meal?.ingredientTitles != null && meal.ingredientRows != null

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
                    textAlign = androidx.compose.ui.text.style.TextAlign.Start
                )
                Spacer(Modifier.width(48.dp))
            }
        }
    ) { paddingValues ->
        if (meal == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Ricetta non trovata", color = Color.Gray, fontSize = 18.sp)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                // Card-like background
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFFF3F3F3))
                        .padding(20.dp)
                ) {
                    // Title
                    Text(
                        text = meal.name + " " + (meal.emoji ?: ""),
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    // Image
                    Image(
                        painter = painterResource(id = recipeImage),
                        contentDescription = meal.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(18.dp))
                    // Ingredienti
                    Text(
                        text = "Ingredienti",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (hasIngredients) {
                        // Table-style ingredients
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            meal.ingredientTitles?.forEach {
                                Text(
                                    text = it,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 15.sp,
                                    modifier = Modifier.weight(1f),
                                    color = Color(0xFF333333),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                        meal.ingredientRows?.forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                row.forEach {
                                    Text(
                                        text = it,
                                        fontSize = 14.sp,
                                        modifier = Modifier.weight(1f),
                                        color = Color(0xFF555555),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Ingredienti non disponibili",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 24.dp)
                        )
                    }

                    Spacer(Modifier.height(22.dp))
                    Button(
                        onClick = { /* TODO: Handle add to meal plan */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF295B4F),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Aggiungi", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}