# CLAUDE.md — Planify

Architecture reference for Claude. Read this before generating any code.

---

## What is Planify

Android mobile app for university students. A study toolkit that centralizes tasks,
pomodoro, flashcards and notes in one place. Part of a two-app ecosystem with Teachly
(teacher-facing app) sharing a Supabase backend.

- Student app: this repo
- Teacher app: https://github.com/brayanalaya/teachly
- Backend: https://github.com/ericklopezdev/api-plnf-tchl (Supabase)

---

## Tech stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Repository Pattern |
| DI | Hilt |
| Async | Coroutines + StateFlow |
| Networking | Retrofit + Gson |
| Backend | Supabase (REST + Auth) |
| Auth | OTP email + Google Sign-In |

---

## Project structure

```
com.planify/
├── theme/                  # PlColors, PlTypography, PlSpacing
├── constants/              # AppConstants, Routes
├── components/             # PlButton, PlInput, PlCard… (reusable composables)
├── logic/
│   ├── validators/         # Pure local validation — no network calls
│   └── utils/              # General utilities
├── api/
│   ├── client/             # RetrofitClient (Supabase base URL + headers)
│   ├── models/             # Shared DTOs (request/response data classes)
│   └── services/           # Retrofit interfaces + Repositories
└── screens/
    ├── auth/
    │   ├── AuthScreen.kt
    │   ├── AuthViewModel.kt
    │   ├── OtpScreen.kt
    │   ├── OtpViewModel.kt
    │   ├── OnboardingScreen.kt
    │   └── OnboardingViewModel.kt
    ├── home/
    │   ├── HomeScreen.kt
    │   └── HomeViewModel.kt
    ├── tasks/
    │   ├── TasksScreen.kt
    │   ├── TasksViewModel.kt
    │   ├── TaskDetailScreen.kt
    │   ├── TaskDetailViewModel.kt
    │   ├── AddTaskScreen.kt
    │   └── AddTaskViewModel.kt
    ├── pomodoro/
    │   ├── PomodoroScreen.kt
    │   └── PomodoroViewModel.kt
    ├── flashcards/         # planned
    ├── notes/              # planned
    └── profile/
        ├── ProfileScreen.kt
        └── ProfileViewModel.kt
```

Each screen folder contains only:
- `{Name}Screen.kt` — pure UI, composables only, no logic
- `{Name}ViewModel.kt` — state + presentation logic + local validation calls

Network calls and DTOs live in `api/`, never inside `screens/`.

---

## Backend: Supabase

Called through Retrofit using the Supabase REST API. Every request needs two headers:

```kotlin
// api/client/RetrofitClient.kt
object RetrofitClient {
    val instance: Retrofit by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("apikey", BuildConfig.SUPABASE_ANON_KEY)
                    .addHeader("Authorization", "Bearer ${TokenManager.getToken()}")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.SUPABASE_URL + "/rest/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
```

Supabase REST conventions:
- Tables → `/rest/v1/{table_name}`
- Filter → `?column=eq.value`
- Select fields → `?select=id,name,created_at`
- Auth OTP send → `POST /auth/v1/otp`
- Auth OTP verify → `POST /auth/v1/verify`

---

## Code conventions

### Components (`components/`)
- Prefix `Pl` on every composable: `PlButton`, `PlInput`, `PlCard`, `PlLoader`, `PlBadge`
- Always include `modifier: Modifier = Modifier` as a parameter
- Internal styles use `PlColors`, `PlTypography`, `PlSpacing` — never hardcoded values

```kotlin
// components/PlButton.kt
@Composable
fun PlButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = PlColors.Primary
        )
    ) {
        Text(text, style = PlTypography.labelMedium)
    }
}
```

### Theme (`theme/`)

```kotlin
// theme/Colors.kt
object PlColors {
    val Primary   = Color(0xFF534AB7)
    val OnPrimary = Color(0xFFFFFFFF)
    val Surface   = Color(0xFFF1EFE8)
    val Error     = Color(0xFFE24B4A)
    val TextMain  = Color(0xFF1C1B1F)
    val TextHint  = Color(0xFF888780)
}

// theme/Spacing.kt
object PlSpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
}
```

### Screen (`screens/{name}/{Name}Screen.kt`)
- Observes `uiState` from ViewModel via `collectAsState()`
- Delegates every action to ViewModel functions
- No business logic, no validation, no direct API calls
- Split into private composables if it exceeds ~60 lines

```kotlin
// screens/tasks/TasksScreen.kt
@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel(),
    onNavigateToDetail: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is TasksUiState.Loading -> PlLoader()
        is TasksUiState.Error   -> PlErrorMessage(state.message)
        is TasksUiState.Success -> TasksContent(
            tasks    = state.tasks,
            onTaskClick = onNavigateToDetail,
            onToggle = viewModel::onToggleTask
        )
        else -> Unit
    }
}
```

### ViewModel (`screens/{name}/{Name}ViewModel.kt`)
- State as `sealed class` exposed via `StateFlow`
- Calls validators from `logic/validators/` before any network call
- Calls repositories from `api/services/` for network operations
- Never imports `android.content.Context`

