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

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchPokemonDetails(name: String) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                _pokemon.value = RetrofitInstance.api.getPokemonDetails(name.lowercase())
            } catch (e: Exception) {
                _errorMessage.value = "Check your internet connection or try again later."
                _pokemon.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}