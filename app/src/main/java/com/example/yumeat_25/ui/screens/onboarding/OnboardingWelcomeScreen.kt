package com.example.yumeat_25.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.yumeat_25.R

data class CarouselItem(
    val imageRes: Int, // Image resource id
    val title: String,
    val description: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingWelcomeScreen(
    onNext: () -> Unit
) {
    var showLoginDialog by remember { mutableStateOf(false) }
    var showRegisterDialog by remember { mutableStateOf(false) }

    // Carousel items
    val items = listOf(
        CarouselItem(
            imageRes = R.drawable.img1,
            title = "Benvenuto in YUMeat",
            description = "L’app che ti aiuta a costruire abitudini alimentari sane e sostenibili, \n" +
                    "senza stress da calorie o numeri"
        ),
        CarouselItem(
            imageRes = R.drawable.img2,
            title = "Traccia i tuoi pasti con semplicità",
            description = "Scegli se inserire gli alimenti manualmente o scatta una foto. \n" +
                    "YUMeat si adatta a te, non il contrario"
        ),
        CarouselItem(
            imageRes = R.drawable.img3,
            title = "Non solo numeri: supporto, sempre",
            description = "Ricevi messagi motivazionali \n" +
                    "e consigli empatici.\n" +
                    "La tua salute parte anche dalla mente"
        ),
        CarouselItem(
            imageRes = R.drawable.img4,
            title = "Ispirazione ogni giorno",
            description = "Scopri ricette sane basate sulle tue preferenze e obiettivi. \n" +
                    "Perchè mangiare bene può essere anche divertente"
        )
    )

    val pagerState = rememberPagerState(pageCount = { items.size })
    val scope = rememberCoroutineScope()

    // Auto-scroll logic
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % items.size
            scope.launch {
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --- TOP BAR: LOGO & APP NAME ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo YUMeat",
                    modifier = Modifier
                        .size(60.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "YUMeat",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // --- CAROUSEL ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = items[page].imageRes),
                            contentDescription = items[page].title,
                            modifier = Modifier
                                .size(300.dp)
                                .padding(bottom = 32.dp)
                        )

                        Text(
                            text = items[page].title,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Text(
                            text = items[page].description,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // --- PAGE INDICATOR DOTS ---
            Row(
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .height(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(items.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    }
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .size(8.dp)
                            .background(
                                color = color,
                                shape = CircleShape
                            )
                    )
                }
            }

            // --- BUTTONS ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { showRegisterDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1F5F5B),
                        contentColor = Color(0xFFFFFFFF)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                ) {
                    Text("Inizia con YUMeat", fontSize = 16.sp)
                }

                TextButton(
                    onClick = { showLoginDialog = true },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color(0xFFEAEAEA),
                        contentColor = Color(0xFF0694F4)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Ho già un account", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(15.dp))
            }
        }

        // --- LOGIN/REGISTER POPUP OVERLAY ---
        AnimatedVisibility(
            visible = showLoginDialog || showRegisterDialog,
            enter = slideInVertically(
                animationSpec = tween(
                    durationMillis = 400,
                    easing = FastOutSlowInEasing
                ),
                initialOffsetY = { fullHeight -> fullHeight }
            ),
            exit = slideOutVertically(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                ),
                targetOffsetY = { fullHeight -> fullHeight }
            )
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .zIndex(2f)
                    .clickable(
                        onClick = {
                            // Dismiss on outside click
                            if (showLoginDialog) showLoginDialog = false
                            if (showRegisterDialog) showRegisterDialog = false
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                if (showLoginDialog) {
                    AuthBottomSheet(
                        isLogin = true,
                        onDismiss = { showLoginDialog = false },
                        onSubmit = onNext
                    )
                }
                if (showRegisterDialog) {
                    AuthBottomSheet(
                        isLogin = false,
                        onDismiss = { showRegisterDialog = false },
                        onSubmit = onNext
                    )
                }
            }
        }
    }
}

@Composable
fun AuthBottomSheet(
    isLogin: Boolean,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    // State for the form
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    // Custom shadow only on the top of the popup
    val topShadow = Modifier
        .fillMaxWidth()
        .height(12.dp)
        .shadow(
            elevation = 12.dp,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            clip = false,
            ambientColor = Color.Black.copy(alpha = 0.12f),
            spotColor = Color.Black.copy(alpha = 0.12f)
        )

    Box(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 520.dp) // Make the popup higher
            .wrapContentHeight()
            .zIndex(10f)
    ) {
        // Only shadow at the top
        Box(
            modifier = topShadow.align(Alignment.TopCenter)
        )

        // Main card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color(0xFFF1F1F1))
                .padding(horizontal = 24.dp, vertical = 40.dp)
                .align(Alignment.BottomCenter)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (isLogin) "Bentornato" else "Registrazione",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Text(
                text = if (isLogin) "Inserisci i tuoi dati qui sotto" else "Compila i campi per la registrazione",
                fontSize = 16.sp,
                color = Color(0xFF606060),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp, bottom = 28.dp)
            )

            if (!isLogin) {
                // Borderless input with centered placeholder and value
                BasicTextFieldWithBackground(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Nome",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
            }

            BasicTextFieldWithBackground(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )

            BasicTextFieldWithBackground(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                isPassword = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp)
            )

            Button(
                onClick = onSubmit,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1F5F5B),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(if (isLogin) "Accedi" else "Registrati", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            }

            if (isLogin) {
                TextButton(
                    onClick = { /* handle forgot password */ },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Hai dimenticato la password?",
                        color = Color(0xFF222222),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            // Divider with "O accedi con"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, bottom = 12.dp)
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text(
                    "  O accedi con  ",
                    color = Color(0xFF888888),
                    fontSize = 14.sp
                )
                Divider(modifier = Modifier.weight(1f))
            }

            // Social login buttons (borderless)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /* handle Google login */ },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    elevation = null,
                    border = null,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .padding(end = 8.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.google),
                        contentDescription = null,
                        modifier = Modifier.requiredSize(40.dp)
                    )
                    Text("Google", color = Color.Black)
                }
                Button(
                    onClick = { /* handle Facebook login */ },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF1877F3)
                    ),
                    elevation = null,
                    border = null,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .padding(start = 8.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.facebook),
                        contentDescription = null,
                        modifier = Modifier.requiredSize(40.dp)
                    )
                    Text("Facebook", color = Color(0xFF1877F3))
                }
            }
        }
    }
}

@Composable
fun BasicTextFieldWithBackground(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .height(56.dp)
            .padding(horizontal = 0.dp, vertical = 0.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
        androidx.compose.foundation.text.BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 0.dp),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Start // Center text in the field
            ),
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color(0xFF888888),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}