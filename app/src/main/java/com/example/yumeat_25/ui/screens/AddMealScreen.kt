package com.example.yumeat_25.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yumeat_25.data.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreen(
    onBack: () -> Unit,
    onMealAdded: () -> Unit
) {
    var selectedMealType by remember { mutableStateOf<MealType?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showAddFoodDialog by remember { mutableStateOf(false) }

    val foodRepository = remember { FoodRepository() }
    val mealRepository = remember { MealRepository() }
    val foods by foodRepository.foods.collectAsState()

    val filteredFoods = remember(searchQuery, foods) {
        if (searchQuery.isEmpty()) foods else foodRepository.searchFoods(searchQuery)
    }

    // Intervento 2: Campo "tipo di pasto" obbligatorio
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aggiungi pasto") },
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
            // Selezione tipo pasto (Intervento 2)
            Text(
                text = "Tipo di pasto *",
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
                MealType.values().forEach { mealType ->
                    FilterChip(
                        onClick = { selectedMealType = mealType },
                        label = { Text(mealType.displayName) },
                        selected = selectedMealType == mealType,
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

            // Food list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredFoods) { food ->
                    FoodItem(
                        food = food,
                        onQuickAdd = {
                            // Intervento 3: Aggiunta rapida
                            selectedMealType?.let { mealType ->
                                val meal = Meal(
                                    id = UUID.randomUUID().toString(),
                                    name = food.name,
                                    type = mealType
                                )
                                mealRepository.addMeal(meal)
                                onMealAdded()
                            }
                        },
                        onDetailAdd = { showAddFoodDialog = true },
                        canQuickAdd = selectedMealType != null
                    )
                }
            }
        }
    }

    // Dialog per aggiunta dettagliata
    if (showAddFoodDialog) {
        AddFoodDetailDialog(
            onDismiss = { showAddFoodDialog = false },
            onConfirm = { foodName, notes ->
                selectedMealType?.let { mealType ->
                    val meal = Meal(
                        id = UUID.randomUUID().toString(),
                        name = foodName,
                        type = mealType,
                        notes = notes
                    )
                    mealRepository.addMeal(meal)
                    onMealAdded()
                }
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
                    text = food.category,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Intervento 3: Pulsante aggiunta rapida
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

            TextButton(onClick = onDetailAdd) {
                Text("Dettagli")
            }
        }
    }
}

@Composable
fun AddFoodDetailDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var foodName by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Aggiungi alimento") },
        text = {
            Column {
                OutlinedTextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = { Text("Nome alimento") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Note (opzionale)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(foodName, notes) },
                enabled = foodName.isNotBlank()
            ) {
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