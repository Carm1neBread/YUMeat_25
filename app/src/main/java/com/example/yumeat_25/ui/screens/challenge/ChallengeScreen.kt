package com.example.yumeat_25.ui.screens.challenge

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun ChallengeScreen(
    navController: NavController
) {
    // Categorie challenge
    val categories = listOf("Giornaliere", "Settimanali", "Mensili")
    var selectedCategory by remember { mutableStateOf(0) }

    // Inizializzazione challenge giornaliere di default
    val dailyChallenges = listOf(
        ChallengeUi(
            title = "Equilibrio del giorno",
            description = "Hai mangiato di almeno 3 gruppi alimentari.",
            progress = "2/3"
        ),
        ChallengeUi(
            title = "Giornata presente",
            description = "Completa il giorno senza guardare le calorie.",
            progress = "1/1"
        ),
        ChallengeUi(
            title = "Ascolto attivo",
            description = "Inserisci un pasto dopo averci pensato su.",
            progress = "0/1"
        ),
        ChallengeUi(
            title = "Sospiro di sollievo",
            description = "Registri come ti senti e ti prendi una pausa.",
            progress = "0/1"
        ),
    )

    val challengeLists = listOf(
        dailyChallenges,
    )

    // Flag per capire quali delle challenge è in primo piano
    var expandedIndex by remember { mutableStateOf<Int?>(null) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Indietro", tint = Color.Black)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Challenge",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1.5f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            categories.forEachIndexed { idx, cat ->
                Text(
                    text = cat,
                    fontSize = 16.sp,
                    fontWeight = if (selectedCategory == idx) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedCategory == idx) Color.Black else Color.Gray,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .clickable { selectedCategory = idx; expandedIndex = null }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val challenges = challengeLists[selectedCategory]
        Column {
            challenges.forEachIndexed { idx, challenge ->
                ChallengeCard(
                    challenge = challenge,
                    expanded = expandedIndex == idx,
                    onClick = {
                        expandedIndex = if (expandedIndex == idx) null else idx
                    }
                )
                Spacer(modifier = Modifier.height(13.dp))
            }
        }
    }
}

data class ChallengeUi(
    val title: String,
    val description: String,
    val progress: String
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ChallengeCard(
    challenge: ChallengeUi,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF295B4F), Color(0xFFC9C9C9)),
        startX = 0f,
        endX = 600f
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(gradient)
            .clickable { onClick() }
            .padding(vertical = if (expanded) 18.dp else 5.dp, horizontal = 18.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = challenge.title,
                    fontWeight = if (expanded) FontWeight.Bold else FontWeight.Normal,
                    fontSize = if (expanded) 20.sp else 18.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(12.dp))
                ChallengeProgressCircle(progressString = challenge.progress)
            }
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
            ) {
                Text(
                    text = challenge.description,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(top = 10.dp, end = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ChallengeProgressCircle(progressString: String) {
    val (current, total) = remember(progressString) {
        val parts = progressString.split('/')
        val current = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val total = parts.getOrNull(1)?.toIntOrNull() ?: 1
        current.coerceAtMost(total) to total.coerceAtLeast(1)
    }
    val progress = current.toFloat() / total.toFloat()

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .drawBehind {
                drawCircle(
                    color = Color.White,
                    radius = size.minDimension / 2f
                )
                drawArc(
                    color = Color(0xFF000000),
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$current/$total",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}