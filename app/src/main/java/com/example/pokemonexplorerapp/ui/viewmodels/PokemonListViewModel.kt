package com.example.pokemonexplorerapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pokemonexplorerapp.PokemonExplorerApp
import com.example.pokemonexplorerapp.data.PokemonRepository
import com.example.pokemonexplorerapp.ui.UiState
import com.example.pokemonexplorerapp.ui.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel(private val repository: PokemonRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<String>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<String>>> = _uiState

    val searchQuery = MutableStateFlow("")
    val currentPage = MutableStateFlow(0)

    private var currentType: String? = null

    fun fetchPokemon(typeName: String) {
        // Reuse the loaded list for the same type; reload only when the type changes.
        if (currentType == typeName && _uiState.value is UiState.Success) return
        currentType = typeName

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                _uiState.value = UiState.Success(repository.getPokemonByType(typeName))
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.toUserMessage())
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PokemonExplorerApp
                PokemonListViewModel(app.container.pokemonRepository)
            }
        }
    }
}
