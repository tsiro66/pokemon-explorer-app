package com.example.pokemonexplorerapp.ui

import retrofit2.HttpException
import java.io.IOException

/**
 * Single source of truth for screen state. Makes impossible states
 * (e.g. "loading" while data is present) unrepresentable.
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Error(val message: String) : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
}

/**
 * Maps any exception to a user-facing message. [HttpException] must be matched
 * before [IOException] since in Retrofit 3 the former extends the latter.
 */
fun Throwable.toUserMessage(): String = when (this) {
    is HttpException -> "Server error (code ${code()}). Please try again later."
    is IOException -> "Check your internet connection and try again."
    else -> "Something went wrong. Please try again."
}
