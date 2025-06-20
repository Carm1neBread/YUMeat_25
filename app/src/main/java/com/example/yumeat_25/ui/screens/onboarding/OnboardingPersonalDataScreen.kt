package com.example.yumeat_25.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yumeat_25.data.PersonalData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingPersonalDataScreen(
    onNext: (PersonalData) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    val isFormValid = name.isNotBlank()

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
                text = "Dati personali",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LinearProgressIndicator(
            progress = 0.33f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        Text(
            text = "Raccontaci qualcosa di te",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Form
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome *") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Età (opzionale)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Altezza (opzionale)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Peso (opzionale)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                onNext(PersonalData(name, age, height, weight))
            },
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Continua", fontSize = 16.sp)
        }
    }
}
