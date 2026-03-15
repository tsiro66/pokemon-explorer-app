package com.example.pokemonexplorerapp.data

import com.google.gson.annotations.SerializedName

data class PokemonDetailsResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val abilities: List<AbilitySlot>,
    val stats: List<StatSlot>,
    val types: List<TypeSlot>,
    val sprites: PokemonSprites
)

data class AbilitySlot(
    val ability: NamedApiResource,
    @SerializedName("is_hidden") val isHidden: Boolean,
    val slot: Int
)

data class StatSlot(
    @SerializedName("base_stat") val baseStat: Int,
    val stat: NamedApiResource
)

data class TypeSlot(
    val slot: Int,
    val type: NamedApiResource
)

data class PokemonSprites(
    @SerializedName("front_default") val frontDefault: String?,
    val other: OtherSprites? = null
)

data class OtherSprites(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtwork? = null
)

data class OfficialArtwork(
    @SerializedName("front_default") val frontDefault: String?
)