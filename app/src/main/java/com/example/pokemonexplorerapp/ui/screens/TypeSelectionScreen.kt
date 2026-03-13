package com.example.pokemonexplorerapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemonexplorerapp.data.PokemonType
import com.example.pokemonexplorerapp.data.pokemonTypes
import com.example.pokemonexplorerapp.ui.components.MyButton
import com.example.pokemonexplorerapp.ui.theme.PokemonFont


@Composable
fun TypeSelectionScreen(onTypeSelected: (String) -> Unit) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "PoKéMon",
                fontFamily = PokemonFont,
                fontSize = 80.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = "Choose a type to explore",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp, top = 16.dp)
            ) {
                items(pokemonTypes) { type ->
                    TypeCard(
                        pokemonType = type,
                        onClick = { onTypeSelected(type.name) }
                    )
                }
            }
        }
    }
}

@Composable
fun TypeCard(pokemonType: PokemonType, onClick: () -> Unit) {
    MyButton(
        text = pokemonType.name,
        onClick = onClick,
        fontSize = 14.sp,
        backgroundColor = pokemonType.color,
        modifier = Modifier.fillMaxWidth()
    )
}