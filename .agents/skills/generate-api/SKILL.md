---
description: Generate Service + Repository + Models for a Supabase table in Planify
---

Generate the full API layer for the feature: **$ARGUMENTS**

The argument format is: `FeatureName [supabase_table_name]`
If no table name is provided, default to the lowercase plural of FeatureName (e.g. `Task` → `tasks`).

Create three files following Planify's CLAUDE.md conventions:

---

### 1. `app/src/main/java/com/planify/api/models/{FeatureName}Models.kt`

Data class for the Supabase table row + request body. All fields must have `@SerializedName`:

```kotlin
data class {FeatureName}(
    @SerializedName("id")         val id: Int,
    @SerializedName("created_at") val createdAt: String
    // add domain-specific fields here
)

data class Create{FeatureName}Request(
    // only the fields the user sends — never id or created_at
)
```

---

### 2. `app/src/main/java/com/planify/api/services/{FeatureName}Service.kt`

Retrofit interface. Supabase REST conventions:
- Filter by column → `@Query("column") @Eq` style: `@Query("id") id: String = "eq.$value"` — but for Supabase use `@Query("id") id: String` and pass `"eq.$id"` from the repository
- Select fields → `@Query("select") select: String = "*"`
- Always `suspend fun`

```kotlin
interface {FeatureName}Service {

    @GET("{table_name}")
    suspend fun get{FeatureName}s(
        @Query("select") select: String = "*"
    ): List<{FeatureName}>

    @GET("{table_name}")
    suspend fun get{FeatureName}ById(
        @Query("id") id: String,
        @Query("select") select: String = "*"
    ): List<{FeatureName}>

    @POST("{table_name}")
    suspend fun create{FeatureName}(
        @Body request: Create{FeatureName}Request
    ): {FeatureName}

    @DELETE("{table_name}")
    suspend fun delete{FeatureName}(
        @Query("id") id: String
    )
}
```

---

### 3. `app/src/main/java/com/planify/api/services/{FeatureName}Repository.kt`

Always returns `Result<T>`. Three catch blocks — never throws:

```kotlin
class {FeatureName}Repository @Inject constructor(
    private val api: {FeatureName}Service
) {
    suspend fun get{FeatureName}s(): Result<List<{FeatureName}>> = try {
        Result.success(api.get{FeatureName}s())
    } catch (e: HttpException) {
        Result.failure(Exception("Server error ${e.code()}"))
    } catch (e: IOException) {
        Result.failure(Exception("No internet connection"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun create{FeatureName}(request: Create{FeatureName}Request): Result<{FeatureName}> = try {
        Result.success(api.create{FeatureName}(request))
    } catch (e: HttpException) {
        Result.failure(Exception("Server error ${e.code()}"))
    } catch (e: IOException) {
        Result.failure(Exception("No internet connection"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun delete{FeatureName}(id: Int): Result<Unit> = try {
        api.delete{FeatureName}("eq.$id")
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

---

Rules:
- DTOs (data classes) live only in `api/models/` — never inside `screens/`
- Repository never has business logic — data transport only
- The `RetrofitClient` singleton in `api/client/RetrofitClient.kt` already handles auth headers (apikey + Bearer token) and base URL. You do NOT recreate it — just inject the service via Hilt
- For Supabase filters, Retrofit `@Query` params are sent as plain query strings: `id=eq.5`, `user_id=eq.abc`
- For Supabase upsert/patch: use `@PATCH("{table_name}")` with `@Query("id") id: String` (value `"eq.$id"`)

After creating the files, remind me to register `{FeatureName}Service` in the Hilt module (or create one if it doesn't exist yet) so it can be injected into the Repository.
