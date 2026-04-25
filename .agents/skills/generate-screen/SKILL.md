---
description: Generate a Screen composable and wire it into Planify's navigation
---

Generate the Screen file and nav wiring for the feature: **$ARGUMENTS**

---

### 1. Create `app/src/main/java/com/planify/screens/{feature_lower}/{FeatureName}Screen.kt`

Follow the Planify Screen pattern exactly — pure UI, zero logic:

```kotlin
@Composable
fun {FeatureName}Screen(
    viewModel: {FeatureName}ViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
    // add other nav callbacks the screen needs
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is {FeatureName}UiState.Loading -> PlLoader()
        is {FeatureName}UiState.Error   -> PlErrorMessage(state.message)
        is {FeatureName}UiState.Success -> {FeatureName}Content(
            items   = state.items,
            // map ViewModel actions to lambda params
        )
        else -> Unit
    }
}

@Composable
private fun {FeatureName}Content(
    items: List<{FeatureName}>,
    modifier: Modifier = Modifier
) {
    // LazyColumn or Column with item cards
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items) { item ->
            // use PlCard or similar Pl* component
        }
    }
}
```

Rules for the Screen file:
- No business logic, no validation, no direct API calls in this file
- Get the ViewModel via `hiltViewModel()` — never construct it manually
- Observe state with `collectAsState()`
- Split into private composables when the file would exceed ~60 lines
- Always use `PlColors`, `PlTypography`, `PlSpacing` — never hardcoded values
- Use existing `Pl*` components from `components/` (PlButton, PlCard, PlInput, PlLoader…)

---

### 2. Add the route to `app/src/main/java/com/app/planify/constants/Routes.kt`

The `object Routes` is the nav singleton — the single source of truth for all route strings.
Add a new constant (and a builder helper if it takes an argument):

```kotlin
// No args
const val {FEATURE_NAME_UPPER} = "{feature_lower}"

// With an Int arg — also add a builder helper below the constants
const val {FEATURE_NAME_UPPER}_DETAIL = "{feature_lower}/{id}"
fun {featureName}Detail(id: Int) = "{feature_lower}/$id"
```

Never write route strings as raw literals anywhere else — always use `Routes.CONSTANT` or `Routes.helper()`.

---

### 3. Wire into `AppNavigation.kt`

Open `app/src/main/java/com/app/planify/AppNavigation.kt` and add inside the `NavHost` block:

```kotlin
// No-arg screen
composable(Routes.{FEATURE_NAME_UPPER}) {
    {FeatureName}Screen(
        onNavigateBack = { navController.popBackStack() }
    )
}

// Screen with Int arg
composable(
    route = Routes.{FEATURE_NAME_UPPER}_DETAIL,
    arguments = listOf(navArgument("id") { type = NavType.IntType })
) { backStackEntry ->
    val id = backStackEntry.arguments?.getInt("id") ?: return@composable
    {FeatureName}DetailScreen(
        itemId = id,
        onNavigateBack = { navController.popBackStack() }
    )
}
```

To navigate from another screen: `navController.navigate(Routes.{featureName}Detail(id))`

After creating the files, tell me what's still missing (ViewModel, Repository, etc.) and suggest the next command to run.

> If `AppNavigation.kt` and `Routes.kt` don't exist yet, run `/setup-navigation` first.
