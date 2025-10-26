# UI and ViewModel Implementation Pattern

This document describes the ViewModel and state management pattern for screens.

For detailed Compose (UI) implementation guidelines, see [docs/compose.md](compose.md).

## File Structure

When creating a new screen (e.g., `SampleScreen`), create these files in `feature/sample/`:

```
feature/sample/
  |-- SampleScreen.kt         # Composable UI
  |-- SampleViewModel.kt      # State management
  |-- SampleUiState.kt        # UI state (data class)
  |-- SampleUserAction.kt     # User actions (sealed interface)
  |-- SampleEvent.kt          # One-shot UI events (sealed interface)
```

## SampleScreen.kt

- For detailed Compose implementation, see [docs/compose.md](compose.md)

## SampleViewModel.kt

Manages state and handles user actions.

```kotlin
class SampleViewModel(
    private val repository: SampleRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SampleUiState())
    val uiState: StateFlow<SampleUiState> = _uiState.asStateFlow()

    private val _events = Channel<SampleEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SampleUserAction) {
        when (action) {
            is SampleUserAction.InputTextChanged -> {
                _uiState.update { it.copy(inputText = action.text) }
            }
            is SampleUserAction.SubmitClicked -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.submitData(_uiState.value.inputText)
                _uiState.update { it.copy(isLoading = false) }
                _events.send(SampleEvent.ShowSnackbar("Success"))
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}
```

**Key Points:**
- Use `MutableStateFlow` for state, expose as `StateFlow`
- Use `Channel` for events, expose as `Flow`
- Handle all actions in `onAction()`
- Use `viewModelScope` for coroutines
- Update state with `.copy()`

## SampleUiState.kt

Immutable data class representing UI state.

```kotlin
data class SampleUiState(
    val inputText: String = "",
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
```

## SampleUserAction.kt

All possible user interactions.

```kotlin
sealed interface SampleUserAction {
    data class InputTextChanged(val text: String) : SampleUserAction
    data object SubmitClicked : SampleUserAction
}
```

## SampleEvent.kt

One-shot UI events (navigation, dialogs, snackbars).

```kotlin
sealed interface SampleEvent {
    data object NavigateBack : SampleEvent
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
    ) : SampleEvent
    data class ShowDialog(val title: String, val message: String) : SampleEvent
}
```

**Key Points:**
- Snackbar events can include an optional `actionLabel` for user actions
- The shared SnackbarHostState (from NavRoot) handles all snackbar displays
- Snackbars persist across screen transitions

## Data Flow

```
User Interaction
    |
    v
SampleScreen emits UserAction
    |
    v
ViewModel.onAction() handles action
    |
    v
Update state OR emit event
    |
    v
Screen recomposes OR handles event
```

## Usage in NavRoot

Use the extension function in `NavRoot.kt`:

```kotlin
@Composable
fun NavRoot() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    NavHost(
        navController = navController,
        startDestination = Route.Initial,
    ) {
        sampleScreen(navController, snackbarHostState)
        // other screens...
    }
}
```

**Key Points:**
- Create shared `SnackbarHostState` in NavRoot
- Pass it to all screen extensions
- Each screen places `SnackbarHost` in its own Scaffold
- Snackbar persists across screen transitions (shared state)
- Snackbar position adapts to each screen's layout
- For more details, see [docs/compose.md](compose.md)

## Registering with Koin

Add to `feature/ViewModelModule.kt`:

```kotlin
val viewModelModule = module {
    factory { SampleViewModel(get()) }
}
```

## Best Practices

**DO:**
- Keep Screen Composable stateless (only receive uiState, onAction, modifier)
- Use NavGraphBuilder extension to manage ViewModel and events
- Use `onAction` pattern for all user interactions
- Use `Channel` and `Flow` for one-shot events (navigation, dialogs, snackbars)
- Keep UI state immutable (use data class and `.copy()`)
- Use `MutableStateFlow` for state, expose as `StateFlow`
- Handle loading and error states in UiState
- Use `viewModelScope` for coroutines

**DON'T:**
- Pass ViewModel directly to Screen Composable
- Put business logic in Composables
- Call repository directly from Screen
- Store Events in StateFlow (use Channel instead)
- Mutate state directly (always use `.update { it.copy(...) }`)
