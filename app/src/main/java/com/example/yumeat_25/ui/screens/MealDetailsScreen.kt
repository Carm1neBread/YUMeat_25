package com.example.yumeat_25.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yumeat_25.data.UserProfileRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailsScreen(
    navController: NavController,
    userProfileRepository: UserProfileRepository
) {
    val userProfile by userProfileRepository.userProfile.collectAsState()
    val mealsList = listOf(
        "Colazione" to userProfile.breakfast,
        "Pranzo" to userProfile.lunch,
        "Cena" to userProfile.dinner
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dettaglio pasti") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            mealsList.forEach { (mealName, foods) ->
                item {
                    Text(
                        text = mealName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                if (foods.isEmpty()) {
                    item {
                        Text(
                            text = "Nessun alimento aggiunto",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                } else {
                    items(foods) { food ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp, horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(food.name, fontWeight = FontWeight.Medium)
                                Text(
                                    food.type.displayName,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("${food.calories} kcal", fontWeight = FontWeight.SemiBold)
                                Text(
                                    "C:${food.carbs}g P:${food.protein}g F:${food.fat}g",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            IconButton(
                                onClick = { userProfileRepository.removeFoodFromMeal(mealName, food) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Rimuovi alimento",
                                    tint = Color.Red
                                )
                            }
                        }
                        Divider()
                    }
                }
            }
        }
    }
}