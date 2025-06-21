package com.example.yumeat_25.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yumeat_25.data.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController
) {
    val mealRepository = remember { MealRepository() }
    val meals by mealRepository.meals.collectAsState()

    // Replace these with your real data
    val kcalGoal = 1200
    val kcalRemaining = 572
    val carbs = 128
    val carbsGoal = 262
    val protein = 78
    val proteinGoal = 121
    val fat = 54
    val fatGoal = 60

    val todaysMeals = listOf(
        Triple("Latte", "250 ml", "98 kcal"),
        Triple("Pasta", "86 g", "237 kcal"),
        Triple("Petto di pollo", "110 g", "130 kcal"),
    )

    // Date
    val now = LocalDate.now()
    val dayOfWeek = now.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val month = now.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val day = now.dayOfMonth

    // Used for Safe mode (eye) - implement your logic accordingly
    val isSafeMode = false

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Top bar with menu, date, safe mode
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /* Drawer */ }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$dayOfWeek $month",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text = day.toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                IconButton(onClick = { /* Safe mode toggle */ }) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Safe mode",
                        tint = Color.Black
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Card: Riepilogo di oggi + Circular Progress
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Riepilogo di oggi",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 4.dp)
                            .size(120.dp)
                    ) {
                        // Simulated circular progress (replace with your own if needed)
                        CircularProgressIndicator(
                            progress = kcalRemaining / kcalGoal.toFloat(),
                            color = Color.Black,
                            strokeWidth = 7.dp,
                            modifier = Modifier.size(110.dp)
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$kcalRemaining",
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "kcal",
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Text(
                        text = "Rimanenti",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(8.dp))
                    // Macros: Carbs / Protein / Fat
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MacroSummary("Carboidrati", carbs, carbsGoal)
                        MacroSummary("Proteine", protein, proteinGoal)
                        MacroSummary("Grassi", fat, fatGoal)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // List of meals
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 8.dp)
                ) {
                    Text(
                        text = "Cosa hai mangiato",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 8.dp, bottom = 6.dp)
                    )
                    // Table header (optional, not in image)
                    todaysMeals.forEach { (name, qty, kcal) ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = name,
                                    fontSize = 15.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = qty,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = kcal,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }
                    }
                    // Dettagli link
                    Text(
                        text = "Dettagli",
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 12.dp)
                            .clickable { navController.navigate("add_meal") }
                    )
                }
            }
        }

        // Floating Add button (bottom left)
        FloatingActionButton(
            onClick = { navController.navigate("add_meal") },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 18.dp),
            containerColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Aggiungi pasto", tint = Color.Black)
        }

        // Floating QR/other button (bottom right, use a placeholder icon)
        FloatingActionButton(
            onClick = { /* TODO: Add QR or navigation logic */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 18.dp),
            containerColor = Color.White
        ) {
            Icon(Icons.Default.Person, contentDescription = "QR", tint = Color.Black)
        }

        // Bottom indicator bar
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp)
                .width(42.dp)
                .height(7.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFDDDDDD))
        )
    }
}

@Composable
fun MacroSummary(label: String, value: Int, goal: Int) {
    Row {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f) // <-- No parentheses after weight!
        ) {
            Text(
                label,
                fontSize = 13.sp,
                color = Color.Gray
            )
            Text(
                "$value / $goal g",
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = Color.Black
            )
        }
    }
}