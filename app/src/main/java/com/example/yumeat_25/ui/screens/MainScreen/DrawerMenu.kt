package com.example.yumeat_25.ui.screens.MainScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yumeat_25.R

@Composable
fun DrawerMenuContent(
    key: Any = Unit,
    onItemClick: (String) -> Unit,
    onClose: () -> Unit
) {
    var profiloExpanded by remember(key) { mutableStateOf(false) }
    var supportoExpanded by remember(key) { mutableStateOf(false) }

    ModalDrawerSheet(
        modifier = Modifier
            .width(310.dp)
            .fillMaxHeight()
    ) {
        Spacer(Modifier.height(24.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Replace with your logo as needed (use painterResource if you have an image asset)
            Icon(
                painterResource(id = R.drawable.logo), // Replace with your app logo
                contentDescription = "Logo",
                tint = Color(0xFF64916D),
                modifier = Modifier.size(100.dp)
            )
        }
        DrawerMenuSection(
            expanded = profiloExpanded,
            label = "Profilo",
            icon = Icons.Default.Person,
            onExpandableClick = { profiloExpanded = !profiloExpanded }
        ) {
            DrawerSubMenuItem("Dati personali", onClick = onItemClick)
            DrawerSubMenuItem("Preferenze alimentari", onClick = onItemClick)
            DrawerSubMenuItem("Obiettivi", onClick = onItemClick)
        }
        DrawerMenuSection(
            expanded = supportoExpanded,
            label = "Supporto",
            icon = Icons.Default.Info,
            onExpandableClick = { supportoExpanded = !supportoExpanded }
        ) {
            DrawerSubMenuItem("Diario", onClick = onItemClick)
            DrawerSubMenuItem("Motivazione", onClick = onItemClick)
            DrawerSubMenuItem("Aiuto", onClick = onItemClick)
        }
        DrawerMenuItem("Challenge", Icons.Default.Star, onItemClick)
        DrawerMenuItem("Ricette consigliate", Icons.Default.DateRange, onItemClick)
        DrawerMenuItem("Impostazioni", Icons.Default.Settings, onItemClick)
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun DrawerMenuSection(
    expanded: Boolean,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onExpandableClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onExpandableClick() }
            .padding(vertical = 8.dp, horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label)
        Spacer(Modifier.width(10.dp))
        Text(label, fontSize = 16.sp)
        Spacer(Modifier.weight(1f))
        Icon(
            if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = if (expanded) "Collapse" else "Expand"
        )
    }
    if (expanded) {
        Column(Modifier.padding(start = 44.dp)) {
            content()
        }
    }
}

@Composable
fun DrawerSubMenuItem(
    label: String,
    onClick: (String) -> Unit
) {
    Text(
        label,
        fontSize = 15.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(label) }
            .padding(vertical = 6.dp)
    )
}

@Composable
fun DrawerMenuItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: (String) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick(label) }
            .padding(vertical = 12.dp, horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label)
        Spacer(Modifier.width(10.dp))
        Text(label, fontSize = 16.sp)
    }
}