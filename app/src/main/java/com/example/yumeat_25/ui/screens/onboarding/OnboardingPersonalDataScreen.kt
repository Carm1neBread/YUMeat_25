package com.example.yumeat_25.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yumeat_25.data.PersonalData
import androidx.compose.material3.TextFieldDefaults

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
            modifier = Modifier.fillMaxWidth().padding(top=16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
            }
            Spacer(modifier = Modifier.width(16.dp))
        }

        LinearProgressIndicator(
            progress = 0.33f,
            color = Color(0xFF1F5F5B),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 32.dp)
        )

        Text(
            text = "Dati personali",
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
            text = "Raccontaci qualcosa di te!",
            fontSize = 15.sp,
            color = Color(0xFF222222),
            modifier = Modifier
                .padding(top = 12.dp, bottom = 32.dp)
                .fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Form
        GrayRoundedTextField(
            value = name,
            onValueChange = { name = it },
            label = "Nome *",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        GrayRoundedTextField(
            value = age,
            onValueChange = { age = it },
            label = "Età (opzionale)",
            keyboardType = KeyboardType.Number,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        GrayRoundedTextField(
            value = height,
            onValueChange = { height = it },
            label = "Altezza (opzionale)",
            keyboardType = KeyboardType.Number,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        GrayRoundedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = "Peso (opzionale)",
            keyboardType = KeyboardType.Number,
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
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1F5F5B),
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

@Composable
private fun GrayRoundedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .background(Color(0xFFF3F3F3), RoundedCornerShape(20.dp))
            .height(56.dp)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(label, color = Color.Black) },
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 0.dp)
        )
    }
}