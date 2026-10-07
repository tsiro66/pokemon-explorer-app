# Pokémon Explorer App — R8 rules

# API response models are deserialized by Gson via reflection:
# keep fields and constructors so serialization never breaks.
-keep class com.example.pokemonexplorerapp.data.** { *; }

# Generic type info retained for Retrofit/Gson.
-keepattributes Signature
-keepattributes *Annotation*

# Retrofit service interface is used reflectively via dynamic proxies.
-keepclassmembers,allowshrinking,allowobfuscation interface com.example.pokemonexplorerapp.data.network.PokeApiService {
    @retrofit2.http.* <methods>;
}

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.codehaus.mojo.animal_sniffer.*
