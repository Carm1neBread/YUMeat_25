package com.example.yumeat_25.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yumeat_25.R
import androidx.compose.foundation.Image

@Composable
fun SettingsScreen(
    navController: NavController,
    isLightMode: Boolean = true,
    onThemeChange: (Boolean) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var selectedLightMode by remember { mutableStateOf(isLightMode) }
    var showSnackbar by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Snackbar effect: shows when showSnackbar becomes true, then auto-hide and switch back to light mode
    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar(
                message = "Funzione in arrivo",
                withDismissAction = true
            )
            selectedLightMode = true
            onThemeChange(true)
            showSnackbar = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            // Top bar with back and title centered
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Impostazioni",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Indietro", tint = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Row for dark mode icon and theme switch
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 6.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.moon), // usa il nome del tuo file senza estensione
                    contentDescription = "Dark mode",
                    modifier = Modifier.size(36.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Theme Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SettingsModeToggleButton(
                        text = "Modalità chiara",
                        selected = selectedLightMode,
                        onClick = {
                            selectedLightMode = true
                            onThemeChange(true)
                        }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    SettingsModeToggleButton(
                        text = "Modalità scrura",
                        selected = !selectedLightMode,
                        onClick = {
                            showSnackbar = true
                            // La selezione torna a chiara appena la snackbar si chiude (gestito sopra)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Logout
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        navController.navigate("onboarding_welcome") {
                            popUpTo(0) { inclusive = true }
                        }
                        onLogout()
                    }
                    .padding(start = 6.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = "Logout",
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(
                    text = "Logout",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
        }

        // Snackbar Host: verde come le altre notifiche
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        ) { snackbarData ->
            Snackbar(
                snackbarData = snackbarData,
                containerColor = Color(0xFF295B4F), // Verde custom
                contentColor = Color.White
            )
        }
    }
}

@Composable
fun SettingsModeToggleButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) Color(0xFF295B4F) else Color(0xFFE8E8E8)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.Black,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}