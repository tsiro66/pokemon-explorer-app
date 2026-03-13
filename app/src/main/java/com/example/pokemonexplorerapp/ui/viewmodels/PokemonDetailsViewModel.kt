package com.example.pokemonexplorerapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonexplorerapp.data.PokemonDetailsResponse
import com.example.pokemonexplorerapp.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonDetailsViewModel : ViewModel() {
    private val _pokemon = MutableStateFlow<PokemonDetailsResponse?>(null)
    val pokemon: StateFlow<PokemonDetailsResponse?> = _pokemon

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchPokemonDetails(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _pokemon.value = RetrofitInstance.api.getPokemonDetails(name.lowercase())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}