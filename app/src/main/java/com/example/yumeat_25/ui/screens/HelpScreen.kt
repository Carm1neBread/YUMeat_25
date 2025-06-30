package com.example.yumeat_25.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HelpScreen(
    navController: NavController,
    onEmergencyClick: () -> Unit = {},
    onTrustContactClick: () -> Unit = {},
    onSupportPageClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        // Row for back arrow and centered title at the same height
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min) // Ensures proper centering even with large font
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Indietro",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Aiuto",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            // Invisible placeholder for symmetry (same size as IconButton)
            Box(modifier = Modifier.size(48.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Hai bisogno di parlare con qualcuno?",
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(26.dp))

        HelpOptionRow(
            icon = Icons.Default.Call,
            label = "Numero d’emergenza",
            onClick = onEmergencyClick
        )
        Spacer(modifier = Modifier.height(12.dp))

        HelpOptionRow(
            icon = Icons.Default.Call,
            label = "Contatto di fiducia",
            onClick = onTrustContactClick
        )
        Spacer(modifier = Modifier.height(12.dp))

        HelpOptionRow(
            icon = Icons.Default.Search,
            label = "Pagina di supporto",
            onClick = onSupportPageClick
        )
    }
}

@Composable
fun HelpOptionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(
                role = Role.Button,
                onClick = onClick
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(18.dp))
        Text(
            text = label,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            color = Color.Black
        )
    }
}