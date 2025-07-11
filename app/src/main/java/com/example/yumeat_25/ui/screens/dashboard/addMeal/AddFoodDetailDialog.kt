package com.example.yumeat_25.ui.screens.dashboard.addMeal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yumeat_25.data.Food

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodDetailDialog(
    defaultFood: Food,
    onDismiss: () -> Unit,
    onConfirm: (Food) -> Unit
) {
    val unitOptions = listOf("quantità", "g")
    var selectedUnit by remember { mutableStateOf(unitOptions[1]) }
    var quantity by remember { mutableStateOf("100") }
    var amountError by remember { mutableStateOf(false) }

    val amount = quantity.toIntOrNull()?.coerceAtLeast(1) ?: 1
    val base = when (selectedUnit) {
        "g" -> amount / 100f
        else -> amount.toFloat()
    }
    val totalCalories = (defaultFood.calories * base).toInt()
    val totalCarbs = (defaultFood.carbs * base)
    val totalProtein = (defaultFood.protein * base)
    val totalFat = (defaultFood.fat * base)

    val customColor = Color(0xFF1F5F5B)
    val customColor2 = Color.Red;

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (quantity.isBlank() || quantity.toIntOrNull() == null || quantity.toInt() < 1) {
                        amountError = true
                        return@Button
                    }
                    onConfirm(
                        defaultFood.copy(
                            calories = totalCalories,
                            carbs = totalCarbs.toInt(),
                            protein = totalProtein.toInt(),
                            fat = totalFat.toInt()
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = customColor,
                    contentColor = Color.White
                )
            ) {
                Text("Aggiungi")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = customColor2
                )
            ) {
                Text("Annulla")
            }
        },
        title = {
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Modifica quantità", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFF6F6F6))
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            defaultFood.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Calorie", fontSize = 15.sp, color = Color.Black)
                            Text("$totalCalories kcal", fontSize = 15.sp, color = Color.Black)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Carboidrati", fontSize = 15.sp, color = Color.Black)
                            Text(String.format("%.1f g", totalCarbs), fontSize = 15.sp, color = Color.Black)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Proteine", fontSize = 15.sp, color = Color.Black)
                            Text(String.format("%.1f g", totalProtein), fontSize = 15.sp, color = Color.Black)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Grassi", fontSize = 15.sp, color = Color.Black)
                            Text(String.format("%.1f g", totalFat), fontSize = 15.sp, color = Color.Black)
                        }

                        Spacer(Modifier.height(32.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            OutlinedTextField(
                                value = quantity,
                                onValueChange = {
                                    if (it.all { c -> c.isDigit() } && it.length <= 4) {
                                        quantity = it
                                        amountError = false
                                    }
                                },
                                singleLine = true,
                                label = null,
                                isError = amountError,
                                modifier = Modifier
                                    .width(90.dp)
                                    .height(48.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            var expanded by remember { mutableStateOf(false) }
                            Box {
                                OutlinedButton(
                                    onClick = { expanded = true },
                                    modifier = Modifier.height(48.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color.Black
                                    )
                                ) {
                                    Text(selectedUnit, color = Color.Black)
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = Color.Black
                                    )
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    unitOptions.forEach { unit ->
                                        DropdownMenuItem(
                                            text = { Text(unit) },
                                            onClick = {
                                                selectedUnit = unit
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        if (amountError) {
                            Text(
                                "Inserisci un valore valido",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}