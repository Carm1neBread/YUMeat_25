package com.example.yumeat_25.ui.screens.dashboard.addMeal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.yumeat_25.data.Food
import com.example.yumeat_25.data.FoodType
import com.example.yumeat_25.data.UserProfileRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoFoodDetailScreen(
    navController: NavController,
    userProfileRepository: UserProfileRepository,
    isSafeMode: Boolean,
    photoSource: String // "camera" o "gallery"
) {
    // Food details (fissi per la demo, in un'app reale sarebbero determinati da ML/AI)
    val foodName = "Poke Bowl"
    val calories = 450
    val carbs = 55
    val protein = 22
    val fat = 15
    val imageRes = R.drawable.poke_foto // Dovrai aggiungere questa immagine

    val food = Food(
        id = "photo_detected",
        name = foodName,
        type = FoodType.ONNIVORE,
        calories = calories,
        carbs = carbs,
        protein = protein,
        fat = fat
    )

    // Safe Mode Flow State
    var showActiveListening by remember { mutableStateOf(isSafeMode) }
    var showFeelBadDialog by remember { mutableStateOf(false) }
    var showPauseScreen by remember { mutableStateOf(false) }
    var pauseSecondsLeft by remember { mutableStateOf(30) }
    val coroutineScope = rememberCoroutineScope()
    var pauseTimerRunning by remember { mutableStateOf(false) }
    var safeModePopupCompleted by remember { mutableStateOf(false) }

    // Meal time selection
    var showMealTimeDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Timer function
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
            if (isSafeMode && !safeModePopupCompleted) {
                safeModePopupCompleted = true
            }
        }
    }

    // Blocca aggiunta se modalità safe è attiva
    val canAdd = !showActiveListening && !showFeelBadDialog && !showPauseScreen

    Box(modifier = Modifier.fillMaxSize()) {
        if (showPauseScreen) {
            PauseBlockScreen(
                secondsLeft = pauseSecondsLeft,
                onInterrupt = {
                    pauseTimerRunning = false
                    showPauseScreen = false
                    safeModePopupCompleted = true
                }
            )
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Cibo rilevato") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
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
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Abbiamo rilevato:",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = foodName,
                                modifier = Modifier
                                    .height(180.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                            )

                            Text(
                                text = foodName,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )

                            // Visualizzazione dei nutrienti in base alla modalità
                            if (!isSafeMode) {
                                NormalModeNutrients(calories, carbs, protein, fat)
                            } else {
                                SafeModeNutrients()
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (isSafeMode && !safeModePopupCompleted) {
                                showActiveListening = true
                            } else {
                                showMealTimeDialog = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1F5F5B),
                            contentColor = Color.White
                        ),
                        enabled = canAdd
                    ) {
                        Text("Aggiungi al diario", fontSize = 16.sp)
                    }
                }
            }

            // Safe Mode dialogs
            if (showActiveListening) {
                ActiveListeningDialog(
                    onFeelGood = {
                        showActiveListening = false
                        showMealTimeDialog = true
                        safeModePopupCompleted = true
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
                        showMealTimeDialog = true
                        safeModePopupCompleted = true
                    }
                )
            }

            // Meal time selection dialog
            if (showMealTimeDialog) {
                AddMealToTimeDialog(
                    onDismiss = { showMealTimeDialog = false },
                    onConfirm = { mealTime ->
                        userProfileRepository.addFoodToMeal(food, mealTime)
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun NormalModeNutrients(calories: Int, carbs: Int, protein: Int, fat: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Calorie", fontSize = 16.sp)
                Text("$calories kcal", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color(0xFFE0E0E0))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Carboidrati", fontSize = 16.sp)
                Text("${carbs}g", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color(0xFFE0E0E0))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Proteine", fontSize = 16.sp)
                Text("${protein}g", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color(0xFFE0E0E0))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Grassi", fontSize = 16.sp)
                Text("${fat}g", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun SafeModeNutrients() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "Questo piatto è ben bilanciato, con un buon apporto di carboidrati dal riso, proteine dal pesce e grassi buoni dall'avocado.",
            textAlign = TextAlign.Center,
            color = Color(0xFF555555),
            fontSize = 16.sp
        )
    }
}

@Composable
fun PauseBlockScreen(
    secondsLeft: Int,
    onInterrupt: () -> Unit
) {
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
                    .background(Color.Transparent, shape = RoundedCornerShape(40.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.clock),
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