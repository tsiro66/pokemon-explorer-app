package com.example.pokemonexplorerapp.data

import com.example.pokemonexplorerapp.data.network.PokeApiService

/**
 * In-memory fake of [PokeApiService] for unit tests. Counts calls so tests
 * can assert caching behaviour.
 */
class FakePokeApiService : PokeApiService {

    var getTypeCallCount = 0
        private set
    var getDetailsCallCount = 0
        private set

    var typeError: Exception? = null
    var detailsError: Exception? = null

    private val types = mutableMapOf<String, TypeResponse>()
    private val details = mutableMapOf<String, PokemonDetailsResponse>()

    fun enqueueType(name: String, pokemonNames: List<String>) {
        types[name.lowercase()] = TypeResponse(
            id = types.size + 1,
            name = name,
            pokemon = pokemonNames.mapIndexed { index, pokemonName ->
                TypePokemonSlot(NamedApiResource(pokemonName, "poke/$pokemonName"), index)
            }
        )
    }

    fun enqueuePokemon(name: String) {
        details[name.lowercase()] = PokemonDetailsResponse(
            id = details.size + 1,
            name = name,
            height = 17,
            weight = 300,
            abilities = emptyList(),
            stats = emptyList(),
            types = emptyList(),
            sprites = PokemonSprites(frontDefault = "sprite/$name")
        )
    }

    override suspend fun getTypeDetails(name: String): TypeResponse {
        getTypeCallCount++
        typeError?.let { throw it }
        return types[name.lowercase()] ?: error("No fake data for type '$name'")
    }

    override suspend fun getPokemonDetails(name: String): PokemonDetailsResponse {
        getDetailsCallCount++
        detailsError?.let { throw it }
        return details[name.lowercase()] ?: error("No fake data for pokemon '$name'")
    }
}
