package com.example.yumeat_25.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import com.example.yumeat_25.data.UserProfileRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    navController: NavController,
    mealName: String,
    mealRepository: MealRepository,
    userProfileRepository: UserProfileRepository
) {
    val meals by mealRepository.meals.collectAsState()
    val meal = meals.find { it.name == mealName }
    val recipeImage = meal?.imageRes ?: R.drawable.placeholder
    val hasIngredients = meal?.ingredientTitles != null && meal.ingredientRows != null

    // State for showing dialog
    var showAddDialog by remember { mutableStateOf(false) }

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
                // Card with elevation effect for the recipe
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
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
                        // Nutritional info
                        Text(
                            text = "Valori nutrizionali",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Calorie: ${meal.calories} kcal", fontSize = 14.sp, color = Color(0xFF555555), fontWeight = FontWeight.Bold)
                            Text("Carboidrati: ${meal.carbs}g", fontSize = 14.sp, color = Color(0xFF555555), fontWeight = FontWeight.Bold)
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Proteine: ${meal.protein}g", fontSize = 14.sp, color = Color(0xFF555555), fontWeight = FontWeight.Bold)
                            Text("Grassi: ${meal.fat}g", fontSize = 14.sp, color = Color(0xFF555555), fontWeight = FontWeight.Bold)
                        }
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
                            onClick = { showAddDialog = true },
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

    // Dialog to choose meal time and immediately go to main after adding
    if (showAddDialog && meal != null) {
        AddMealToTimeDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { mealTime ->
                userProfileRepository.addMealToMealTime(meal, mealTime)
                showAddDialog = false
                // Navigate to MainScreen to show updated macros/calories
                navController.popBackStack("main", inclusive = false)
            }
        )
    }
}

@Composable
fun AddMealToTimeDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var selectedMealTime by remember { mutableStateOf("breakfast") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Aggiungi a:") },
        text = {
            Column {
                MealTimeRadioButton("Colazione", "breakfast", selectedMealTime) { selectedMealTime = it }
                MealTimeRadioButton("Pranzo", "lunch", selectedMealTime) { selectedMealTime = it }
                MealTimeRadioButton("Cena", "dinner", selectedMealTime) { selectedMealTime = it }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedMealTime) }) {
                Text("Aggiungi")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla")
            }
        }
    )
}

@Composable
fun MealTimeRadioButton(label: String, value: String, groupValue: String, onSelected: (String) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onSelected(value) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = groupValue == value,
            onClick = { onSelected(value) }
        )
        Text(label, fontSize = 15.sp)
    }
}