package com.example.yumeat_25.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    // Alimentazione
    var alimentation by remember { mutableStateOf<String?>(null) } // <-- Not selected initially
    val alimentationOptions = listOf("Onnivora", "Vegetariana", "Vegana")

    // Alimenti da evitare
    var avoidCarneRossa by remember { mutableStateOf(false) }
    var avoidLatticini by remember { mutableStateOf(false) }
    var avoidGlutine by remember { mutableStateOf(false) }
    var avoidZucchero by remember { mutableStateOf(false) }

    val isButtonEnabled = alimentation != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Header row with back
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
            }
        }

        // Progress bar at top
        LinearProgressIndicator(
            progress = 0.66f,
            color = Color(0xFF1F5F5B),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 32.dp)
        )

        // Title
        Text(
            text = "Personalizza la tua\nesperienza alimentare!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            lineHeight = 28.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        // Subtitle
        Text(
            text = "Attiva solo ciò che ti fa sentire a tuo agio.\nPuoi cambiare tutto in qualsiasi momento.",
            fontSize = 15.sp,
            color = Color(0xFF222222),
            modifier = Modifier
                .padding(top = 12.dp, bottom = 32.dp)
                .fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Tipo di alimentazione -- CARD with elevation
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = "Tipo di alimentazione",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 4.dp, bottom = 6.dp)
                )
                alimentationOptions.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.Transparent)
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option,
                            fontSize = 17.sp,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)
                        )
                        RadioButton(
                            selected = alimentation == option,
                            onClick = { alimentation = option },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF000000),
                                unselectedColor = Color(0xFF000000),
                                disabledSelectedColor = Color.Gray,
                                disabledUnselectedColor = Color.LightGray
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Alimenti da evitare -- CARD with elevation
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = "Alimenti da evitare",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 4.dp, bottom = 6.dp)
                )
                AvoidItem(
                    label = "Carne rossa",
                    checked = avoidCarneRossa,
                    onCheckedChange = { avoidCarneRossa = it }
                )
                AvoidItem(
                    label = "Latticini",
                    checked = avoidLatticini,
                    onCheckedChange = { avoidLatticini = it }
                )
                AvoidItem(
                    label = "Glutine",
                    checked = avoidGlutine,
                    onCheckedChange = { avoidGlutine = it }
                )
                AvoidItem(
                    label = "Zucchero",
                    checked = avoidZucchero,
                    onCheckedChange = { avoidZucchero = it }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (alimentation != null) {
                    onNext(
                        DietaryPreferences(
                            vegetarian = alimentation == "Vegetariana",
                            vegan = alimentation == "Vegana",
                            glutenFree = avoidGlutine,
                            dairyFree = avoidLatticini,
                            allergies = listOf(),
                            avoidRedMeat = avoidCarneRossa,
                            avoidSugar = avoidZucchero
                        )
                    )
                }
            },
            enabled = isButtonEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isButtonEnabled) Color(0xFF1F5F5B) else Color(0xFFD1D1D1),
                contentColor = Color(0xFFFFFFFF)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Continua", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}

@Composable
private fun AvoidItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Transparent)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 17.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        )
        RadioButton(
            selected = checked,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF000000),
                unselectedColor = Color(0xFF000000),
                disabledSelectedColor = Color.Gray,
                disabledUnselectedColor = Color.LightGray
            ),
            onClick = { onCheckedChange(!checked) }
        )
    }
}