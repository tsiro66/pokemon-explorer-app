package com.example.pokemonexplorerapp.data.network

import com.example.pokemonexplorerapp.data.PokemonDetailsResponse
import com.example.pokemonexplorerapp.data.TypeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("type/{name}")
    suspend fun getTypeDetails(@Path("name") name: String): TypeResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(@Path("name") name: String): PokemonDetailsResponse
}