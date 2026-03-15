package com.example.pokemonexplorerapp

import android.os.Bundle
import android.util.Log
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
import com.example.pokemonexplorerapp.ui.screens.PokemonPagerScreen
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
                            onPokemonSelected = { _, index ->
                                Log.d("PagerDebug", "Navigating with index: $index")
                                navController.navigate("pokemon_pager/$typeName/$index")
                            },
                            onBackPressed = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("pokemon_pager/{typeName}/{startIndex}") { backStackEntry ->
                        val typeName = backStackEntry.arguments?.getString("typeName").orEmpty()
                        val startIndex = backStackEntry.arguments?.getString("startIndex")
                            ?.toIntOrNull() ?: 0
                        val typeColor = pokemonTypes.find {
                            it.name.equals(typeName, ignoreCase = true)
                        }?.color ?: Color.Black

                        PokemonPagerScreen(
                            typeName = typeName,
                            typeColor = typeColor,
                            startIndex = startIndex,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
