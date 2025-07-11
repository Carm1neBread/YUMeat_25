package com.example.yumeat_25.ui.screens.dashboard.addMeal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yumeat_25.data.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealSafeMode(
    userProfileRepository: UserProfileRepository,
    foodRepository: FoodRepository,
    onBack: () -> Unit,
    onMealAdded: () -> Unit
) {
    var selectedMealTime by remember { mutableStateOf("breakfast") }
    var searchQuery by remember { mutableStateOf("") }

    val foods by foodRepository.foods.collectAsState()

    val filteredFoods = remember(searchQuery, foods) {
        if (searchQuery.isEmpty()) foods else foodRepository.searchFoods(searchQuery)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aggiungi alimento (Safe Mode)") },
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
                text = "A quale pasto vuoi aggiungere?",
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
                    FoodItemSafeMode(
                        food = food,
                        onQuickAdd = {
                            userProfileRepository.addFoodToMeal(food, selectedMealTime)
                            onMealAdded()
                        },
                        canQuickAdd = selectedMealTime.isNotBlank()
                    )
                }
            }
        }
    }
}

@Composable
fun FoodItemSafeMode(
    food: Food,
    onQuickAdd: () -> Unit,
    canQuickAdd: Boolean
) {
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
                // In safe mode NON mostro info nutrizionali, solo il tipo se vuoi
                Text(
                    text = food.type.displayName,
                    fontSize = 14.sp,
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
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}