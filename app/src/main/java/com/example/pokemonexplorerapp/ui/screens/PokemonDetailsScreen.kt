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
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.pokemonexplorerapp.ui.viewmodels.PokemonDetailsViewModel
import com.example.pokemonexplorerapp.ui.components.MyButton
import com.example.pokemonexplorerapp.ui.components.MyCard

@Composable
fun PokemonDetailsScreen(
    pokemonName: String,
    typeColor: Color,
    viewModel: PokemonDetailsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit
) {
    val pokemon by viewModel.pokemon.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var columnFontSize by remember { mutableStateOf(32.sp) }

    LaunchedEffect(pokemonName) {
        viewModel.fetchPokemonDetails(pokemonName)
    }

    // Outer box so we can pin the back button to the bottom
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = typeColor,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            pokemon?.let { p ->
                // Scrollable content above the back button
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp) // leave room for back button
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // IMAGE first
                    AsyncImage(
                        model = p.sprites.other?.officialArtwork?.frontDefault
                            ?: p.sprites.frontDefault,
                        contentDescription = p.name,
                        modifier = Modifier
                            .size(220.dp)
                            .padding(8.dp)
                    )

                    // NAME below image
                    MyCard(backgroundColor = typeColor) {
                        Text(
                            text = p.name.uppercase(),
                            fontSize = getAdaptiveFontSize(p.name),
                            fontWeight = FontWeight.Black,
                            modifier = Modifier
                                .padding(horizontal = 32.dp, vertical = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

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
                            Spacer(modifier = Modifier.height(8.dp))
                            StatRow("HP",  p.stats.find { it.stat.name == "hp" }?.baseStat ?: 0, typeColor)
                            StatRow("ATK", p.stats.find { it.stat.name == "attack" }?.baseStat ?: 0, typeColor)
                            StatRow("DEF", p.stats.find { it.stat.name == "defense" }?.baseStat ?: 0, typeColor)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    // PHYSICAL TRAITS (Height & Weight)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        MyCard(backgroundColor = Color.White, modifier = Modifier.weight(1f)) {
                            Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("HEIGHT", fontWeight = FontWeight.Black, fontSize = 15.sp)
                                // Height is in decimeters, so / 10.0 gives meters
                                Text("${p.height / 10.0} m", fontWeight = FontWeight.Normal, fontSize = 20.sp)
                            }
                        }
                        Spacer(Modifier.width(16.dp))
                        MyCard(backgroundColor = Color.White, modifier = Modifier.weight(1f)) {
                            Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("WEIGHT", fontWeight = FontWeight.Black, fontSize = 15.sp)
                                // Weight is in hectograms, so / 10.0 gives kilograms
                                Text("${p.weight / 10.0} kg", fontWeight = FontWeight.Normal, fontSize = 20.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // TYPES & ABILITIES
                    MyCard(backgroundColor = Color.White, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text("TRAITS", fontWeight = FontWeight.Black, fontSize = 20.sp)

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                // Column 1: TYPES
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

                                // Column 2: ABILITIES
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

                // BACK BUTTON pinned to bottom
                MyButton(
                    text = "← Back to List",
                    onClick = onBack,
                    backgroundColor = typeColor,
                    height = 56.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: Int, accentColor: Color = Color.Black) {
    // 1. Create a boolean state to trigger the animation
    var animationPlayed by remember { mutableStateOf(false) }

    // 2. Calculate the target percentage (0.0 to 1.0)
    val targetPercentage = (value / 255f).coerceIn(0f, 1f)

    // 3. Define the animated float
    // If animationPlayed is false, width is 0. If true, it animates to target.
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) targetPercentage else 0f,
        animationSpec = tween(
            durationMillis = 1000, // 1 second duration
            delayMillis = 100,     // Slight delay for better feel
            easing = FastOutSlowInEasing
        ),
        label = "StatBarAnimation"
    )

    // 4. Trigger the animation when the composable is first launched
    LaunchedEffect(Unit) {
        animationPlayed = true
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

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .border(BorderStroke(width = 2.dp, color = Color.Black))
        ) {
            // 5. Use 'animatedProgress' instead of 'percentage'
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
        name.length > 22 -> 10.sp
        name.length > 18 -> 12.sp
        name.length > 14 -> 16.sp
        name.length > 10 -> 20.sp
        name.length > 8 -> 24.sp
        else -> 28.sp
    }
}