package com.example.pokemonexplorerapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "captured_pokemon")

class CaptureStore(private val context: Context) {

    private val CAPTURED_KEY = stringSetPreferencesKey("captured")

    val capturedPokemon: Flow<Set<String>> = context.dataStore.data
        .map { prefs -> prefs[CAPTURED_KEY] ?: emptySet() }

    suspend fun toggleCapture(pokemonName: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[CAPTURED_KEY] ?: emptySet()
            prefs[CAPTURED_KEY] = if (pokemonName in current) {
                current - pokemonName
            } else {
                current + pokemonName
            }
        }
    }
}