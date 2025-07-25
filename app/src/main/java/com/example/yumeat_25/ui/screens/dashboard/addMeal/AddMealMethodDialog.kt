package com.example.yumeat_25.ui.screens.dashboard.addMeal

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.yumeat_25.R

@Composable
fun AddMealMethodDialog(
    onDismiss: () -> Unit,
    onManualAddClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Aggiungi pasto",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Opzione per aggiunta manuale (usando l'icona predefinita)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Added elevation
                ) {
                    AddMethodOptionWithResource(
                        iconResId = R.drawable.edit,
                        title = "Aggiungere maualmente",
                        onClick = {
                            onDismiss()
                            onCameraClick()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Opzione per aggiunta da libreria (usando l'icona personalizzata)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Added elevation
                ) {
                    AddMethodOptionWithResource(
                        iconResId = R.drawable.photo_library,
                        title = "Aggiungere dalla libreria",
                        onClick = {
                            onDismiss()
                            onGalleryClick()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Opzione per aggiunta da fotocamera (usando l'icona personalizzata)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Added elevation
                ) {
                    AddMethodOptionWithResource(
                        iconResId = R.drawable.camera,
                        title = "Aggiungere con foto",
                        onClick = {
                            onDismiss()
                            onCameraClick()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Annulla",
                        color = Color(0xFFFF0000),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun AddMethodOptionWithVector(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF1F5F5B),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Composable
private fun AddMethodOptionWithResource(
    iconResId: Int,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}