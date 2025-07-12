package com.example.yumeat_25.ui.screens.dashboard.addMeal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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

    // Ingredienti fittizi per la poke bowl
    val ingredientTitles = listOf("Base", "Proteine", "Condimenti")
    val ingredientRows = listOf(
        listOf("Riso", "Salmone", "Salsa di soia"),
        listOf("Avocado", "Tonno", "Sesamo"),
        listOf("Cetriolo", "Gamberetti", "Wasabi"),
        listOf("Edamame", "Uova", "Zenzero")
    )

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
    var showActiveListening by remember { mutableStateOf(false) }
    var showFeelBadDialog by remember { mutableStateOf(false) }
    var showPauseScreen by remember { mutableStateOf(false) }
    var pauseSecondsLeft by remember { mutableStateOf(30) }
    val coroutineScope = rememberCoroutineScope()
    var pauseTimerRunning by remember { mutableStateOf(false) }
    var safeModePopupCompleted by remember { mutableStateOf(false) }

    // Meal time selection
    var showMealTimeDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Timer function per la schermata di pausa
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
            if (isSafeMode) {
                safeModePopupCompleted = true
            }
        }
    }

    // Blocca aggiunta se modalità safe è attiva e modale è visualizzata
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
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            // Header con nome e emoji
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = foodName,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Immagine
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = foodName,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Sezione valori nutrizionali
                            if (!isSafeMode) {
                                Text(
                                    text = "Valori nutrizionali",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Calorie: $calories kcal",
                                        fontSize = 14.sp,
                                        color = Color(0xFF555555),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Carboidrati: ${carbs}g",
                                        fontSize = 14.sp,
                                        color = Color(0xFF555555),
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Proteine: ${protein}g",
                                        fontSize = 14.sp,
                                        color = Color(0xFF555555),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Grassi: ${fat}g",
                                        fontSize = 14.sp,
                                        color = Color(0xFF555555),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                SafeModeNutrients()
                            }

                            // Sezione ingredienti
                            Text(
                                text = "Ingredienti",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                ingredientTitles.forEach {
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

                            ingredientRows.forEach { row ->
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

                            Spacer(modifier = Modifier.height(24.dp))

                            // Pulsante di aggiunta - logica modificata
                            Button(
                                onClick = {
                                    if (isSafeMode && !safeModePopupCompleted) {
                                        // Se in safe mode e non ancora mostrato il popup, mostra il popup
                                        showActiveListening = true
                                    } else {
                                        // Altrimenti vai direttamente al dialog di selezione del pasto
                                        showMealTimeDialog = true
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF295B4F),
                                    contentColor = Color.White
                                ),
                                enabled = canAdd
                            ) {
                                Text("Aggiungi", fontSize = 16.sp)
                            }
                        }
                    }
                }
            }

            // Safe Mode dialogs
            if (showActiveListening) {
                ActiveListeningDialog(
                    onFeelGood = {
                        showActiveListening = false
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
                    .background(Color.Transparent, shape = CircleShape),
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
    val customColor1 = Color(color=0xFF0694F4);
    val customColor2 = Color.Red;
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
            TextButton(onClick = { onConfirm(selectedMealTime) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = customColor1
                )
            ) {
                Text("Aggiungi")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = customColor2
                )
            ) {
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