```kotlin
// screens/tasks/TasksViewModel.kt
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TasksUiState>(TasksUiState.Idle)
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    fun loadTasks() {
        viewModelScope.launch {
            _uiState.value = TasksUiState.Loading
            tasksRepository.getTasks()
                .onSuccess { _uiState.value = TasksUiState.Success(it) }
                .onFailure { _uiState.value = TasksUiState.Error(it.message ?: "Error") }
        }
    }

    fun onToggleTask(taskId: Int) {
        viewModelScope.launch {
            tasksRepository.toggleTask(taskId)
                .onSuccess { loadTasks() }
        }
    }
}

sealed class TasksUiState {
    object Idle    : TasksUiState()
    object Loading : TasksUiState()
    data class Success(val tasks: List<Task>) : TasksUiState()
    data class Error(val message: String)     : TasksUiState()
}
```

### Repository (`api/services/{Name}Repository.kt`)
- Always returns `Result<T>`, never throws exceptions
- Three catches: `HttpException`, `IOException`, `Exception`
- No business logic — data transport only

```kotlin
// api/services/TasksRepository.kt
class TasksRepository @Inject constructor(
    private val api: TasksService
) {
    suspend fun getTasks(): Result<List<Task>> = try {
        Result.success(api.getTasks())
    } catch (e: HttpException) {
        Result.failure(Exception("Server error ${e.code()}"))
    } catch (e: IOException) {
        Result.failure(Exception("No internet connection"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun toggleTask(taskId: Int): Result<Unit> = try {
        api.toggleTask(taskId)
        Result.success(Unit)
    } catch (e: HttpException) {
        Result.failure(Exception("Server error ${e.code()}"))
    } catch (e: IOException) {
        Result.failure(Exception("No internet connection"))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

### Service (`api/services/{Name}Service.kt`)

```kotlin
// api/services/TasksService.kt
interface TasksService {
    @GET("tasks")
    suspend fun getTasks(): List<Task>

    @POST("tasks")
    suspend fun createTask(@Body request: CreateTaskRequest): Task

    @PATCH("tasks")
    suspend fun toggleTask(@Query("id") taskId: Int): Task

    @DELETE("tasks")
    suspend fun deleteTask(@Query("id") taskId: Int)
}
```

### Models (`api/models/`)

```kotlin
// api/models/TaskModels.kt
data class CreateTaskRequest(
    @SerializedName("title")      val title: String,
    @SerializedName("due_date")   val dueDate: String?,
    @SerializedName("priority")   val priority: String
)

data class Task(
    @SerializedName("id")          val id: Int,
    @SerializedName("title")       val title: String,
    @SerializedName("is_done")     val isDone: Boolean,
    @SerializedName("due_date")    val dueDate: String?,
    @SerializedName("priority")    val priority: String,
    @SerializedName("created_at")  val createdAt: String
)
```

### Validators (`logic/validators/`)

```kotlin
// logic/validators/TaskValidator.kt
object TaskValidator {
    fun validateTitle(title: String): String? {
        if (title.isBlank()) return "Title is required"
        if (title.length > 100) return "Title is too long"
        return null  // null = valid
    }
}

// logic/validators/AuthValidator.kt
object AuthValidator {
    fun validateEmail(email: String): String? {
        if (email.isBlank()) return "Email is required"
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Invalid email"
        return null
    }

    fun validateOtp(code: String): String? {
        if (code.length != 6) return "Code must be 6 digits"
        if (!code.all { it.isDigit() }) return "Code must contain only digits"
        return null
    }
}
```

---

## Auth flow

```
AuthScreen (email input)
    ↓  POST /auth/v1/otp  { email }
    ↓  backend always returns same response (anti user-enumeration)
OtpScreen (6-digit code)
    ↓  POST /auth/v1/verify  { email, token, type: "email" }
    ↓  returns { access_token, isNewUser }
    ├── isNewUser: true  →  OnboardingScreen  →  HomeScreen
    └── isNewUser: false →  HomeScreen
```

---

## Navigation (`constants/Routes.kt`)

```kotlin
object Routes {
    const val AUTH          = "auth"
    const val OTP           = "otp/{email}"
    const val ONBOARDING    = "onboarding"
    const val HOME          = "home"
    const val TASKS         = "tasks"
    const val TASK_DETAIL   = "tasks/{taskId}"
    const val ADD_TASK      = "tasks/add"
    const val POMODORO      = "pomodoro"
    const val PROFILE       = "profile"
}
```

---

## Gradle dependencies

```kotlin
// Compose BOM
implementation(platform("androidx.compose:compose-bom:2024.02.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.activity:activity-compose:1.8.2")

// Lifecycle + ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.6")

// Hilt
implementation("com.google.dagger:hilt-android:2.50")
kapt("com.google.dagger:hilt-compiler:2.50")
implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

// Networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// Google Sign-In
implementation("com.google.android.gms:play-services-auth:21.0.0")
```

---

## Common mistakes to avoid

| Wrong | Correct |
|---|---|
| Logic inside Screen | Move to ViewModel |
| API call from ViewModel directly | Use Repository in `api/services/` |
| Hardcoded `Color(0xFF...)` in component | Use `PlColors.Primary` |
| Hardcoded `16.dp` in Screen | Use `PlSpacing.md` |
| Repository throwing exceptions | Always return `Result<T>` |
| Validating in Repository | Validate only in ViewModel |
| Revealing if email exists in auth | Always return same response |
| DTOs inside `screens/` | DTOs only in `api/models/` |

---

## Feature priority

**Must have:** auth, tasks, pomodoro

**Nice to have:** home/dashboard, flashcards, notes, profile, local notifications

**Out of scope:** schedule/calendar, offline sync, social features
