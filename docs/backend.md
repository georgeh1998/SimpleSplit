# Backend (Supabase)

## Overview

SimpleSplit uses **Supabase** as the backend service.

**Installed Plugins:**
- `Auth` - User authentication with PKCE flow
- `Postgrest` - Database queries

## Database Schema

### 1. profiles
User profile information (linked to Supabase Auth).

```sql
CREATE TABLE profiles (
  id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
  name TEXT,
  created_at TIMESTAMP DEFAULT now()
);
```

### 2. groups
Expense groups created by users.

```sql
CREATE TABLE groups (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  owner_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
  name TEXT NOT NULL,
  description TEXT,
  created_at TIMESTAMP DEFAULT now()
);
```

### 3. group_members
Many-to-many relationship between groups and users.

```sql
CREATE TABLE group_members (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  group_id UUID REFERENCES groups(id) ON DELETE CASCADE,
  user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
  created_at TIMESTAMP DEFAULT now(),
  UNIQUE (group_id, user_id)
);
```

### 4. transactions
Expense transactions within groups.

```sql
CREATE TABLE transactions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  group_id UUID REFERENCES groups(id) ON DELETE CASCADE,
  user_id UUID REFERENCES auth.users(id) ON DELETE SET NULL,
  amount NUMERIC NOT NULL,
  memo TEXT,
  date DATE NOT NULL,
  created_at TIMESTAMP DEFAULT now()
);
```

## Row Level Security (RLS)

RLS policies will be defined to ensure:
- Users can only access their own `profiles` and `groups`
- Users can only access `transactions` for groups they belong to

**Status:** To be implemented

## Implementation Pattern

### SupabaseService

Define backend operations as an interface.

```kotlin
interface SupabaseService {
    val sessionStatus: Flow<SessionStatus>

    suspend fun signUpWith(signUpEmail: String, signUpPassword: String)
    suspend fun exchangeCodeForSession(code: String)

    suspend fun queryTransactions(): List<TransactionDto>
    suspend fun insertTransaction(transaction: TransactionDto)
}
```

Implement using `SupabaseClient`.

```kotlin
class SupabaseServiceImpl(
    private val supabaseClient: SupabaseClient,
) : SupabaseService {

    override val sessionStatus: Flow<SessionStatus>
        get() = supabaseClient.auth.sessionStatus

    override suspend fun signUpWith(signUpEmail: String, signUpPassword: String) {
        supabaseClient.auth.signUpWith(Email) {
            email = signUpEmail
            password = signUpPassword
        }
    }

    override suspend fun queryTransactions(): List<TransactionDto> {
        return supabaseClient.from(SupabaseTable.TRANSACTIONS)
            .select()
            .decodeList<TransactionDto>()
    }

    override suspend fun insertTransaction(transaction: TransactionDto) {
        supabaseClient.from(SupabaseTable.TRANSACTIONS)
            .insert(transaction)
    }
}
```

### DTO (Data Transfer Object)

Use `@Serializable` and map database column names with `@SerialName`.

```kotlin
@Serializable
data class TransactionDto(
    val id: String,
    @SerialName("group_id") val groupId: String,
    @SerialName("user_id") val userId: String?,
    val amount: Double,
    val memo: String?,
    val date: LocalDate,
    @SerialName("created_at") val createdAt: LocalDateTime,
)
```

**Key Points:**
- Use `@SerialName` for snake_case column names
- Use `kotlinx.datetime` types for dates
- Make fields nullable if they can be NULL in database

### SupabaseTable

Centralize table names as constants.

```kotlin
object SupabaseTable {
    const val PROFILES = "profiles"
    const val GROUPS = "groups"
    const val GROUP_MEMBERS = "group_members"
    const val TRANSACTIONS = "transactions"
}
```

## Basic Operations

### Select (Query)

```kotlin
// Select all
val transactions = supabaseClient.from(SupabaseTable.TRANSACTIONS)
    .select()
    .decodeList<TransactionDto>()

// Select with filter
val groupTransactions = supabaseClient.from(SupabaseTable.TRANSACTIONS)
    .select {
        filter {
            eq("group_id", groupId)
        }
    }
    .decodeList<TransactionDto>()

// Select single
val transaction = supabaseClient.from(SupabaseTable.TRANSACTIONS)
    .select {
        filter {
            eq("id", transactionId)
        }
    }
    .decodeSingle<TransactionDto>()
```

### Insert

```kotlin
val newTransaction = TransactionDto(
    id = UUID.randomUUID().toString(),
    groupId = groupId,
    userId = userId,
    amount = 1000.0,
    memo = "Lunch",
    date = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
)

supabaseClient.from(SupabaseTable.TRANSACTIONS)
    .insert(newTransaction)
```

### Update

```kotlin
supabaseClient.from(SupabaseTable.TRANSACTIONS)
    .update({
        set("amount", 1500.0)
        set("memo", "Dinner")
    }) {
        filter {
            eq("id", transactionId)
        }
    }
```

### Delete

```kotlin
supabaseClient.from(SupabaseTable.TRANSACTIONS)
    .delete {
        filter {
            eq("id", transactionId)
        }
    }
```

## Authentication

### Sign Up

```kotlin
supabaseClient.auth.signUpWith(Email, redirectUrl = "app://callback") {
    email = "user@example.com"
    password = "password123"
}
```

### Exchange Code for Session

After email confirmation, exchange the code for a session.

```kotlin
supabaseClient.auth.exchangeCodeForSession(code)
```

### Session Status

Monitor authentication state with `sessionStatus` Flow.

```kotlin
supabaseClient.auth.sessionStatus.collect { status ->
    when (status) {
        is SessionStatus.Authenticated -> {
            // User is logged in
            val user = status.session.user
        }
        is SessionStatus.NotAuthenticated -> {
            // User is not logged in
        }
        else -> {
            // Loading or network unavailable
        }
    }
}
```

## Koin Configuration

Configure Supabase client in `data/DataModule.kt`.

```kotlin
val dataModule = module {
    single {
        createSupabaseClient(BuildConfig.SUPABASE_URL, BuildConfig.SUPABASE_KEY) {
            defaultSerializer = KotlinXSerializer(Json)
            install(Auth) {
                flowType = FlowType.PKCE
            }
            install(Postgrest)
        }
    }
    single<SupabaseService> {
        SupabaseServiceImpl(get())
    }
}
```

**Key Points:**
- Use `single` scope for SupabaseClient (singleton)
- Configure `KotlinXSerializer` for JSON serialization
- Install `Auth` with PKCE flow for secure authentication
- Install `Postgrest` for database operations

## Adding New Tables

When adding a new table:

1. Define table in Supabase SQL editor
2. Add table name to `SupabaseTable` object
3. Create corresponding DTO in `data/dto/`
4. Add methods to `SupabaseService` interface
5. Implement methods in `SupabaseServiceImpl`
6. Define RLS policies in Supabase

## Best Practices

**DO:**
- Use DTOs for all database operations
- Define table names in `SupabaseTable`
- Handle nullable fields properly
- Use `@SerialName` for snake_case columns
- Catch exceptions and handle errors

**DON'T:**
- Hardcode table names in queries
- Expose `SupabaseClient` directly to Repository
- Store sensitive data without RLS
- Ignore authentication state changes
