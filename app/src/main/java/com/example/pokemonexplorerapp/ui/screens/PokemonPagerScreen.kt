package com.example.pokemonexplorerapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.pokemonexplorerapp.ui.viewmodels.PokemonDetailsViewModel

@Composable
fun PokemonPagerScreen(
    initialPokemonName: String,
    pokemonList: List<String>,
    typeColor: Color,
    onBack: () -> Unit
) {
    // Find where we are in the list
    val initialIndex = pokemonList.indexOf(initialPokemonName).coerceAtLeast(0)

    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { pokemonList.size }
    )

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        beyondViewportPageCount = 1 // Keeps the next Pokemon loaded in memory
    ) { pageIndex ->
        // Use the Pokemon name as a unique key for the ViewModel instance
        val detailViewModel: PokemonDetailsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
            key = pokemonList[pageIndex]
        )

        // Reuse your existing Detail Screen logic
        PokemonDetailsScreen(
            pokemonName = pokemonList[pageIndex],
            typeColor = typeColor,
            viewModel = detailViewModel,
            onBack = onBack
        )
    }
}