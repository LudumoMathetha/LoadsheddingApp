# Load-Shedding Tracker

## Project Overview
Load-Shedding Tracker is an Android mobile application prototype developed for tracking electricity load-shedding stages and schedules in South Africa. The application allows users to create local accounts, search for suburbs across South African municipalities, save multiple locations, view daily and weekly load-shedding schedule slots, and access previously loaded schedules offline via a local Room Database.

## Key Features Implemented (Part 2 Prototype)
- User Authentication: Local prototype registration, login, logout, password validation, and SHA-256 hashed password storage.
- Suburb Management: Search suburbs via REST API, view current load-shedding stage, add multiple suburbs, mark favorites, and delete saved suburbs.
- Schedule Tracking: View daily and weekly load-shedding schedule slots per suburb.
- Offline Room Database Caching: Automatic caching of suburb information and schedule slots in Room Database for offline access.
- User Preferences: Jetpack DataStore preferences for toggling Dark Mode, selecting preferred language (English, isiZulu, Afrikaans structure), and configuring notification lead time reminders.
- Robust Error Handling: User-friendly error messages and fallback mechanisms to prevent app crashes during network failures.

## Technology Stack
- Programming Language: Kotlin
- User Interface: Jetpack Compose, Material 3, Navigation Compose
- Architecture: MVVM (Model-View-ViewModel), Repository Pattern, Unidirectional Data Flow
- Local Storage: Room Database (Entities, DAOs, Async Flow), Jetpack DataStore Preferences
- Network & REST API: Retrofit 2, OkHttp 4, Gson Converter, HttpLoggingInterceptor
- Asynchronous Processing: Kotlin Coroutines, StateFlow, SharedFlow
- Annotation Processor: Google KSP (Kotlin Symbol Processing)
- Testing: JUnit 4, Kotlinx Coroutines Test

## Project Structure
```
com.example.loadsheddingapp
├── MainActivity.kt
│
├── data
│   ├── local
│   │   ├── AppDatabase.kt
│   │   ├── dao
│   │   │   ├── SuburbDao.kt
│   │   │   ├── ScheduleDao.kt
│   │   │   └── UserDao.kt
│   │   └── entity
│   │       ├── SuburbEntity.kt
│   │       ├── ScheduleEntity.kt
│   │       └── UserEntity.kt
│   │
│   ├── remote
│   │   ├── LoadSheddingApiService.kt
│   │   ├── RetrofitInstance.kt
│   │   ├── MockLoadSheddingData.kt
│   │   └── dto
│   │       ├── SuburbDto.kt
│   │       ├── ScheduleDto.kt
│   │       └── LoadSheddingResponseDto.kt
│   │
│   ├── repository
│   │   ├── AuthRepository.kt
│   │   ├── SuburbRepository.kt
│   │   └── ScheduleRepository.kt
│   │
│   └── preferences
│       └── UserPreferencesManager.kt
│
├── domain
│   └── model
│       ├── Suburb.kt
│       ├── Schedule.kt
│       ├── User.kt
│       └── UserPreferences.kt
│
├── ui
│   ├── components
│   │   ├── LoadingIndicator.kt
│   │   ├── ErrorMessage.kt
│   │   ├── SuburbCard.kt
│   │   └── ScheduleCard.kt
│   │
│   ├── navigation
│   │   ├── AppNavigation.kt
│   │   └── Screen.kt
│   │
│   ├── screens
│   │   ├── auth
│   │   │   ├── LoginScreen.kt
│   │   │   └── RegisterScreen.kt
│   │   ├── dashboard
│   │   │   └── DashboardScreen.kt
│   │   ├── suburbs
│   │   │   ├── SearchSuburbScreen.kt
│   │   │   └── SavedSuburbsScreen.kt
│   │   ├── schedule
│   │   │   └── ScheduleDetailScreen.kt
│   │   └── settings
│   │       └── SettingsScreen.kt
│   │
│   ├── theme
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   │
│   └── viewmodel
│       ├── AuthViewModel.kt
│       ├── DashboardViewModel.kt
│       ├── SuburbViewModel.kt
│       ├── ScheduleViewModel.kt
│       ├── SettingsViewModel.kt
│       └── ViewModelFactory.kt
│
└── utils
    ├── ValidationUtils.kt
    └── NetworkResult.kt
```

## Setup Instructions

### 1. Prerequisites
- Android Studio Ladybug / Jellyfish or newer
- JDK 17 / JDK 21
- Android SDK 36 (Minimum SDK 24)

### 2. Opening the Project
1. Launch Android Studio.
2. Select "Open" and select the project directory.
3. Wait for Gradle to finish syncing dependencies.

### 3. REST API Configuration
The base REST API URL is configured in `data/remote/RetrofitInstance.kt`:
```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/api/"
```
- For Android Emulator connecting to host machine localhost: Use `http://10.0.2.2:8080/api/`
- For Physical Android Device on local Wi-Fi: Use `http://<YOUR_COMPUTER_IP>:8080/api/`
- For Hosted Cloud API: Replace with `https://your-domain.com/api/`

## Room Database Architecture
The local Room Database (`AppDatabase`) manages three primary tables:
1. `suburbs`: Holds saved suburb metadata, favorite status, and stage info.
2. `schedules`: Holds schedule slot start/end times and stage details with a foreign key constraint linking to `suburbs` with `CASCADE` deletion.
3. `users`: Holds local prototype account credentials using SHA-256 password hashes.

## Authentication Overview
In Part 2, user registration and authentication are handled locally using `UserDao` and `AuthRepository`. Plaintext passwords are NEVER stored; passwords are hashed using SHA-256 in `ValidationUtils.kt`.
In subsequent PoE development stages, this local prototype authentication layer will be replaced with Firebase Authentication.

## Running the Application
1. Start an Android Virtual Device (AVD) emulator or connect a physical Android device via USB with ADB debugging enabled.
2. Click the Run button in Android Studio or execute:
```bash
./gradlew assembleDebug
```

## Running Unit Tests
Unit tests cover input validation, Room repository CRUD operations, offline cache fallback, and ViewModels.
To run unit tests from the command line:
```bash
./gradlew testDebugUnitTest
```
To run unit tests inside Android Studio:
Right-click on `app/src/test/java/com/example/loadsheddingapp` and select "Run Tests in 'com.example...'".

## Known Limitations
- Local Authentication: User credentials are managed locally in Room for Part 2 prototype purposes. Cloud session sync will be added with Firebase Auth in Part 3.
- Language Localization: Language options are selectable in Settings and stored in DataStore; string resource files for full isiZulu and Afrikaans translations will be expanded in future PoE stages.

## Recommended Git Commit Structure
```
Initial Android project setup
Added Room database entities and DAOs
Added Retrofit API integration
Added authentication screens
Added dashboard screen
Added suburb search functionality
Added schedule screen
Added settings screen
Added unit tests
Updated README
Added GitHub Actions workflow
```

## GitHub Actions Workflow
The project includes a GitHub Actions workflow defined in `.github/workflows/android.yml`. It runs automated unit tests and debug builds on every push or pull request to the `main` or `master` branch.
