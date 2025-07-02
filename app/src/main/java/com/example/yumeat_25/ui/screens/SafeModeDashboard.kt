package com.example.yumeat_25.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yumeat_25.data.Food

@Composable
fun SafeModeDashboard(
    mealsList: List<Pair<String, List<Food>>>,
    onMealDetailsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp, horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ciao, oggi conta come ti senti non cosa mangi!",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(13.dp))
            Text(
                text = "Stai seguendo un buon ritmo,\ncontinua ad ascoltare il tuo corpo! 😁",
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(32.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MacroQualitative("Carboidrati", "Adeguato", Color(0xFF59AE4A), Modifier.weight(1f))
                MacroQualitative("Proteine", "Aggiungine un po", Color(0xFFE7B600), Modifier.weight(1f))
                MacroQualitative("Grassi", "Ok", Color(0xFF59AE4A), Modifier.weight(1f))
            }
        }
    }

    Spacer(Modifier.height(30.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp)
        ) {
            Text(
                text = "Cosa hai mangiato",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp, bottom = 6.dp)
            )
            mealsList.forEach { (mealName, foods) ->
                Column(Modifier.padding(vertical = 6.dp, horizontal = 8.dp)) {
                    Text(
                        text = mealName,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                    if (foods.isEmpty()) {
                        Text(
                            text = "Nessun alimento aggiunto",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    } else {
                        foods.forEach { food ->
                            Text(
                                text = food.name,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(start = 16.dp, top = 2.dp, bottom = 2.dp)
                            )
                        }
                    }
                }
            }
            Text(
                text = "Dettagli",
                color = Color.Gray,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp)
                    .clickable { onMealDetailsClick() }
            )
        }
    }
}

@Composable
fun MacroQualitative(label: String, status: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            status,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = color
        )
    }
}