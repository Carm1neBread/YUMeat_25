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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        // Top bar with back and title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Indietro", tint = Color.Black)
            }
            Spacer(Modifier.weight(1f))

            Text(
                text = "Impostazioni",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = -20.dp)
                    .weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Start
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        // Row for dark mode icon and theme switch
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 6.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.btn_star_big_off), // Replace with a moon vector if you have one
                contentDescription = "Dark mode",
                tint = Color.Black,
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
                        selectedLightMode = false
                        onThemeChange(false)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Logout
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onLogout() }
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