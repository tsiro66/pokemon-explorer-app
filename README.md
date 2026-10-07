# Pokémon Explorer App

A modern Pokémon browser built with Jetpack Compose. This application interfaces with the PokéAPI to provide a fluid experience for exploring Pokémon data, viewing detailed stats, and managing a personal collection.

---

## Key Features

### Type-Based Discovery
The journey begins with a categorized grid of Pokémon types. Selecting a type filters the database and dynamically updates the application's color scheme to match the chosen element (e.g., red for Fire, blue for Water).

### Paginated List and Search
The app features a paginated list that handles large amounts of data efficiently.
* **Search Integration:** Users can filter Pokémon by name in real-time.
* **In-Memory Caching:** Data is fetched once per type and cached, so revisiting a type or Pokémon is instant.

### Interactive Detail Pager
The details screen uses a paging system that allows users to swipe horizontally between Pokémon.
* **Statistics:** Combat stats like HP and Attack are displayed using animated progress bars that trigger as you navigate.
* **Physical Traits:** View official artwork alongside height, weight, abilities, and hidden traits.
* **Collection Management:** A capture toggle allows users to mark Pokémon as caught. This state is persisted locally using Jetpack DataStore, so your collection is saved even after closing the app.

---

## Technical Overview

The project is built using the latest Android development standards:

* **User Interface:** Built entirely with Jetpack Compose for a declarative and reactive UI.
* **State Management:** Uses ViewModels and Kotlin StateFlow with a sealed `UiState` (Loading / Error / Success) for a unidirectional data flow with no impossible states.
* **Dependency Injection:** Lightweight manual DI via an `AppContainer` owned by the `Application`, keeping ViewModels constructor-injectable.
* **Networking:** Uses Retrofit with an explicitly configured OkHttp client (timeouts, debug logging) to communicate with the PokéAPI.
* **Testing:** Unit tests for repository caching and ViewModel state transitions (JUnit + coroutines-test), backed by a fake API service.
* **Local Storage:** Implements Jetpack DataStore for lightweight, persistent key-value storage.
* **Image Handling:** Utilizes Coil for asynchronous image loading and caching.
* **State Management:** Uses ViewModels and Kotlin StateFlow to maintain a unidirectional data flow.

---

## Setup Instructions

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Sync the Gradle files to download the necessary dependencies.
4. Run the application on a physical device or emulator running API level 24 or higher.