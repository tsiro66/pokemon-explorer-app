package com.example.pokemonexplorerapp.data

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import java.io.IOException

class PokemonRepositoryTest {

    @Test
    fun `type list is fetched once and cached by normalized key`() = runTest {
        val fake = FakePokeApiService().apply {
            enqueueType("fire", listOf("charmander", "bulbasaur", "squirtle"))
        }
        val repository = PokemonRepository(fake)

        val first = repository.getPokemonByType("Fire")
        val second = repository.getPokemonByType("fire")

        assertEquals(listOf("bulbasaur", "charmander", "squirtle"), first)
        assertEquals(first, second)
        assertEquals(1, fake.getTypeCallCount)
    }

    @Test
    fun `details are fetched once and cached by normalized key`() = runTest {
        val fake = FakePokeApiService().apply { enqueuePokemon("pikachu") }
        val repository = PokemonRepository(fake)

        val first = repository.getPokemonDetails("Pikachu")
        val second = repository.getPokemonDetails("pikachu")

        assertEquals("pikachu", first.name)
        assertSame(first, second)
        assertEquals(1, fake.getDetailsCallCount)
    }

    @Test
    fun `network errors propagate to the caller`() = runTest {
        val fake = FakePokeApiService().apply {
            enqueueType("fire", listOf("charmander"))
            typeError = IOException("offline")
        }
        val repository = PokemonRepository(fake)

        val error = runCatching { repository.getPokemonByType("fire") }.exceptionOrNull()

        assertEquals(IOException::class.java, error?.javaClass)
    }
}
