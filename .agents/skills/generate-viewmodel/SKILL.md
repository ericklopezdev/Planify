---
description: Generate a ViewModel + sealed UiState for a new Planify feature
---

Generate a ViewModel file for the feature: **$ARGUMENTS**

Follow Planify's CLAUDE.md conventions exactly. Create the file at:
`app/src/main/java/com/planify/screens/{feature_lower}/{FeatureName}ViewModel.kt`

Use this exact structure — do not deviate:

```kotlin
@HiltViewModel
class {FeatureName}ViewModel @Inject constructor(
    private val {featureName}Repository: {FeatureName}Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow<{FeatureName}UiState>({FeatureName}UiState.Idle)
    val uiState: StateFlow<{FeatureName}UiState> = _uiState.asStateFlow()

    init {
        load{FeatureName}s()
    }

    fun load{FeatureName}s() {
        viewModelScope.launch {
            _uiState.value = {FeatureName}UiState.Loading
            {featureName}Repository.get{FeatureName}s()
                .onSuccess { _uiState.value = {FeatureName}UiState.Success(it) }
                .onFailure { _uiState.value = {FeatureName}UiState.Error(it.message ?: "Error") }
        }
    }
}

sealed class {FeatureName}UiState {
    object Idle    : {FeatureName}UiState()
    object Loading : {FeatureName}UiState()
    data class Success(val items: List<{FeatureName}>) : {FeatureName}UiState()
    data class Error(val message: String)              : {FeatureName}UiState()
}
```

Rules:
- Use `StateFlow`, never `mutableStateOf` (that is the professor's simplified teaching version)
- Annotate with `@HiltViewModel` + `@Inject constructor`
- Never import `android.content.Context`
- The ViewModel calls the Repository; the Repository calls the Service (never call the Service directly from ViewModel)
- Call `load{FeatureName}s()` in `init` so data loads automatically when the screen opens
- If the feature needs validation before a write operation (e.g. create/update), call the corresponding validator from `logic/validators/` before calling the repository
- Add any extra actions the feature needs (delete, toggle, create) as additional `fun` methods following the same pattern: launch coroutine → call repository → update state

After creating the file, tell me what repository and service files still need to be created and suggest running `/generate-api {FeatureName}`.
