# Pokémon Explorer App

A mobile application built with Jetpack Compose that implements a multi-screen navigation flow to browse Pokémon data from the PokéAPI. The project focuses on clean architecture, reactive state management, and efficient list handling.

## Technical Stack

* **UI:** Jetpack Compose (Declarative UI)
* **Navigation:** Navigation Compose
* **Networking:** Retrofit with GSON Converter
* **Image Loading:** Coil 3
* **Persistence:** Jetpack DataStore (Preferences)
* **Concurrency:** Kotlin Coroutines and Flow

## Architecture and Data Flow

The application is structured into a multi-layered architecture to ensure separation of concerns and testability.

### 1. Presentation Layer (UI)
The UI is built entirely in Compose, utilizing State Hoisting to maintain a unidirectional data flow.

* **Screens:** Stateless composables that receive state from ViewModels and emit events via lambdas.
* **Lifecycle:** Uses `LaunchedEffect` for side effects (fetching data) and `remember` for optimizing UI-local calculations like list filtering.



### 2. Logic Layer (ViewModel)
ViewModels manage screen state using `StateFlow`.

* **Shared State:** Uses `AndroidViewModel` to provide access to the Application Context for DataStore initialization.
* **State Transformation:** Implements `combine` and `stateIn` operators to merge network data with local persistence data (capture status) into a single UI state.

### 3. Data Layer
* **Repository Pattern:** Provides a clean API to the rest of the app, abstracting the source of data (Network vs. Disk).
* **Network:** Retrofit service interface defines asynchronous `suspend` functions for API interaction.
* **Persistence:** `CaptureStore` utilizes Jetpack DataStore to provide a reactive `Flow` of user-captured Pokémon, ensuring state is preserved across app restarts.

## Project Structure

* `data/`: Data models (DTOs), Retrofit service interfaces, and the Repository.
* `data/network/`: Retrofit client configuration.
* `ui/viewmodels/`: ViewModels managing StateFlow for the various screens.
* `ui/screens/`: Top-level screen composables.
* `ui/components/`: Reusable atomic UI elements and custom-styled widgets.

---

### Installation

1. Clone the repository.
2. Open in Android Studio Ladybug or later.
3. Sync Gradle and build the project.
4. Run on a device or emulator with API level 24 or higher.