package com.example.pokemonexplorerapp

import android.app.Application
import com.example.pokemonexplorerapp.di.AppContainer

class PokemonExplorerApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
