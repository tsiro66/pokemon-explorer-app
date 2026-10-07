package com.example.pokemonexplorerapp.di

import android.content.Context
import android.content.pm.ApplicationInfo
import com.example.pokemonexplorerapp.data.CaptureStore
import com.example.pokemonexplorerapp.data.PokemonRepository
import com.example.pokemonexplorerapp.data.network.PokeApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Manual dependency container owned by [com.example.pokemonexplorerapp.PokemonExplorerApp].
 * Centralizes object creation so ViewModels stay constructor-injectable and testable
 * (a fake `PokeApiService` or `CaptureStore` can be passed in unit tests).
 */
class AppContainer(context: Context) {

    private val isDebuggable =
        (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .apply {
            if (isDebuggable) {
                addInterceptor(
                    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
                )
            }
        }
        .build()

    private val pokeApiService: PokeApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokeApiService::class.java)

    val pokemonRepository = PokemonRepository(pokeApiService)
    val captureStore = CaptureStore(context)

    private companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
    }
}
