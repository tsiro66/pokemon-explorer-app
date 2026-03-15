package com.example.pokemonexplorerapp.data

data class TypeResponse(
    val id: Int,
    val name: String,
    val pokemon: List<TypePokemonSlot>
)

data class TypePokemonSlot(
    val pokemon: NamedApiResource,
    val slot: Int
)