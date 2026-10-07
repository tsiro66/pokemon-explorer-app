package com.example.pokemonexplorerapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pokemonexplorerapp.PokemonExplorerApp
import com.example.pokemonexplorerapp.data.PokemonDetailsResponse
import com.example.pokemonexplorerapp.data.PokemonRepository
import com.example.pokemonexplorerapp.ui.UiState
import com.example.pokemonexplorerapp.ui.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(private val repository: PokemonRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<PokemonDetailsResponse>>(UiState.Loading)
    val uiState: StateFlow<UiState<PokemonDetailsResponse>> = _uiState

    fun fetchPokemonDetails(name: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                _uiState.value = UiState.Success(repository.getPokemonDetails(name))
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.toUserMessage())
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PokemonExplorerApp
                PokemonDetailsViewModel(app.container.pokemonRepository)
            }
        }
    }
}
