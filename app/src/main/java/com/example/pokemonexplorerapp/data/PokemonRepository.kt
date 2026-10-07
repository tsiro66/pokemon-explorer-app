package com.example.pokemonexplorerapp.data

import com.example.pokemonexplorerapp.data.network.PokeApiService
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Single source of Pokémon data. Wraps [PokeApiService] with an in-memory cache
 * so revisiting a type or Pokémon (e.g. reopening the pager) never refetches.
 * Cache access is guarded by a [Mutex] and keys are normalized to lowercase.
 */
class PokemonRepository(private val api: PokeApiService) {

    private val mutex = Mutex()
    private val typeListCache = mutableMapOf<String, List<String>>()
    private val detailsCache = mutableMapOf<String, PokemonDetailsResponse>()

    suspend fun getPokemonByType(type: String): List<String> = mutex.withLock {
        val key = type.lowercase()
        typeListCache.getOrPut(key) {
            api.getTypeDetails(key).pokemon.map { it.pokemon.name }.sorted()
        }
    }

    suspend fun getPokemonDetails(name: String): PokemonDetailsResponse = mutex.withLock {
        val key = name.lowercase()
        detailsCache.getOrPut(key) { api.getPokemonDetails(key) }
    }
}
