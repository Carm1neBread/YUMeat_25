package com.example.yumeat_25.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yumeat_25.R
import com.example.yumeat_25.data.MealRepository
import com.example.yumeat_25.data.UserProfileRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    navController: NavController,
    mealName: String,
    mealRepository: MealRepository,
    userProfileRepository: UserProfileRepository,
    safeMode: Boolean = false
) {
    val meals by mealRepository.meals.collectAsState()
    val meal = meals.find { it.name == mealName }
    val recipeImage = meal?.imageRes ?: R.drawable.placeholder
    val hasIngredients = meal?.ingredientTitles != null && meal.ingredientRows != null

    // State for showing dialog
    var showAddDialog by remember { mutableStateOf(false) }

    // --- SAFE MODE logic ---
    var showActiveListening by remember { mutableStateOf(safeMode) }
    var showFeelBadDialog by remember { mutableStateOf(false) }
    var showPauseScreen by remember { mutableStateOf(false) }
    var pauseSecondsLeft by remember { mutableStateOf(30) }
    val coroutineScope = rememberCoroutineScope()
    var pauseTimerRunning by remember { mutableStateOf(false) }

    // NEW: Track if safe mode popup flow has already been completed (only once per detail session)
    var safeModePopupCompleted by remember { mutableStateOf(false) }

    // Timer logic for the blocking screen
    fun startPauseTimer() {
        pauseSecondsLeft = 30
        pauseTimerRunning = true
        coroutineScope.launch {
            while (pauseSecondsLeft > 0 && pauseTimerRunning) {
                delay(1000)
                pauseSecondsLeft -= 1
            }
            pauseTimerRunning = false
            showPauseScreen = false
            if (safeMode && !safeModePopupCompleted) {
                safeModePopupCompleted = true // Blocca il flusso dopo la prima esecuzione (timer scaduto)
            }
        }
    }

    // Blocca aggiunta pasto se una delle modali safe mode è attiva
    val canAdd = !showActiveListening && !showFeelBadDialog && !showPauseScreen

    // Overlay BLOCK SCREEN a livello root, copre TUTTO (anche la topbar)
    Box(modifier = Modifier.fillMaxSize()) {
        // Blocca TUTTO (anche topbar) se il timer è attivo
        if (showPauseScreen) {
            PauseBlockScreen(
                secondsLeft = pauseSecondsLeft,
                onInterrupt = {
                    pauseTimerRunning = false
                    showPauseScreen = false
                    safeModePopupCompleted = true // Blocca il flusso dopo la prima esecuzione (interruzione manuale)
                }
            )
        } else {
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
                            textAlign = TextAlign.Start
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
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
                                    // Nutritional info (mostra solo se NON safeMode)
                                    if (!safeMode) {
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
                                                    textAlign = TextAlign.Center
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
                                                        textAlign = TextAlign.Center
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
                                        onClick = {
                                            if (safeMode && !safeModePopupCompleted) {
                                                showActiveListening = true
                                            } else {
                                                showAddDialog = true
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(46.dp)
                                            .clip(RoundedCornerShape(12.dp)),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF295B4F),
                                            contentColor = Color.White
                                        ),
                                        enabled = canAdd
                                    ) {
                                        Text("Aggiungi", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                        }

                        // --- SAFE MODE POPUPS ---
                        if (showActiveListening) {
                            ActiveListeningDialog(
                                onFeelGood = {
                                    showActiveListening = false
                                    showAddDialog = true
                                    safeModePopupCompleted = true // BLOCCA popup su aggiunte successive
                                },
                                onFeelBad = {
                                    showActiveListening = false
                                    showFeelBadDialog = true
                                }
                            )
                        }
                        if (showFeelBadDialog) {
                            FeelBadDialog(
                                onTakePause = {
                                    showFeelBadDialog = false
                                    showPauseScreen = true
                                    startPauseTimer()
                                },
                                onProceed = {
                                    showFeelBadDialog = false
                                    showAddDialog = true
                                    safeModePopupCompleted = true // BLOCCA popup su aggiunte successive
                                }
                            )
                        }
                        // --- Dialog to add meal to meal time ---
                        if (showAddDialog && meal != null) {
                            AddMealToTimeDialog(
                                onDismiss = { showAddDialog = false },
                                onConfirm = { mealTime ->
                                    userProfileRepository.addMealToMealTime(meal, mealTime)
                                    showAddDialog = false
                                    navController.popBackStack("main", inclusive = false)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/* ----------- SAFE MODE POPUP UI COMPOSABLES ----------- */

@Composable
fun ActiveListeningDialog(
    onFeelGood: () -> Unit,
    onFeelBad: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                TextButton(onClick = onFeelGood) {
                    Text("Bene", color = Color(0xFF295B4F), fontWeight = FontWeight.SemiBold)
                }
                TextButton(onClick = onFeelBad) {
                    Text("Male", color = Color.Red, fontWeight = FontWeight.SemiBold)
                }
            }
        },
        title = {
            Text("Ascolto attivo 🧘‍♂️", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            Text("Che bel piatto!\nCome ti fa sentire?")
        },
        dismissButton = {},
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
    )
}

@Composable
fun FeelBadDialog(
    onTakePause: () -> Unit,
    onProceed: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                TextButton(onClick = onTakePause) {
                    Text("Certo", color = Color(0xFF295B4F), fontWeight = FontWeight.SemiBold)
                }
                TextButton(onClick = onProceed) {
                    Text("No", color = Color.Red, fontWeight = FontWeight.SemiBold)
                }
            }
        },
        title = {
            Text("Ascolto attivo 🧘‍♂️", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            Text("Mi dispiace!\nVuoi pensarci su prima di aggiungerlo?")
        },
        dismissButton = {},
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
    )
}

@Composable
fun PauseBlockScreen(
    secondsLeft: Int,
    onInterrupt: () -> Unit
) {
    // Blocca TUTTO lo schermo!
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xDD111111)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Transparent, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.clock), // Usa la tua icona personalizzata!
                    contentDescription = "Orologio",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "Prenditi una pausa,\n30 secondi possono fare la differenza.",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            if (secondsLeft > 0) {
                Text(
                    "$secondsLeft secondi",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onInterrupt,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Text("Interrompi", color = Color(0xFF295B4F), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/* ------------------- DIALOG PER AGGIUNTA ------------- */

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