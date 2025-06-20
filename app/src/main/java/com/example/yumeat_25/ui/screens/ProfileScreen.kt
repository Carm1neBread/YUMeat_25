package com.example.yumeat_25.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yumeat_25.data.UserProfileRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userProfileRepository: UserProfileRepository
) {
    val userProfile by userProfileRepository.userProfile.collectAsState()
    val completionStatus = userProfile.getCompletionStatus()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profilo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Il tuo profilo",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Sezione Dati Personali
            item {
                ProfileSection(
                    title = "Dati personali",
                    isCompleted = completionStatus["personal_data"] == true,
                    content = {
                        if (userProfile.personalData.name.isNotEmpty()) {
                            ProfileItem("Nome", userProfile.personalData.name)
                            if (userProfile.personalData.age.isNotEmpty()) {
                                ProfileItem("Età", userProfile.personalData.age)
                            }
                            if (userProfile.personalData.height.isNotEmpty()) {
                                ProfileItem("Altezza", userProfile.personalData.height)
                            }
                            if (userProfile.personalData.weight.isNotEmpty()) {
                                ProfileItem("Peso", userProfile.personalData.weight)
                            }
                        } else {
                            Text(
                                text = "Completa i tuoi dati personali",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            }

            // Sezione Preferenze Alimentari
            item {
                ProfileSection(
                    title = "Preferenze alimentari",
                    isCompleted = completionStatus["dietary_preferences"] == true,
                    content = {
                        val preferences = userProfile.dietaryPreferences
                        if (preferences.vegetarian) ProfileItem("Dieta", "Vegetariana")
                        if (preferences.vegan) ProfileItem("Dieta", "Vegana")
                        if (preferences.glutenFree) ProfileItem("Restrizione", "Senza glutine")
                        if (preferences.dairyFree) ProfileItem("Restrizione", "Senza lattosio")
                        if (preferences.allergies.isNotEmpty()) {
                            ProfileItem("Allergie", preferences.allergies.joinToString(", "))
                        }

                        if (!preferences.vegetarian && !preferences.vegan &&
                            !preferences.glutenFree && !preferences.dairyFree &&
                            preferences.allergies.isEmpty()) {
                            Text(
                                text = "Nessuna restrizione alimentare",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            }

            // Sezione Obiettivi
            item {
                ProfileSection(
                    title = "Obiettivi",
                    isCompleted = completionStatus["goals"] == true,
                    content = {
                        if (userProfile.goals.primaryGoal.isNotEmpty()) {
                            ProfileItem("Obiettivo principale", userProfile.goals.primaryGoal)
                            if (userProfile.goals.safeMode) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Person, //Eye
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Modalità Safe attiva",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = "Imposta i tuoi obiettivi",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileSection(
    title: String,
    isCompleted: Boolean,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )

                // Intervento 4: Indicazione visiva completamento
                if (isCompleted) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Completato",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Icon(
                        Icons.Default.CheckCircle, //RadioButton
                        contentDescription = "Da completare",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            content()
        }
    }
}

@Composable
fun ProfileItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = "$label:",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.6f)
        )
    }
}
