package com.example.pokemonexplorerapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pokemonexplorerapp.PokemonExplorerApp
import com.example.pokemonexplorerapp.data.CaptureStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CaptureViewModel(private val store: CaptureStore) : ViewModel() {

    val capturedPokemon = store.capturedPokemon.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptySet()
    )

    fun toggleCapture(pokemonName: String) {
        viewModelScope.launch {
            store.toggleCapture(pokemonName)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PokemonExplorerApp
                CaptureViewModel(app.container.captureStore)
            }
        }
    }
}
