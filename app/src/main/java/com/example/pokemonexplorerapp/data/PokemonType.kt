package com.example.pokemonexplorerapp.data

import androidx.compose.ui.graphics.Color

data class PokemonType(
    val name: String,
    val color: Color
)

val pokemonTypes = listOf(
    PokemonType("Fire",     Color(0xFFFF5231)),
    PokemonType("Water",    Color(0xFF3692DC)),
    PokemonType("Grass",    Color(0xFF38BF4B)),
    PokemonType("Electric", Color(0xFFFBD100)),
    PokemonType("Dragon",   Color(0xFF4F60E2)),
    PokemonType("Psychic",  Color(0xFFF85888)),
    PokemonType("Ghost",    Color(0xFF5269AC)),
    PokemonType("Dark",     Color(0xFF887c9c)),
    PokemonType("Steel",    Color(0xFF5A8EA2)),
    PokemonType("Fairy",    Color(0xFFFB89EB)),
)