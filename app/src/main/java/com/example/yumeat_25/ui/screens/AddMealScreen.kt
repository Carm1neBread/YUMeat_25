package com.example.yumeat_25.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yumeat_25.data.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreen(
    userProfileRepository: UserProfileRepository,
    foodRepository: FoodRepository,
    onBack: () -> Unit,
    onMealAdded: () -> Unit
) {
    var selectedMealTime by remember { mutableStateOf("breakfast") }
    var searchQuery by remember { mutableStateOf("") }
    var showAddFoodDialog by remember { mutableStateOf<Food?>(null) }

    val foods by foodRepository.foods.collectAsState()

    val filteredFoods = remember(searchQuery, foods) {
        if (searchQuery.isEmpty()) foods else foodRepository.searchFoods(searchQuery)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aggiungi alimento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Selezione pasto: colazione, pranzo, cena
            Text(
                text = "A quale pasto vuoi aggiungere? (valori per 100g)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("breakfast" to "Colazione", "lunch" to "Pranzo", "dinner" to "Cena").forEach { (key, label) ->
                    FilterChip(
                        onClick = { selectedMealTime = key },
                        label = { Text(label) },
                        selected = selectedMealTime == key,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Cerca alimento") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredFoods) { food ->
                    FoodItem(
                        food = food,
                        onQuickAdd = {
                            userProfileRepository.addFoodToMeal(food, selectedMealTime)
                            onMealAdded()
                        },
                        onDetailAdd = { showAddFoodDialog = food },
                        canQuickAdd = selectedMealTime.isNotBlank()
                    )
                }
            }
        }
    }

    // Dialog per aggiunta dettagliata
    if (showAddFoodDialog != null) {
        AddFoodDetailDialog(
            defaultFood = showAddFoodDialog!!,
            onDismiss = { showAddFoodDialog = null },
            onConfirm = { food ->
                userProfileRepository.addFoodToMeal(food, selectedMealTime)
                onMealAdded()
            }
        )
    }
}

@Composable
fun FoodItem(
    food: Food,
    onQuickAdd: () -> Unit,
    onDetailAdd: () -> Unit,
    canQuickAdd: Boolean
) {
    val customColor = Color(0xFF1F5F5B)
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = food.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = food.type.displayName,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${food.calories} kcal • C: ${food.carbs}g P: ${food.protein}g F: ${food.fat}g",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (canQuickAdd) {
                IconButton(
                    onClick = onQuickAdd,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Aggiungi rapidamente",
                        tint = Color(0xFF0694F4)
                    )
                }
            }

            TextButton(
                onClick = onDetailAdd,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = customColor
                )
            ) {
                Text("Dettagli")
            }
        }
    }
}