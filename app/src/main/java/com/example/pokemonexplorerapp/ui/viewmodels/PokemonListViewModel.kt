package com.example.pokemonexplorerapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonexplorerapp.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel : ViewModel() {
    private val _allPokemon = MutableStateFlow<List<String>>(emptyList())
    val allPokemon: StateFlow<List<String>> = _allPokemon

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // These persist across navigation
    val searchQuery = MutableStateFlow("")
    val currentPage = MutableStateFlow(0)

    fun fetchPokemon(typeName: String) {
        // Don't re-fetch if we already have data for this type
        if (_allPokemon.value.isNotEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getTypeDetails(typeName)
                _allPokemon.value = response.pokemon.map { it.pokemon.name }.sorted()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onSearchChanged(query: String) {
        searchQuery.value = query
        currentPage.value = 0
    }

    fun nextPage(totalPages: Int) {
        if (currentPage.value < totalPages - 1) {
            currentPage.value++
        }
    }

    fun prevPage() {
        if (currentPage.value > 0) {
            currentPage.value--
        }
    }
}