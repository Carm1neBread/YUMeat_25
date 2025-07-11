package com.example.yumeat_25.ui.screens.profile

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yumeat_25.data.UserProfileRepository
import com.example.yumeat_25.data.DietaryPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietaryPreferencesScreen(
    navController: NavController,
    userProfileRepository: UserProfileRepository
) {
    val userProfile by userProfileRepository.userProfile.collectAsState()
    val originalPrefs = userProfile.dietaryPreferences

    var editMode by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var selectedDiet by remember { mutableStateOf(
        when {
            originalPrefs.vegan -> "Vegana"
            originalPrefs.vegetarian -> "Vegetariana"
            else -> "Onnivora"
        }
    ) }
    var avoidRedMeat by remember { mutableStateOf(originalPrefs.avoidRedMeat) }
    var avoidDairy by remember { mutableStateOf(originalPrefs.dairyFree) }
    var avoidGluten by remember { mutableStateOf(originalPrefs.glutenFree) }
    var avoidSugar by remember { mutableStateOf(originalPrefs.avoidSugar) }

    LaunchedEffect(userProfile, editMode) {
        if (!editMode) {
            selectedDiet = when {
                userProfile.dietaryPreferences.vegan -> "Vegana"
                userProfile.dietaryPreferences.vegetarian -> "Vegetariana"
                else -> "Onnivora"
            }
            avoidRedMeat = userProfile.dietaryPreferences.avoidRedMeat
            avoidDairy = userProfile.dietaryPreferences.dairyFree
            avoidGluten = userProfile.dietaryPreferences.glutenFree
            avoidSugar = userProfile.dietaryPreferences.avoidSugar
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        },
        snackbarHost = {
            Box(
                Modifier
                    .fillMaxSize()
            ) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.Center),
                    snackbar = { data ->
                        Snackbar(
                            snackbarData = data,
                            containerColor = Color(0xFF295B4F),
                            contentColor = Color.White
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Personalizza la tua\nesperienza alimentare!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(),
                lineHeight = 28.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Attiva solo ciò che ti fa sentire a tuo agio.\nPuoi cambiare tutto in qualsiasi momento.",
                fontSize = 15.sp,
                color = Color(0xFF222222),
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 32.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // Tipo di alimentazione
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
                    val dietOptions = listOf("Onnivora", "Vegetariana", "Vegana")
                    dietOptions.forEach { option ->
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
                                selected = selectedDiet == option,
                                onClick = { if (editMode) selectedDiet = option },
                                enabled = editMode,
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

            // Alimenti da evitare
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
                        checked = avoidRedMeat,
                        enabled = editMode,
                        onCheckedChange = { if (editMode) avoidRedMeat = it }
                    )
                    AvoidItem(
                        label = "Latticini",
                        checked = avoidDairy,
                        enabled = editMode,
                        onCheckedChange = { if (editMode) avoidDairy = it }
                    )
                    AvoidItem(
                        label = "Glutine",
                        checked = avoidGluten,
                        enabled = editMode,
                        onCheckedChange = { if (editMode) avoidGluten = it }
                    )
                    AvoidItem(
                        label = "Zucchero",
                        checked = avoidSugar,
                        enabled = editMode,
                        onCheckedChange = { if (editMode) avoidSugar = it }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (editMode) {
                        val newPrefs = DietaryPreferences(
                            vegetarian = selectedDiet == "Vegetariana",
                            vegan = selectedDiet == "Vegana",
                            avoidRedMeat = avoidRedMeat,
                            dairyFree = avoidDairy,
                            glutenFree = avoidGluten,
                            avoidSugar = avoidSugar
                        )
                        scope.launch {
                            userProfileRepository.updateDietaryPreferences(newPrefs)
                            snackbarHostState.showSnackbar("Preferenze salvate con successo!")
                        }
                    }
                    editMode = !editMode
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor =Color(0xFF1F5F5B),
                    contentColor = Color.White
                )
            ) {
                Text(
                    if (editMode) "Conferma" else "Modifica preferenze",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
private fun AvoidItem(
    label: String,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
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
            onClick = { onCheckedChange(!checked) },
            enabled = enabled,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF000000),
                unselectedColor = Color(0xFF000000),
                disabledSelectedColor = Color.Gray,
                disabledUnselectedColor = Color.LightGray
            )
        )
    }
}