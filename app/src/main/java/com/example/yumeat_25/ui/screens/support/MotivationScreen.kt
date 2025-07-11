package com.example.yumeat_25.ui.screens.support

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun MotivationScreen(
    navController: NavController
) {
    val quotes = listOf(
        MotivationQuote(
            "Autostima",
            listOf(
                "Non devi piacerti per meritare amore",
                "Sei già degno così"
            )
        ),
        MotivationQuote(
            "Rapporto con il cibo",
            listOf("Il tuo valore non cambia", "con ciò che metti nel piatto")
        ),
        MotivationQuote(
            "Mente",
            listOf("La tua mente corre, ma tu puoi", "camminare. Un respiro alla volta")
        ),
        MotivationQuote(
            "Corpo",
            listOf("Cambiare come guardi il tuo corpo", "vale più che cambiarlo davvero")
        )
    )

    val visibleList = remember { mutableStateListOf(false, false, false, false) }
    LaunchedEffect(Unit) {
        for (i in visibleList.indices) {
            delay(800L)
            visibleList[i] = true
        }
    }

    // Custom TopBar as a Card with padding from top
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp, start = 0.dp, end = 0.dp, bottom = 0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Motivazione",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
                }
            }
            Text(
                text = "Quando ti serve una parola buona,\nqui ne trovi una.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for ((i, quote) in quotes.withIndex()) {
                AnimatedVisibility(
                    visible = visibleList[i],
                    enter = fadeIn(animationSpec = tween(durationMillis = 600)) +
                            slideInVertically(
                                animationSpec = tween(durationMillis = 600),
                                initialOffsetY = { it / 4 }
                            ),
                    exit = fadeOut()
                ) {
                    MotivationQuoteCard(
                        label = quote.label,
                        lines = quote.lines,
                        modifier = Modifier.padding(vertical = 13.dp)
                    )
                }
            }
        }
    }
}

data class MotivationQuote(
    val label: String,
    val lines: List<String>
)

@Composable
fun MotivationQuoteCard(
    label: String,
    lines: List<String>,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF295B4F), Color(0xFFC9C9C9)),
        startX = 0f,
        endX = 600f
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // Rotated label on the left, with gap above card
        Text(
            text = label,
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 5.dp, top = 8.dp)
        )

        // Quote card with gradient and rounded corners
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .background(gradient, shape = RoundedCornerShape(28.dp))
                .padding(vertical = 22.dp, horizontal = 24.dp)
        ) {
            Column {
                Text(
                    text = lines.joinToString("\n"),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White,
                    lineHeight = 22.sp
                )
            }
        }
    }
}