package com.example.pokemonexplorerapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemonexplorerapp.ui.viewmodels.PokemonListViewModel
import com.example.pokemonexplorerapp.ui.components.MyButton

@Composable
fun PokemonListScreen(
    typeName: String,
    typeColor: Color,
    onPokemonSelected: (String) -> Unit,
    onBackPressed: () -> Unit,
    viewModel: PokemonListViewModel =  androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val allPokemon by viewModel.allPokemon.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val pageSize = 10

    val filteredPokemon = remember(allPokemon, searchQuery) {
        if (searchQuery.isBlank()) allPokemon
        else allPokemon.filter { it.contains(searchQuery.trim(), ignoreCase = true) }
    }

    val paginatedPokemon = remember(filteredPokemon, currentPage) {
        filteredPokemon.chunked(pageSize).getOrElse(currentPage) { emptyList() }
    }

    val totalPages = remember(filteredPokemon) {
        maxOf(1, (filteredPokemon.size + pageSize - 1) / pageSize)
    }

    LaunchedEffect(typeName) {
        viewModel.fetchPokemon(typeName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // Header
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = typeName,
//            fontFamily = PokemonFont,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
        )
        Text(
            text = "SELECT A POKÉMON",
            fontSize = 15.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                viewModel.onSearchChanged(it)
            },
            label = { Text("Search Pokémon", fontWeight = FontWeight.Bold) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            singleLine = true,
            shape = RectangleShape,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
                focusedBorderColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                focusedLabelColor = Color.Black,
            )
        )

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = typeColor)
                }
            }
            errorMessage != null -> {
                Text(
                    "Error: $errorMessage",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
            filteredPokemon.isEmpty() -> {
                Text(
                    "NO POKÉMON FOUND FOR \"${searchQuery.uppercase()}\"",
                    fontWeight = FontWeight.Bold
                )
            }
            else -> {
                // Pokemon list — takes up all available space, pushes pagination to bottom
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(paginatedPokemon) { pokemonName ->
                        MyButton(
                            text = pokemonName,
                            onClick = { onPokemonSelected(pokemonName) },
                            backgroundColor = typeColor,
                            textColor = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            height = 48.dp,
                            fontSize = 12.sp
                        )
                    }
                }

                // Pagination controls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyButton(
                        text = "← Prev",
                        onClick = { viewModel.prevPage() },
                        backgroundColor = typeColor,
                        modifier = Modifier.weight(1f),
                        height = 48.dp,
                        fontSize = 14.sp,
                        enabled = currentPage > 0
                    )

                    Text(
                        text = "${currentPage + 1} / $totalPages",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    MyButton(
                        text = "Next →",
                        onClick = { viewModel.nextPage(totalPages) },
                        backgroundColor = typeColor,
                        modifier = Modifier.weight(1f),
                        height = 48.dp,
                        fontSize = 14.sp,
                        enabled = currentPage < totalPages - 1
                    )
                }
                // Back button
                Spacer(modifier = Modifier.height(8.dp))
                MyButton(
                    text = "← Back",
                    onClick = onBackPressed,
                    backgroundColor = typeColor,
                    modifier = Modifier.fillMaxWidth(),
                    height = 52.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

