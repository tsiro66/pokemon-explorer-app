package com.example.pokemonexplorerapp.data

import com.example.pokemonexplorerapp.data.network.RetrofitInstance

class PokemonRepository {
    suspend fun getPokemonByType(type: String): List<String> {
        val response = RetrofitInstance.api.getTypeDetails(type)
        return response.pokemon.map { it.pokemon.name }.sorted()
    }
}