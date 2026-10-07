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
import com.example.pokemonexplorerapp.ui.UiState
import com.example.pokemonexplorerapp.ui.components.MyButton
import com.example.pokemonexplorerapp.ui.components.MyCard
import com.example.pokemonexplorerapp.ui.components.MyError
import com.example.pokemonexplorerapp.ui.viewmodels.CaptureViewModel
import com.example.pokemonexplorerapp.ui.viewmodels.PokemonDetailsViewModel
import com.example.pokemonexplorerapp.ui.viewmodels.PokemonListViewModel

@Composable
fun PokemonPagerScreen(
    typeName: String,
    typeColor: Color,
    startIndex: Int,
    onBack: () -> Unit,
    listViewModel: PokemonListViewModel = viewModel(factory = PokemonListViewModel.Factory),
    captureViewModel: CaptureViewModel = viewModel(factory = CaptureViewModel.Factory)
) {
    // Safe to call unconditionally: the ViewModel only refetches when the type changes,
    // and the repository serves repeated requests from its in-memory cache.
    LaunchedEffect(typeName) {
        listViewModel.fetchPokemon(typeName)
    }

    val uiState by listViewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))
    ) {
        when (val state = uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    color = typeColor,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is UiState.Error -> {
                MyError(
                    message = state.message,
                    onRetry = { listViewModel.fetchPokemon(typeName) }
                )
            }

            is UiState.Success -> {
                val pokemonList = state.data
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
                            viewModel = viewModel(
                                key = pokemonList[page],
                                factory = PokemonDetailsViewModel.Factory
                            ),
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
