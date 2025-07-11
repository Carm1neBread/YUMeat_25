package com.example.yumeat_25.ui.screens.support.diary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yumeat_25.R
import com.example.yumeat_25.data.DiaryRepository
import com.example.yumeat_25.data.MoodEmoji
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WellnessDiaryScreen(
    navController: NavController,
    diaryRepository: DiaryRepository
) {
    val currentEntry by diaryRepository.currentEntry.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Usiamo una stringa normale
    var textContent by remember { mutableStateOf(currentEntry?.content ?: "") }
    var isFocused by remember { mutableStateOf(false) }

    // Aggiorna il contenuto quando cambia l'entry corrente
    LaunchedEffect(currentEntry) {
        currentEntry?.content?.let {
            textContent = it
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Diario",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                        Text(
                            "Spazio per me",
                            fontSize = 16.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    // Icona personalizzata dal drawable
                    IconButton(onClick = { navController.navigate("diary_history") }) {
                        Image(
                            painter = painterResource(id = R.drawable.diary), // Sostituisci con il nome del tuo file drawable
                            contentDescription = "Visualizza storia",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        },
        // Sistema Snackbar personalizzato e centrato
        snackbarHost = {
            Box(
                Modifier.fillMaxSize()
            ) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.Center),
                    snackbar = { data ->
                        Snackbar(
                            snackbarData = data,
                            containerColor = Color(0xFF295B4F),
                            contentColor = Color.White
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Sezione emoji
            Text(
                "Clicca sulle faccine che più ti rappresentano oggi!",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Row di emoji con selezione
            EmojiSelectionRow(
                selectedEmoji = currentEntry?.emoji ?: MoodEmoji.NEUTRAL,
                onEmojiSelected = {
                    diaryRepository.updateCurrentEmoji(it)
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Titolo campo di testo
            Text(
                "Come ti sei sentito rispetto al cibo oggi?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Start
            )

            // Card di input testo semplificata
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF3F3F3) // Stesso colore delle card in DiaryHistoryScreen
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Stesso valore di elevation
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Placeholder visibile solo quando il campo è vuoto e non ha focus
                    if (textContent.isEmpty() && !isFocused) {
                        Text(
                            text = "Scrivi qui la tua esperienza...",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }

                    // Campo di testo
                    BasicTextField(
                        value = textContent,
                        onValueChange = {
                            textContent = it
                            diaryRepository.updateCurrentContent(it)
                        },
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .onFocusChanged { isFocused = it.isFocused }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Pulsante "Aggiungi"
            Button(
                onClick = {
                    if (textContent.isNotBlank()) {
                        diaryRepository.saveCurrentEntry()
                        scope.launch {
                            snackbarHostState.showSnackbar("Diario aggiornato")
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Scrivi qualcosa prima di salvare")
                        }
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1F5F5B)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
                    .height(48.dp)
            ) {
                Text("Aggiungi", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun EmojiSelectionRow(
    selectedEmoji: MoodEmoji,
    onEmojiSelected: (MoodEmoji) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MoodEmoji.values().forEach { emoji ->
            EmojiOption(
                emoji = emoji,
                selected = emoji == selectedEmoji,
                onSelected = { onEmojiSelected(emoji) }
            )
        }
    }
}

@Composable
fun EmojiOption(
    emoji: MoodEmoji,
    selected: Boolean,
    onSelected: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Selezione (cerchio)
        Box(
            modifier = Modifier
                .size(25.dp)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = CircleShape
                )
                .padding(2.dp)
                .clip(CircleShape)
                .clickable { onSelected() },
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(15.dp)
                        .background(Color.Black, CircleShape)
                )
            }
        }

        // Emoji
        Text(
            text = emoji.unicode,
            fontSize = 36.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}