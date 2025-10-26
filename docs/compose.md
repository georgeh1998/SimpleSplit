# Compose Implementation Guide

## Screen Structure

Each screen consists of two parts:
1. **NavGraphBuilder extension** - Manages Scaffold, ScaffoldState, ViewModel, and event handling
2. **Screen Composable** - Stateless private function that receives `uiState` and `onAction`

### NavGraphBuilder Extension

```kotlin
fun NavGraphBuilder.sampleScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
) {
    composable<Route.Sample> {
        val viewModel: SampleViewModel = koinInject()
        val uiState by viewModel.uiState.collectAsState()

        // Handle one-shot events
        LaunchedEffect(Unit) {
            viewModel.events.collect { event ->
                when (event) {
                    is SampleEvent.NavigateToDetail -> {
                        navController.navigate(Route.Detail(event.id))
                    }
                    is SampleEvent.ShowSnackbar -> {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.actionLabel,
                        )
                    }
                    SampleEvent.NavigateBack -> {
                        navController.navigateUp()
                    }
                }
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Sample") },
                )
            },
            bottomBar = {
                // If needed
                // BottomNavigationBar()
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { paddingValues ->
            SampleScreen(
                uiState = uiState,
                onAction = viewModel::onAction,
                modifier = Modifier.padding(paddingValues),
            )
        }
    }
}
```

### Screen Composable

```kotlin
@Composable
private fun SampleScreen(
    uiState: SampleUiState,
    onAction: (SampleUserAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Content
    }
}
```

**Key Points:**
- Scaffold is defined in NavGraphBuilder extension (for screen-specific TopBar/BottomBar)
- **SnackbarHostState is shared** (passed from NavRoot), but **SnackbarHost is in each Scaffold**
- This allows Snackbar to persist across screens while respecting each screen's layout
- Scaffold automatically positions Snackbar above BottomBar (if present)
- Screen function is `private` and stateless
- Screen receives only `uiState`, `onAction`, and `modifier`
- `paddingValues` from Scaffold is passed to Screen via `modifier`
- Event handling (navigation, snackbars) happens in NavGraphBuilder extension

**Why shared SnackbarHostState + per-screen SnackbarHost?**
- **Shared State**: Snackbar persists during screen transitions
- **Per-screen Host**: Snackbar position adapts to each screen's layout (above BottomBar, etc.)
- Users can see feedback even after navigation (e.g., "Saved successfully")
- Snackbar actions (e.g., "View Details") can trigger navigation
- Consistent UX with proper positioning for each screen

## Component Definition

### Private Components (Screen-Specific)

Components used only within a screen should be `private` and defined in the same file.

```kotlin
@Composable
private fun SampleComponent(
    data: String,
    onAction: (SampleUserAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        onClick = { onAction(SampleUserAction.ItemClicked(data)) }
    ) {
        Text(text = data)
    }
}
```

**Pass only necessary data:**
- Don't pass entire `UiState` to sub-components
- Extract only the data needed by the component
- Always include `modifier: Modifier = Modifier` parameter

```kotlin
// Good
SampleComponent(
    data = uiState.itemName,
    onAction = onAction,
)

// Bad - don't pass entire UiState
SampleComponent(
    uiState = uiState,
    onAction = onAction,
)
```

### Public Components (Reusable)

Components reusable across multiple screens should be defined in `ui_component/`.

```kotlin
// ui_component/PrimaryButton.kt
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        Text(text = text)
    }
}
```

## Spacing

Use `Spacer` for spacing between components instead of `Modifier.padding`.

```kotlin
Column {
    Text("Title")

    Spacer(modifier = Modifier.height(16.dp))

    Text("Description")

    Spacer(modifier = Modifier.height(8.dp))

    Button(onClick = { }) {
        Text("Submit")
    }
}
```

**Horizontal spacing:**
```kotlin
Row {
    Text("Name")

    Spacer(modifier = Modifier.width(8.dp))

    Text("Value")
}
```

**Why Spacer?**
- Explicit and visible in code
- Easier to adjust spacing
- Consistent spacing across the app

## Preview

Each Screen file should include Preview functions for the stateless Screen Composable.

```kotlin
@Preview
@Composable
private fun PreviewSampleScreen() {
    MaterialTheme {
        SampleScreen(
            uiState = SampleUiState(
                inputText = "Sample Text",
                isLoading = false,
            ),
            onAction = {},
        )
    }
}
```

**Key Points:**
- Preview function is `private`
- Name: `Preview{ScreenName}`
- Wrap in `MaterialTheme` for proper theming
- Preview the Screen Composable directly (without Scaffold)
- Provide realistic sample data
- Set `onAction = {}` (no-op)

**Preview multiple states:**
```kotlin
@Preview
@Composable
private fun PreviewSampleScreenLoading() {
    MaterialTheme {
        SampleScreen(
            uiState = SampleUiState(isLoading = true),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun PreviewSampleScreenError() {
    MaterialTheme {
        SampleScreen(
            uiState = SampleUiState(errorMessage = "Error occurred"),
            onAction = {},
        )
    }
}
```

## NavRoot Setup

The shared SnackbarHostState is created in NavRoot and passed to all screens.

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
        detailScreen(navController, snackbarHostState)
        // other screens...
    }
}
```

**Key Points:**
- Create `SnackbarHostState` in NavRoot with `remember`
- Pass it to all screen extensions
- Each screen's Scaffold places `SnackbarHost` in the appropriate position
- Snackbar persists across screen transitions (shared state)
- Snackbar position adapts to each screen's layout (BottomBar, etc.)

## Material3 Components

Use Material3 components from `androidx.compose.material3`.

**Common components:**
- `Scaffold` - Screen layout with TopBar/BottomBar
- `TopAppBar` - Top app bar
- `Button` - Primary action button
- `OutlinedButton` - Secondary action button
- `Card` - Container for content
- `OutlinedTextField` - Text input
- `Text` - Text display
- `Icon` - Icons
- `SnackbarHost` - Displays snackbars

## Best Practices

**DO:**
- Use NavGraphBuilder extension for each screen
- Manage `Scaffold` in NavGraphBuilder extension
- **Share `SnackbarHostState` across all screens** (create in NavRoot, pass to all screens)
- **Place `SnackbarHost` in each Scaffold** (for proper positioning with BottomBar, etc.)
- Keep Screen Composable stateless (only receive `uiState`, `onAction`, `modifier`)
- Pass `paddingValues` from Scaffold to Screen via `modifier`
- Define screen-specific components as `private`
- Pass only necessary data to components
- Use `Spacer` for spacing
- Include Previews for different states
- Use Material3 components
- Always include `modifier: Modifier = Modifier` parameter

**DON'T:**
- Put Scaffold inside the Screen Composable
- Create separate `SnackbarHostState` in each screen (use shared state)
- Place `SnackbarHost` outside NavHost (use per-screen placement in Scaffold)
- Pass ViewModel directly to Screen Composable
- Pass entire `UiState` to sub-components
- Use excessive `Modifier.padding` for spacing
- Hard-code colors (use `MaterialTheme.colorScheme`)
- Hard-code text sizes (use `MaterialTheme.typography`)
