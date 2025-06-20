package com.example.yumeat_25.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yumeat_25.data.DietaryPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingDietaryPreferencesScreen(
    onNext: (DietaryPreferences) -> Unit,
    onBack: () -> Unit
) {
    var vegetarian by remember { mutableStateOf(false) }
    var vegan by remember { mutableStateOf(false) }
    var glutenFree by remember { mutableStateOf(false) }
    var dairyFree by remember { mutableStateOf(false) }
    var allergies by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Preferenze alimentari",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LinearProgressIndicator(
            progress = 0.66f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        Text(
            text = "Seleziona le tue preferenze alimentari",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Preferences
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = vegetarian,
                        onCheckedChange = { vegetarian = it }
                    )
                    Text("Vegetariano", modifier = Modifier.padding(start = 8.dp))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = vegan,
                        onCheckedChange = { vegan = it }
                    )
                    Text("Vegano", modifier = Modifier.padding(start = 8.dp))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = glutenFree,
                        onCheckedChange = { glutenFree = it }
                    )
                    Text("Senza glutine", modifier = Modifier.padding(start = 8.dp))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = dairyFree,
                        onCheckedChange = { dairyFree = it }
                    )
                    Text("Senza lattosio", modifier = Modifier.padding(start = 8.dp))
                }
            }

            item {
                OutlinedTextField(
                    value = allergies,
                    onValueChange = { allergies = it },
                    label = { Text("Allergie (opzionale)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Button(
            onClick = {
                onNext(
                    DietaryPreferences(
                        vegetarian = vegetarian,
                        vegan = vegan,
                        glutenFree = glutenFree,
                        dairyFree = dairyFree,
                        allergies = allergies.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Continua", fontSize = 16.sp)
        }
    }
}