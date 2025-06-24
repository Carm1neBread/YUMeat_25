package com.example.yumeat_25.ui.screens

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
import com.example.yumeat_25.data.UserGoals
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    navController: NavController,
    userProfileRepository: UserProfileRepository
) {
    val userProfile by userProfileRepository.userProfile.collectAsState()
    val goals = userProfile.goals

    val goalOptions = listOf(
        "Perdere peso in modo sano e graduale",
        "Mantenere il mio peso attuale",
        "Aumentare massa muscolare",
        "Mangiare in modo più equilibrato",
        "Ritrovare serenità nel rapporto con il cibo",
        "Nessun obiettivo fisico per ora"
    )

    var editMode by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var selectedGoal by remember { mutableStateOf<String?>(goals.primaryGoal.takeIf { it in goalOptions }) }
    var safeMode by remember { mutableStateOf(goals.safeMode) }

    // Sync with repository when not editing
    LaunchedEffect(userProfile, editMode) {
        if (!editMode) {
            selectedGoal = userProfile.goals.primaryGoal.takeIf { it in goalOptions }
            safeMode = userProfile.goals.safeMode
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
                            contentColor = Color.White // Optional: black text for contrast
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
                .padding(24.dp)
        ) {
            // Title
            Text(
                text = "Qual è il tuo obiettivo?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(),
                lineHeight = 28.sp,
                textAlign = TextAlign.Center
            )
            // Subtitle
            Text(
                text = "Scegli un obiettivo che vuoi raggiungere",
                fontSize = 15.sp,
                color = Color(0xFF222222),
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 32.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // Options in a rounded grey wrapper
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF3F3F3))
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
                goalOptions.forEach { option ->
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
                            onClick = { if (editMode) selectedGoal = option },
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
            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.weight(1f))

            val isButtonEnabled = selectedGoal != null

            Button(
                onClick = {
                    if (editMode && selectedGoal != null) {
                        val newGoals = UserGoals(
                            primaryGoal = selectedGoal!!,
                            safeMode = safeMode
                        )
                        scope.launch {
                            userProfileRepository.updateGoals(newGoals)
                            snackbarHostState.showSnackbar("Obiettivi salvati con successo!")
                        }
                    }
                    editMode = !editMode
                },
                enabled = editMode || isButtonEnabled, // Always enabled in view mode, only check for null in edit mode
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1F5F5B),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    if (editMode) "Conferma" else "Modifica obiettivi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}