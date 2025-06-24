package com.example.yumeat_25.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yumeat_25.data.*
import com.example.yumeat_25.ui.screens.MainScreen.DrawerMenuContent
import com.example.yumeat_25.ui.screens.MainScreen.SemiCircularProgressBar
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var drawerOpenCount by remember { mutableStateOf(0) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenuContent(
                key = drawerOpenCount,
                onItemClick = { label ->
                    when (label) {
                        "Dati personali" -> navController.navigate("profile");
                        "Preferenze alimentari" -> navController.navigate("preferences");
                        "Obiettivi" -> navController.navigate("goals");
                        "Ricette consigliate" -> navController.navigate("recipes")
                        // add more cases for other labels if needed
                    }
                    scope.launch { drawerState.close() }
                },
                onClose = { scope.launch { drawerState.close() } }
            )
        }
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
            Triple("Mandorle", "20 g", "110 kcal"),
            Triple("Olio Evo", "20 g", "160 kcal"),
        )

        // Date
        val now = LocalDate.now()
        val dayOfWeek = now.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val month = now.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val day = now.dayOfMonth

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 0.dp)
            ) {
                Spacer(Modifier.height(60.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(60.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        scope.launch {
                            drawerOpenCount++
                            drawerState.open()
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$dayOfWeek $month",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = day.toString(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    IconButton(onClick = { /* Safe mode toggle */ }) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Safe mode",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Card: Riepilogo di oggi + SemiCircularProgressBar
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
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
                            fontSize = 22.sp,
                            color = Color.Black
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 14.dp)
                                .size(150.dp)
                        ) {
                            SemiCircularProgressBar(
                                progress = kcalRemaining / kcalGoal.toFloat(),
                                modifier = Modifier.size(140.dp),
                                strokeWidth = 18f,
                                backgroundColor = Color(0x22000000),
                                progressColor = Color.Black
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$kcalRemaining",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 30.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = "kcal",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Rimanenti",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        // Macros: Carbs / Protein / Fat
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MacroSummary("Carboidrati", carbs, carbsGoal, Modifier.weight(1f))
                            MacroSummary("Proteine", protein, proteinGoal, Modifier.weight(1f))
                            MacroSummary("Grassi", fat, fatGoal, Modifier.weight(1f))
                        }
                    }
                }

                Spacer(Modifier.height(30.dp))

                // List of meals
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
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

                Spacer(Modifier.weight(1f))
            }

            // Floating Add Meal button (bottom left)
            FloatingActionButton(
                onClick = { navController.navigate("add_meal") },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, bottom = 35.dp),
                containerColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Aggiungi pasto", tint = Color.Black)
            }

            // Floating AI Chat button (bottom right)
            FloatingActionButton(
                onClick = { navController.navigate("ai_chat") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 35.dp),
                containerColor = Color.White
            ) {
                Icon(Icons.Default.Send, contentDescription = "AI Chat", tint = Color.Black)
            }
        }
    }
}

@Composable
fun MacroSummary(label: String, value: Int, goal: Int, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
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