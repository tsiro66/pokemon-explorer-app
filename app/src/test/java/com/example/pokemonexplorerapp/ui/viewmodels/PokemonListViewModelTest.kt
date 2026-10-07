package com.example.pokemonexplorerapp.ui.viewmodels

import com.example.pokemonexplorerapp.MainDispatcherRule
import com.example.pokemonexplorerapp.data.FakePokeApiService
import com.example.pokemonexplorerapp.data.PokemonRepository
import com.example.pokemonexplorerapp.ui.UiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class PokemonListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeApi = FakePokeApiService()
    private val repository = PokemonRepository(fakeApi)
    private val viewModel = PokemonListViewModel(repository)

    @Test
    fun `loads pokemon for a type`() {
        fakeApi.enqueueType("fire", listOf("charmander", "bulbasaur"))

        viewModel.fetchPokemon("fire")

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals(listOf("bulbasaur", "charmander"), (state as UiState.Success).data)
    }

    @Test
    fun `emits error state on network failure`() {
        fakeApi.enqueueType("fire", listOf("charmander"))
        fakeApi.typeError = IOException("offline")

        viewModel.fetchPokemon("fire")

        assertTrue(viewModel.uiState.value is UiState.Error)
    }

    @Test
    fun `does not refetch the same type when already loaded`() {
        fakeApi.enqueueType("fire", listOf("charmander"))

        viewModel.fetchPokemon("fire")
        viewModel.fetchPokemon("fire")

        assertEquals(1, fakeApi.getTypeCallCount)
    }

    @Test
    fun `refetches when the type changes`() {
        fakeApi.enqueueType("fire", listOf("charmander"))
        fakeApi.enqueueType("water", listOf("squirtle"))

        viewModel.fetchPokemon("fire")
        viewModel.fetchPokemon("water")

        assertEquals(2, fakeApi.getTypeCallCount)
    }

    @Test
    fun `retry after error hits the repository again`() {
        fakeApi.enqueueType("fire", listOf("charmander"))
        fakeApi.typeError = IOException("offline")

        viewModel.fetchPokemon("fire")
        fakeApi.typeError = null
        viewModel.fetchPokemon("fire")

        assertTrue(viewModel.uiState.value is UiState.Success)
        assertEquals(2, fakeApi.getTypeCallCount)
    }

    @Test
    fun `search resets the current page`() {
        fakeApi.enqueueType("fire", List(30) { "pokemon-$it" })

        viewModel.fetchPokemon("fire")
        viewModel.nextPage(3)
        assertEquals(1, viewModel.currentPage.value)

        viewModel.onSearchChanged("pokemon")

        assertEquals(0, viewModel.currentPage.value)
    }
}
