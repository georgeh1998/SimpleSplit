# Architecture

## Overview

SimpleSplit follows the **MVVM (Model-View-ViewModel)** architecture pattern with clean architecture principles. 
The codebase is organized into distinct layers to ensure separation of concerns, testability, and maintainability.

## Architecture Pattern

```
+------------------------------------------+
|         Presentation Layer               |
|    (feature/*, Screen, ViewModel)        |
+------------------+-----------------------+
                   |
                   v
+------------------+-----------------------+
|         Domain Layer                     |
|         (repository/*)                   |
+------------------+-----------------------+
                   |
                   v
+------------------+-----------------------+
|         Data Layer                       |
|    (data/*, SupabaseService, DTOs)      |
+------------------------------------------+
```

## Layer Structure


### 1. Presentation Layer (`feature/`)

The presentation layer contains UI components and state management logic, organized by feature.

**Structure:**
- Each feature has its own package (e.g., `initial`)
- Each feature contains:
  - `*Screen.kt`: Composable UI components
  - `*ViewModel.kt`: State management and business logic orchestration
  - `*UiState.kt`: UI state data classes
  - Actions/Events: Sealed interfaces for user actions and navigation events

**Key Technologies:**
- Jetpack Compose for declarative UI
- ViewModels for state management
- Kotlin StateFlow for reactive state
- Type-safe Navigation with Compose Navigation

**Example Structure:**
```
feature/
  |-- initial/
  |     |-- InitialScreen.kt
  |     |-- InitialViewModel.kt
  |     |-- InitialUiState.kt
```

### 2. Domain Layer (`repository/`)

The domain layer contains business logic and acts as an intermediary between the presentation and data layers.

**Responsibilities:**
- Abstract data sources
- Expose domain-specific APIs to ViewModels
- Transform data models to domain models
- Handle business logic


**Example:**
```kotlin
class UserRepository(
    private val supabaseService: SupabaseService,
) {
    suspend fun login() {
        supabaseService.login()
    }
}
```

### 3. Data Layer (`data/`)

The data layer handles all external data operations and backend communication.

**Components:**
- `SupabaseService`: Interface defining backend operations
- `SupabaseServiceImpl`: Concrete implementation using Supabase client
- DTOs (`dto/`): Data transfer objects for API communication
- `DataModule`: Koin module for dependency injection

**Key Technologies:**
- Supabase SDK for backend services

**Example:**
```kotlin
interface SupabaseService {
    suspend fun login()
}

class SupabaseServiceImpl(
    private val supabaseClient: SupabaseClient
) : SupabaseService {
    override suspend fun login() {
        // Implementation using supabaseClient
    }
}
```

## Dependency Injection

The project uses **Koin** for dependency injection, configured in `di/Koin.kt`.

**Modules:**
- `viewModelModule`: Provides ViewModels with factory scope
- `repositoryModule`: Provides Repositories as singletons
- `dataModule`: Provides data sources and services as singletons

**Initialization:**
```kotlin
fun initKoin() = startKoin {
    modules(viewModelModule, repositoryModule, dataModule)
}
```

## State Management

### Unidirectional Data Flow (UDF)

The app follows a unidirectional data flow pattern:

1. **User Action** -> ViewModel method call
2. **ViewModel** -> Updates state or calls repository
3. **Repository** -> Fetches/updates data via data layer
4. **Data Layer** -> Communicates with backend
5. **State Update** -> UI recomposes

### State Flow Pattern

```kotlin
// ViewModel exposes StateFlow
val uiState: StateFlow<UiState>

// Repository exposes Flow
val sessionStatus: Flow<SessionStatus>

// ViewModel transforms and exposes as StateFlow
val uiState = repository.sessionStatus
    .map { /* transform */ }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initialValue)
```

## Navigation

The app uses **Jetpack Compose Navigation** with type-safe routes defined as serializable sealed interfaces.

**Features:**
- Type-safe navigation arguments
- Deep link support (e.g., email confirmation callbacks)
- Centralized navigation logic in `NavRoot.kt`

**Routes:**
```kotlin
sealed interface Route {
    @Serializable data object Initial : Route
}
```

## Package Structure

```
com.github.georgeh1998.simplesplit/
  |-- App.kt                      # Application entry point
  |-- di/                         # Dependency injection
  |     |-- Koin.kt
  |-- data/                       # Data layer
  |     |-- DataModule.kt
  |     |-- SupabaseService.kt
  |     |-- SupabaseServiceImpl.kt
  |     |-- SupabaseTable.kt
  |     |-- dto/                  # Data transfer objects
  |-- repository/                 # Domain layer
  |     |-- RepositoryModule.kt
  |     |-- UserRepository.kt
  |     |-- TransactionRepository.kt
  |-- feature/                    # Presentation layer
  |     |-- ViewModelModule.kt
  |     |-- initial/
  |     |-- signup/
  |     |-- expenseList/
  |     |-- waitingConfirmation/
  |     |-- navigation/
  |-- theme/                      # UI theming
  |     |-- Color.kt
  |     |-- Theme.kt
  |     |-- Typography.kt
  |-- ui_component/               # Reusable UI components
  |     |-- PrimaryButton.kt
  |-- util/                       # Utilities
        |-- Log.kt
```

## Data Flow Example

Example: User sign-up flow

```
1. User enters email/password in SignUpScreen
                |
                v
2. SignUpScreen calls viewModel.signUp(email, password)
                |
                v
3. SignUpViewModel calls userRepository.signUpWithEmail(email, password)
                |
                v
4. UserRepository calls supabaseService.signUpWith(email, password)
                |
                v
5. SupabaseService makes API call to Supabase backend
                |
                v
6. Response updates sessionStatus Flow
                |
                v
7. ViewModel transforms sessionStatus to UiState
                |
                v
8. Screen observes UiState and recomposes
                |
                v
9. Navigation triggered based on new state
```

## Platform-Specific Implementation

When platform-specific code is needed:

1. Define `expect` declarations in `commonMain`
2. Provide `actual` implementations in `androidMain` and `iosMain`

## Best Practices

1. **Single Responsibility**: Each class has one clear purpose
2. **Dependency Inversion**: Depend on abstractions (interfaces), not concrete implementations
3. **Reactive Programming**: Use Kotlin Flow for reactive data streams
4. **Immutable State**: UI state is immutable and read-only
5. **Scoped ViewModels**: Use factory scope for ViewModels, singleton for repositories
6. **Type Safety**: Leverage Kotlin's type system for compile-time safety
7. **Resource Management**: Centralize strings and resources in `composeResources/`