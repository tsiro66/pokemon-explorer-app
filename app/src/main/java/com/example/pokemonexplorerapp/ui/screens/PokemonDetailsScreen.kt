package com.example.pokemonexplorerapp.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.pokemonexplorerapp.ui.viewmodels.PokemonDetailsViewModel
import com.example.pokemonexplorerapp.ui.components.MyCard
import com.example.pokemonexplorerapp.ui.components.MyError
import com.example.pokemonexplorerapp.ui.viewmodels.CaptureViewModel
import kotlinx.coroutines.delay

@Composable
fun PokemonDetailsScreen(
    pokemonName: String,
    typeColor: Color,
    isPageActive: Boolean = true,
    viewModel: PokemonDetailsViewModel = viewModel(),
    captureViewModel: CaptureViewModel = viewModel(),
) {
    val pokemon by viewModel.pokemon.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val capturedSet by captureViewModel.capturedPokemon.collectAsState()
    val isCaptured = pokemonName in capturedSet

    LaunchedEffect(pokemonName) {
        viewModel.fetchPokemonDetails(pokemonName)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))
            .padding(horizontal = 16.dp)
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    color = typeColor,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            errorMessage != null -> {
                MyError(
                    message = errorMessage ?: "Unknown Error",
                    onRetry = { viewModel.fetchPokemonDetails(pokemonName) }
                )
            }

            pokemon != null -> {
                val p = pokemon!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 88.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // NAME CARD
                    MyCard(backgroundColor = typeColor) {
                        Text(
                            text = p.name.uppercase(),
                            fontSize = getAdaptiveFontSize(p.name),
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // SPRITE
                    AsyncImage(
                        model = p.sprites.other?.officialArtwork?.frontDefault
                            ?: p.sprites.frontDefault,
                        contentDescription = p.name,
                        modifier = Modifier
                            .size(220.dp)
                            .padding(8.dp)
                    )

                    // CAPTURE TOGGLE
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isCaptured) "CAPTURED" else "NOT CAPTURED",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                        Switch(
                            checked = isCaptured,
                            onCheckedChange = { captureViewModel.toggleCapture(pokemonName) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = typeColor,
                                uncheckedThumbColor = Color.Black,
                                uncheckedTrackColor = Color.LightGray,
                                uncheckedBorderColor = Color.Black,
                                checkedBorderColor = Color.Black
                            )
                        )
                    }

                    // STATS
                    MyCard(
                        backgroundColor = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth()
                        ) {
                            Text("BASIC STATS", fontWeight = FontWeight.Black, fontSize = 20.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            StatRow("HP",  p.stats.find { it.stat.name == "hp" }?.baseStat ?: 0, typeColor, p.name, isPageActive)
                            StatRow("ATK", p.stats.find { it.stat.name == "attack" }?.baseStat ?: 0, typeColor, p.name, isPageActive)
                            StatRow("DEF", p.stats.find { it.stat.name == "defense" }?.baseStat ?: 0, typeColor, p.name, isPageActive)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // HEIGHT & WEIGHT
                    Row(modifier = Modifier.fillMaxWidth()) {
                        MyCard(backgroundColor = Color.White, modifier = Modifier.weight(1f)) {
                            Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("HEIGHT", fontWeight = FontWeight.Black, fontSize = 15.sp)
                                Text("${p.height / 10.0} m", fontWeight = FontWeight.Normal, fontSize = 20.sp)
                            }
                        }
                        Spacer(Modifier.width(16.dp))
                        MyCard(backgroundColor = Color.White, modifier = Modifier.weight(1f)) {
                            Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("WEIGHT", fontWeight = FontWeight.Black, fontSize = 15.sp)
                                Text("${p.weight / 10.0} kg", fontWeight = FontWeight.Normal, fontSize = 20.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // TRAITS
                    MyCard(backgroundColor = Color.White, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text("TRAITS", fontWeight = FontWeight.Black, fontSize = 20.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("TYPES", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    p.types.forEach { typeSlot ->
                                        Text(
                                            text = typeSlot.type.name.uppercase(),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("ABILITIES", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    p.abilities.forEach { abilitySlot ->
                                        val hiddenText = if (abilitySlot.isHidden) " (H)" else ""
                                        Text(
                                            text = "• ${abilitySlot.ability.name.replace("-", " ")}$hiddenText",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatRow(
    label: String,
    value: Int,
    accentColor: Color = Color.Black,
    pokemonName: String,
    isPageActive: Boolean
) {
    // 1. Every time the pokemonName changes, this boolean resets to false.
    // This ensures that clicking from a list ALWAYS resets the bar to 0.
    var startAnimation by remember(pokemonName) { mutableStateOf(false) }

    val targetPercentage = (value / 255f).coerceIn(0f, 1f)

    // 2. Animate from 0 to target ONLY when startAnimation becomes true
    val animatedProgress by animateFloatAsState(
        targetValue = if (startAnimation) targetPercentage else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "StatBarAnimation"
    )

    // 3. Wait for the page to be active,
    // then wait 50ms to let the UI draw once at 0% before starting.
    LaunchedEffect(isPageActive, pokemonName) {
        if (isPageActive) {
            delay(50)
            startAnimation = true
        } else {
            // Reset when swiping away so it's ready for next time
            startAnimation = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(value.toString(), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .border(BorderStroke(width = 2.dp, color = Color.Black))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .background(accentColor)
            )
        }
    }
}
@Composable
fun getAdaptiveFontSize(name: String): TextUnit {
    return when {
//        name.length >= 22 -> 8.sp
        name.length >= 18 -> 10.sp
        name.length >= 14 -> 12.sp
        name.length >= 10 -> 16.sp
        name.length >= 8 -> 18.sp
        else -> 28.sp
    }
}