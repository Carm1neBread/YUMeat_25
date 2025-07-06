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
import com.example.yumeat_25.data.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingGoalsScreen(
    onNext: (UserGoals, safeMode: Boolean) -> Unit,
    onBack: () -> Unit
) {
    var selectedGoal by remember { mutableStateOf<String?>(null) }
    var safeMode by remember { mutableStateOf(false) }
    val goalsOptions = listOf(
        "Perdere peso in modo sano e graduale",
        "Mantenere il mio peso attuale",
        "Aumentare massa muscolare",
        "Mangiare in modo più equilibrato",
        "Ritrovare serenità nel rapporto con il cibo",
        "Nessun obiettivo fisico per ora"
    )

    val isButtonEnabled = selectedGoal != null

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
            progress = 1.0f,
            color = Color(0xFF1F5F5B),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 32.dp)
        )

        // Title
        Text(
            text = "Qual è il tuo obiettivo?",
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
            text = "Scegli un obiettivo che vuoi raggiungere",
            fontSize = 15.sp,
            color = Color(0xFF222222),
            modifier = Modifier
                .padding(top = 12.dp, bottom = 32.dp)
                .fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Options in a rounded grey wrapper -- now as Card with elevation
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
                goalsOptions.forEach { option ->
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
                            selected = selectedGoal == option,
                            onClick = { selectedGoal = option },
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
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        onCheckedChange = { safeMode = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF1F5F5B),          // Color when checked
                            uncheckedColor = Color(0xFFB0B0B0),        // Color when NOT checked
                            checkmarkColor = Color.White,              // Color of the checkmark
                            disabledCheckedColor = Color.Gray,         // Optional
                            disabledUncheckedColor = Color.LightGray   // Optional
                        )
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

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (selectedGoal != null) {
                    onNext(
                        UserGoals(
                            primaryGoal = selectedGoal!!,
                            safeMode = safeMode
                        ),
                        safeMode // Passa anche il valore del safeMode qui!
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