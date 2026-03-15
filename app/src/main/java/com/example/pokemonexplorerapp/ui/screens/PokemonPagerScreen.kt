package com.example.pokemonexplorerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokemonexplorerapp.ui.components.MyButton
import com.example.pokemonexplorerapp.ui.components.MyCard
import com.example.pokemonexplorerapp.ui.components.MyError
import com.example.pokemonexplorerapp.ui.viewmodels.CaptureViewModel
import com.example.pokemonexplorerapp.ui.viewmodels.PokemonListViewModel

@Composable
fun PokemonPagerScreen(
    typeName: String,
    typeColor: Color,
    startIndex: Int,
    onBack: () -> Unit,
    listViewModel: PokemonListViewModel = viewModel(),
    captureViewModel: CaptureViewModel = viewModel()
) {
    LaunchedEffect(typeName) {
        if (listViewModel.allPokemon.value.isEmpty()) {
            listViewModel.fetchPokemon(typeName)
        }
    }

    val pokemonList by listViewModel.allPokemon.collectAsState()
    val isLoading by listViewModel.isLoading.collectAsState()
    val listErrorMessage by listViewModel.errorMessage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    color = typeColor,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            listErrorMessage != null -> {
                MyError(
                    message = listErrorMessage ?: "Failed to load Pokémon list",
                    onRetry = { listViewModel.fetchPokemon(typeName) }
                )
            }

            pokemonList.isNotEmpty() -> {
                val pagerState = rememberPagerState(
                    initialPage = startIndex.coerceIn(0, pokemonList.lastIndex),
                    pageCount = { pokemonList.size }
                )

                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(40.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MyCard(backgroundColor = typeColor) {
                            Text(
                                text = typeName.uppercase(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                            )
                        }
                        Text(
                            text = "${pagerState.currentPage + 1} / ${pokemonList.size}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f),
                        key = { page -> pokemonList[page] }
                    ) { page ->
                        PokemonDetailsScreen(
                            pokemonName = pokemonList[page],
                            typeColor = typeColor,
                            isPageActive = page == pagerState.currentPage,
                            viewModel = viewModel(key = pokemonList[page]),
                            captureViewModel = captureViewModel
                        )
                    }
                }
            }
        }

        MyButton(
            text = "← Back to List",
            onClick = onBack,
            backgroundColor = typeColor,
            height = 56.dp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}