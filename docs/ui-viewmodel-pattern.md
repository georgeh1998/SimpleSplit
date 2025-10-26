# UI (Compose) and ViewModel Implementation Pattern

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

Stateless Composable UI that only receives state and events.

```kotlin
fun NavGraphBuilder.sampleScreen(navController: NavController) {
    composable<Route.Sample> {
        val viewModel: SampleViewModel = koinInject()
        val uiState by viewModel.uiState.collectAsState()

        SampleScreen(
            uiState = uiState,
            events = viewModel.events,
            onAction = viewModel::onAction,
            navController = navController,
        )
    }
}


@Composable
private fun SampleScreen(
    uiState: SampleUiState,
    events: Flow<SampleEvent>,
    onAction: (SampleUserAction) -> Unit,
    navController: NavController,
) {
    // Handle one-shot events
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is SampleEvent.NavigateToDetail -> navController.navigate(Route.Detail(event.id))
                is SampleEvent.ShowSnackbar -> { /* show snackbar */ }
            }
        }
    }

    Column {
        OutlinedTextField(
            value = uiState.inputText,
            onValueChange = { onAction(SampleUserAction.InputTextChanged(it)) },
            enabled = !uiState.isLoading,
        )

        Button(
            onClick = { onAction(SampleUserAction.SubmitClicked) }
        ) {
            Text("Submit")
        }
    }
}
```

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
    data class ShowSnackbar(val message: String) : SampleEvent
    data class ShowDialog(val title: String, val message: String) : SampleEvent
}
```

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
    NavHost(
        navController = navController,
        startDestination = Route.Initial,
    ) {
        sampleScreen(navController)
        // other screens...
    }
}
```

## Registering with Koin

Add to `feature/ViewModelModule.kt`:

```kotlin
val viewModelModule = module {
    factory { SampleViewModel(get()) }
}
```

## Best Practices

**DO:**
- Keep Screen stateless (only receive state, events, onAction)
- Use NavGraphBuilder extension to manage ViewModel
- Use `onAction` pattern for all user interactions
- Use Events for navigation, dialogs, snackbars
- Keep state immutable
- Handle loading and error states

**DON'T:**
- Pass ViewModel directly to Screen
- Put business logic in Composables
- Call repository directly from Screen
- Store Events in StateFlow
