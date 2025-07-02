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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yumeat_25.R
import androidx.compose.foundation.Image

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
            drawableResId = R.drawable.user,
            onExpandableClick = { profiloExpanded = !profiloExpanded }
        ) {
            DrawerSubMenuItem("Dati personali", onClick = onItemClick)
            DrawerSubMenuItem("Preferenze alimentari", onClick = onItemClick)
            DrawerSubMenuItem("Obiettivi", onClick = onItemClick)
        }
        DrawerMenuSection(
            expanded = supportoExpanded,
            label = "Supporto",
            drawableResId = R.drawable.help, // <-- Il tuo file PNG (help.png) in drawable
            onExpandableClick = { supportoExpanded = !supportoExpanded }
        ) {
            DrawerSubMenuItem("Diario", onClick = onItemClick)
            DrawerSubMenuItem("Motivazione", onClick = onItemClick)
            DrawerSubMenuItem("Aiuto", onClick = onItemClick)
        }
        DrawerMenuItemWithCustomDrawable(
            label = "Challenge",
            drawableResId = R.drawable.challenge,
            onClick = onItemClick
        )
        DrawerMenuItemWithCustomDrawable(
            label = "Ricette consigliate",
            drawableResId = R.drawable.ricette,
            onClick = onItemClick
        )
        DrawerMenuItemWithCustomDrawable(
            label = "Impostazioni",
            drawableResId = R.drawable.setting,
            onClick = onItemClick
        )
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun DrawerMenuSection(
    expanded: Boolean,
    label: String,
    icon: ImageVector? = null,
    drawableResId: Int? = null,
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
        when {
            drawableResId != null -> {
                Image(
                    painter = painterResource(id = drawableResId),
                    contentDescription = label,
                    modifier = Modifier.size(24.dp)
                )
            }
            icon != null -> {
                Icon(icon, contentDescription = label)
            }
        }
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
    icon: ImageVector,
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

@Composable
fun DrawerMenuItemWithCustomDrawable(
    label: String,
    drawableResId: Int,
    onClick: (String) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick(label) }
            .padding(vertical = 12.dp, horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = drawableResId),
            contentDescription = label,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(label, fontSize = 16.sp)
    }
}