package com.example.yumeat_25.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yumeat_25.R
import com.example.yumeat_25.data.PersonalData
import com.example.yumeat_25.data.UserProfileRepository
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.CircleShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(
    navController: NavController,
    userProfileRepository: UserProfileRepository
) {
    val userProfile by userProfileRepository.userProfile.collectAsState()
    var editMode by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Editable fields state
    var name by remember { mutableStateOf(userProfile.personalData.name) }
    var age by remember { mutableStateOf(userProfile.personalData.age) }
    var height by remember { mutableStateOf(userProfile.personalData.height) }
    var weight by remember { mutableStateOf(userProfile.personalData.weight) }

    // Sync fields when editMode is off or userProfile changes
    LaunchedEffect(userProfile, editMode) {
        if (!editMode) {
            name = userProfile.personalData.name
            age = userProfile.personalData.age
            height = userProfile.personalData.height
            weight = userProfile.personalData.weight
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Ciao, $name!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.prof),
                    contentDescription = "User",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape) // This makes the image a perfect circle
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { if (editMode) name = it },
                    label = { Text("Nome") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    enabled = editMode,
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = age,
                    onValueChange = { if (editMode) age = it },
                    label = { Text("Età") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    enabled = editMode,
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = height,
                    onValueChange = { if (editMode) height = it },
                    label = { Text("Altezza") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    enabled = editMode,
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = { if (editMode) weight = it },
                    label = { Text("Peso") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    enabled = editMode,
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (editMode) {
                            // Save changes with PersonalData object
                            scope.launch {
                                userProfileRepository.updatePersonalData(
                                    PersonalData(
                                        name = name,
                                        age = age,
                                        height = height,
                                        weight = weight
                                    )
                                )
                                snackbarHostState.showSnackbar("Dati modificati con successo!")
                            }
                        }
                        editMode = !editMode
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF295B4F),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        if (editMode) "Conferma" else "Modifica dati",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}