package com.example.yumeat_25.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.example.yumeat_25.R

private const val MAX_FOODS_PER_MEAL = 3 // Cambia qui per modificare il massimo per card

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    userProfileRepository: UserProfileRepository,
    initialSafeMode: Boolean = false // AGGIUNTO parametro per safe mode iniziale
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var drawerOpenCount by remember { mutableStateOf(0) }
    var isSafeMode by rememberSaveable { mutableStateOf(initialSafeMode) } // Modificato qui

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenuContent(
                key = drawerOpenCount,
                onItemClick = { label ->
                    when (label) {
                        "Dati personali" -> navController.navigate("profile")
                        "Preferenze alimentari" -> navController.navigate("preferences")
                        "Obiettivi" -> navController.navigate("goals")
                        "Ricette consigliate" -> navController.navigate("recipes?safeMode=$isSafeMode")
                        "Motivazione" -> navController.navigate("motivation")
                        "Aiuto" -> navController.navigate("help")
                        "Challenge" -> navController.navigate("challenge")
                        "Impostazioni" -> navController.navigate("settings")
                        "Diario" -> navController.navigate("wellness_diary")
                    }
                    scope.launch { drawerState.close() }
                },
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        val userProfile by userProfileRepository.userProfile.collectAsState()

        val kcalGoal = userProfile.caloriesGoal
        val carbsGoal = userProfile.carbsGoal
        val proteinGoal = userProfile.proteinGoal
        val fatGoal = userProfile.fatGoal

        val currentCalories = userProfile.getCurrentCalories()
        val currentCarbs = userProfile.getCurrentCarbs()
        val currentProtein = userProfile.getCurrentProtein()
        val currentFat = userProfile.getCurrentFat()

        val mealsList = listOf(
            "Colazione" to userProfile.breakfast,
            "Pranzo" to userProfile.lunch,
            "Cena" to userProfile.dinner
        )

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
                        Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.size(30.dp))
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
                    IconButton(onClick = { isSafeMode = !isSafeMode }) {
                        val eyeIcon = if (isSafeMode) R.drawable.closedeye else R.drawable.openeye
                        Image(
                            painter = painterResource(id = eyeIcon),
                            contentDescription = if (isSafeMode) "Safe mode attivo" else "Safe mode disattivo",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                if (isSafeMode) {
                    SafeModeDashboard(
                        mealsList = mealsList,
                        onMealDetailsClick = { navController.navigate("meal_details?safeMode=true") }
                    )
                } else {
                    NormalDashboard(
                        kcalGoal = kcalGoal,
                        carbsGoal = carbsGoal,
                        proteinGoal = proteinGoal,
                        fatGoal = fatGoal,
                        currentCalories = currentCalories,
                        currentCarbs = currentCarbs,
                        currentProtein = currentProtein,
                        currentFat = currentFat,
                        mealsList = mealsList,
                        onMealDetailsClick = { navController.navigate("meal_details") }
                    )
                }

                Spacer(Modifier.weight(1f))
            }

            FloatingActionButton(
                onClick = { navController.navigate("add_meal?safeMode=$isSafeMode") },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, bottom = 50.dp),
                containerColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Aggiungi pasto", tint = Color.Black)
            }

            FloatingActionButton(
                onClick = { navController.navigate("ai_chat") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 50.dp),
                containerColor = Color.White
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ai),
                    contentDescription = "AI Chat",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
fun NormalDashboard(
    kcalGoal: Int,
    carbsGoal: Int,
    proteinGoal: Int,
    fatGoal: Int,
    currentCalories: Int,
    currentCarbs: Int,
    currentProtein: Int,
    currentFat: Int,
    mealsList: List<Pair<String, List<Food>>>,
    onMealDetailsClick: () -> Unit
) {
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
                .padding(vertical = 20.dp, horizontal = 18.dp),
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
                    progress = currentCalories / kcalGoal.toFloat(),
                    modifier = Modifier.size(140.dp),
                    strokeWidth = 18f,
                    backgroundColor = Color(0x22000000),
                    progressColor = Color.Black
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$currentCalories",
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
                        text = "Consumate",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MacroSummary("Carboidrati", currentCarbs, carbsGoal, Modifier.weight(1f))
                MacroSummary("Proteine", currentProtein, proteinGoal, Modifier.weight(1f))
                MacroSummary("Grassi", currentFat, fatGoal, Modifier.weight(1f))
            }
        }
    }

    Spacer(Modifier.height(30.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .heightIn(max = 360.dp), // Limita l'altezza massima della card pasti
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
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            mealsList.forEach { (mealName, foods) ->
                Column(Modifier.padding(vertical = 6.dp, horizontal = 8.dp)) {
                    Text(
                        text = mealName,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                    if (foods.isEmpty()) {
                        Text(
                            text = "Nessun alimento aggiunto",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    } else {
                        val shownFoods = foods.take(MAX_FOODS_PER_MEAL)
                        val foodNames = shownFoods.joinToString(", ") { it.name }
                        Text(
                            text = foodNames + if (foods.size > MAX_FOODS_PER_MEAL) ", ..." else "",
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp, bottom = 2.dp)
                        )
                        // Calcola i macro sull'INTERA lista di alimenti del pasto!
                        val totalCalories = foods.sumOf { it.calories }
                        val totalCarbs = foods.sumOf { it.carbs }
                        val totalProtein = foods.sumOf { it.protein }
                        val totalFat = foods.sumOf { it.fat }
                        Text(
                            text = "$totalCalories kcal | C:${totalCarbs}g P:${totalProtein}g F:${totalFat}g",
                            color = Color.Gray,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    }
                }
            }
            Text(
                text = "Dettagli",
                color = Color.Gray,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp)
                    .clickable { onMealDetailsClick() }
            )
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