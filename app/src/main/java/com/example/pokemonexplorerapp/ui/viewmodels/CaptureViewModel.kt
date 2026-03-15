package com.example.pokemonexplorerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonexplorerapp.data.CaptureStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CaptureViewModel(application: Application) : AndroidViewModel(application) {

    private val store = CaptureStore(application)

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
}