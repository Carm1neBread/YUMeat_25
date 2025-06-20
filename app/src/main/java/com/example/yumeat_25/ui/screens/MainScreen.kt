package com.example.yumeat_25.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yumeat_25.data.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val mealRepository = remember { MealRepository() }
    val meals by mealRepository.meals.collectAsState()

    val todaysMeals = remember(meals) {
        mealRepository.getTodaysMeals().groupBy { it.type }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onNavigate = { route ->
                    navController.navigate(route)
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("YUMeat") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("add_meal") }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Aggiungi pasto")
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "I tuoi pasti di oggi",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(MealType.values()) { mealType ->
                    MealTypeCard(
                        mealType = mealType,
                        meals = todaysMeals[mealType] ?: emptyList()
                    )
                }

                if (meals.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🍽️",
                                    fontSize = 48.sp,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                Text(
                                    text = "Nessun pasto registrato oggi",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Tocca il pulsante + per iniziare",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MealTypeCard(
    mealType: MealType,
    meals: List<Meal>
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = mealType.displayName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (meals.isEmpty()) {
                Text(
                    text = "Nessun alimento aggiunto",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                meals.forEach { meal ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = meal.name,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        if (meal.notes.isNotEmpty()) {
                            Text(
                                text = meal.notes,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerContent(
    onNavigate: (String) -> Unit
) {
    val userProfileRepository = remember { UserProfileRepository() }
    val userProfile by userProfileRepository.userProfile.collectAsState()
    val completionStatus = userProfile.getCompletionStatus()

    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "YUMeat",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Navigation Items with completion indicators
            DrawerNavigationItem(
                icon = Icons.Default.Person,
                title = "Profilo",
                isCompleted = completionStatus["personal_data"] == true,
                onClick = { onNavigate("profile") }
            )

            DrawerNavigationItem(
                icon = Icons.Default.Person, //Book
                title = "Diario del benessere",
                onClick = { onNavigate("wellness_diary") }
            )

            DrawerNavigationItem(
                icon = Icons.Default.Person, //Resturant
                title = "Ricette",
                onClick = { onNavigate("recipes") }
            )

            DrawerNavigationItem(
                icon = Icons.Default.Person, //School
                title = "Educazione alimentare",
                onClick = { onNavigate("education") }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Safe Mode Toggle
            if (userProfile.goals.safeMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Person, //Eye
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Modalità Safe attiva",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerNavigationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    isCompleted: Boolean = false,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null)
                if (isCompleted) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Completato",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(16.dp)
                            .offset(x = (-4).dp, y = (-4).dp)
                    )
                }
            }
        },
        label = { Text(title) },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}