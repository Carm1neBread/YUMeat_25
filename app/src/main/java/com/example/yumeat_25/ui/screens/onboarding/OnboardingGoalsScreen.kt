package com.example.yumeat_25.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yumeat_25.data.UserGoals

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingGoalsScreen(
    onComplete: (UserGoals) -> Unit,
    onBack: () -> Unit
) {
    var selectedGoal by remember { mutableStateOf("") }
    var safeMode by remember { mutableStateOf(false) }

    val goals = listOf(
        "Mangiare in modo più consapevole",
        "Migliorare il rapporto con il cibo",
        "Aumentare la varietà alimentare",
        "Ridurre lo stress legato ai pasti",
        "Sviluppare abitudini alimentari sane"
    )

    val isFormValid = selectedGoal.isNotEmpty()

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
                text = "I tuoi obiettivi",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LinearProgressIndicator(
            progress = 1f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        Text(
            text = "Qual è il tuo obiettivo principale?",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(goals) { goal ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selectedGoal == goal,
                            onClick = { selectedGoal = goal },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedGoal == goal,
                        onClick = { selectedGoal = goal }
                    )
                    Text(
                        text = goal,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = safeMode,
                                onCheckedChange = { safeMode = it }
                            )
                            Text(
                                text = "Attiva modalità Safe",
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        Text(
                            text = "Nasconde numeri e contenuti potenzialmente triggeranti",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 40.dp, top = 4.dp)
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                onComplete(
                    UserGoals(
                        primaryGoal = selectedGoal,
                        safeMode = safeMode
                    )
                )
            },
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Completa configurazione", fontSize = 16.sp)
        }
    }
}