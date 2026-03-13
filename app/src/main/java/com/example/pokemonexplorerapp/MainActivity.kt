package com.example.pokemonexplorerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokemonexplorerapp.data.pokemonTypes
import com.example.pokemonexplorerapp.ui.screens.PokemonDetailsScreen
import com.example.pokemonexplorerapp.ui.screens.PokemonListScreen
import com.example.pokemonexplorerapp.ui.screens.TypeSelectionScreen
import com.example.pokemonexplorerapp.ui.theme.PokemonExplorerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonExplorerAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "type_selection",
                ) {
                    composable("type_selection") {
                        TypeSelectionScreen(
                            onTypeSelected = { typeName ->
                                navController.navigate("pokemon_list/$typeName")
                            }
                        )
                    }

                    composable("pokemon_list/{typeName}") { backStackEntry ->
                        val typeName = backStackEntry.arguments?.getString("typeName") ?: ""
                        val typeColor = pokemonTypes.find {
                            it.name.equals(typeName, ignoreCase = true)
                        }?.color ?: Color.Black

                        PokemonListScreen(
                            typeName = typeName,
                            typeColor = typeColor,
                            onPokemonSelected = { pokemonName ->
                                navController.navigate("pokemon_detail/$pokemonName/$typeName")
                            },
                            onBackPressed = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("pokemon_detail/{pokemonName}/{typeName}") { backStackEntry ->
                        val pokemonName = backStackEntry.arguments?.getString("pokemonName").orEmpty()
                        val typeName = backStackEntry.arguments?.getString("typeName").orEmpty()
                        val typeColor = pokemonTypes.find {
                            it.name.equals(typeName, ignoreCase = true)
                        }?.color ?: Color.Black

                        PokemonDetailsScreen(
                            pokemonName = pokemonName,
                            typeColor = typeColor,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
