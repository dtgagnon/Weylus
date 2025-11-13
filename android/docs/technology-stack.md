# Weylus Android - Technology Stack

## Overview

This document provides detailed information about the technology choices for the Weylus Android native implementation, including rationale, alternatives considered, and usage guidelines.

---

## Core Technologies

### Programming Language

#### Kotlin
**Version**: 1.9.x+

**Why Kotlin**:
- Official language for Android development
- Null safety reduces crashes
- Coroutines for async programming
- Extension functions for cleaner code
- Better than Java for modern Android development
- Excellent IDE support (Android Studio)
- Interoperable with Java libraries

**Alternatives Considered**:
- **Java**: More verbose, lacks modern features
- **Flutter/Dart**: Cross-platform but worse performance for video
- **React Native**: Not suitable for low-latency video streaming

**Usage**:
```kotlin
// Modern Kotlin idioms
val result = repository.getData()
    .mapNotNull { it.value }
    .filter { it.isValid }
    .firstOrNull()
```

---

## Android SDK

### Minimum SDK: API 24 (Android 7.0)
**Why API 24**:
- Covers ~95% of active devices (as of 2024)
- Good MediaCodec support
- Modern API features available
- Reasonable to support

### Target SDK: API 34 (Android 14)
**Why API 34**:
- Latest stable Android version
- Required for Play Store submission
- Access to latest features
- Security improvements

---

## UI Framework

### Jetpack Compose
**Version**: 1.5.x+

**Why Compose**:
- Modern declarative UI framework
- Recommended by Google
- Less boilerplate than XML
- Reactive UI updates with Flow/State
- Better performance
- Easier to test

**Alternatives Considered**:
- **XML Layouts**: Legacy, more verbose
- **View Binding**: Better than XML but not declarative

**Usage**:
```kotlin
@Composable
fun VideoScreen(viewModel: VideoViewModel) {
    val state by viewModel.videoState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is VideoState.Loading -> LoadingIndicator()
            is VideoState.Playing -> VideoSurface()
            is VideoState.Error -> ErrorMessage(state.message)
        }
    }
}
```

**Key Components**:
- **Material 3**: Modern Material Design
- **Navigation Compose**: Type-safe navigation
- **Accompanist**: Additional Compose utilities

---

## Architecture Components

### MVVM (Model-View-ViewModel)

**Why MVVM**:
- Recommended by Google
- Clear separation of concerns
- Testable business logic
- Lifecycle-aware
- Works well with Compose

**Components**:

#### ViewModel
```kotlin
@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val repository: WeylusRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ConnectionState>(ConnectionState.Idle)
    val state: StateFlow<ConnectionState> = _state.asStateFlow()

    fun connect(url: String) {
        viewModelScope.launch {
            repository.connect(url)
                .collect { result ->
                    _state.value = result
                }
        }
    }
}
```

#### Repository Pattern
```kotlin
class WeylusRepository @Inject constructor(
    private val webSocket: WeylusWebSocket,
    private val localStorage: LocalStorage
) {
    suspend fun connect(url: String): Flow<ConnectionResult> = flow {
        emit(ConnectionResult.Connecting)
        webSocket.connect(url)
        emit(ConnectionResult.Connected)
    }
}
```

---

## Dependency Injection

### Hilt
**Version**: 2.48+

**Why Hilt**:
- Built on Dagger, but simpler
- Recommended by Google
- Android-specific annotations
- Automatic ViewModel injection
- Scoping support

**Alternatives Considered**:
- **Dagger 2**: More complex setup
- **Koin**: Simpler but reflection-based, slower

**Setup**:
```kotlin
@HiltAndroidApp
class WeylusApplication : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity()

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }
}
```

---

## Networking

### OkHttp
**Version**: 4.12.x

**Why OkHttp**:
- Industry standard
- Excellent WebSocket support
- Connection pooling
- Automatic retries
- HTTP/2 and HTTP/3 support
- Interceptors for logging/auth

**Alternatives Considered**:
- **Ktor Client**: Good but OkHttp more mature
- **Java WebSocket API**: Basic, lacks features

**Usage**:
```kotlin
val client = OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(0, TimeUnit.SECONDS) // No timeout for streaming
    .writeTimeout(10, TimeUnit.SECONDS)
    .pingInterval(30, TimeUnit.SECONDS)
    .addInterceptor(loggingInterceptor)
    .build()

val request = Request.Builder()
    .url("ws://192.168.1.100:1701")
    .build()

val webSocket = client.newWebSocket(request, listener)
```

---

## Serialization

### kotlinx.serialization
**Version**: 1.6.x

**Why kotlinx.serialization**:
- Official Kotlin library
- Compile-time code generation (fast)
- Type-safe
- Supports JSON, protobuf, CBOR
- Works with Kotlin multiplatform

**Alternatives Considered**:
- **Gson**: Reflection-based, slower
- **Moshi**: Good but kotlinx.serialization better for Kotlin

**Usage**:
```kotlin
@Serializable
data class PointerEvent(
    @SerialName("event_type")
    val eventType: String,
    val x: Float,
    val y: Float,
    val pressure: Float
)

// Serialization
val json = Json.encodeToString(pointerEvent)

// Deserialization
val event = Json.decodeFromString<PointerEvent>(json)
```

---

## Async Programming

### Kotlin Coroutines
**Version**: 1.7.x

**Why Coroutines**:
- Built into Kotlin
- Lightweight (100k+ concurrent coroutines possible)
- Structured concurrency
- Easy error handling
- Cancellation support
- Integration with Android lifecycle

**Alternatives Considered**:
- **RxJava**: More complex, less Kotlin-friendly
- **Callbacks**: Callback hell, hard to manage

**Usage**:
```kotlin
viewModelScope.launch {
    try {
        val result = withContext(Dispatchers.IO) {
            repository.fetchData()
        }
        _state.value = Success(result)
    } catch (e: Exception) {
        _state.value = Error(e.message)
    }
}
```

### Flow
**Why Flow**:
- Reactive streams
- Backpressure handling
- Transformation operators
- Cold streams (lazy)
- Integration with Compose

**Usage**:
```kotlin
val videoFrames: Flow<VideoFrame> = flow {
    while (true) {
        val frame = decoder.decodeNextFrame()
        emit(frame)
    }
}

// Collect in UI
LaunchedEffect(Unit) {
    videoFrames.collect { frame ->
        displayFrame(frame)
    }
}
```

---

## Video Processing

### MediaCodec
**Android Framework API**

**Why MediaCodec**:
- Hardware-accelerated decoding
- Native Android API
- Low latency
- Direct rendering to Surface
- Supports all Android devices

**Alternatives Considered**:
- **ExoPlayer**: Overkill for this use case, adds latency
- **FFmpeg**: Software decoding, slower, larger binary
- **VLC**: Too heavy, not designed for low latency

**Usage**:
```kotlin
val codec = MediaCodec.createDecoderByType("video/avc")

val format = MediaFormat.createVideoFormat(
    MediaFormat.MIMETYPE_VIDEO_AVC,
    1920,
    1080
).apply {
    setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 0)
    setInteger(MediaFormat.KEY_LOW_LATENCY, 1)
}

codec.configure(format, surface, null, 0)
codec.start()

// Feed data
val inputBuffer = codec.getInputBuffer(index)
inputBuffer.put(h264Data)
codec.queueInputBuffer(index, 0, h264Data.size, timestampUs, 0)

// Get decoded frame
val info = MediaCodec.BufferInfo()
val outputIndex = codec.dequeueOutputBuffer(info, 10000)
codec.releaseOutputBuffer(outputIndex, true) // Render to surface
```

**Codecs Supported**:
- **H.264 (AVC)**: Primary codec, universal support
- **H.265 (HEVC)**: Optional, better compression, less device support

---

## Storage

### DataStore
**Version**: 1.0.x

**Why DataStore**:
- Recommended replacement for SharedPreferences
- Type-safe with Proto DataStore
- Async API
- Data consistency guarantees
- Flow-based

**Alternatives Considered**:
- **SharedPreferences**: Synchronous, not type-safe
- **Room Database**: Overkill for preferences

**Usage**:
```kotlin
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// Write
suspend fun saveServerUrl(url: String) {
    dataStore.edit { preferences ->
        preferences[SERVER_URL_KEY] = url
    }
}

// Read
val serverUrlFlow: Flow<String?> = dataStore.data
    .map { preferences ->
        preferences[SERVER_URL_KEY]
    }
```

---

## Database (Optional)

### Room
**Version**: 2.6.x

**Why Room** (if needed):
- Official Android ORM
- Compile-time SQL verification
- LiveData/Flow support
- Migration support

**Usage**:
```kotlin
@Entity
data class ServerConnection(
    @PrimaryKey val id: Int,
    val name: String,
    val url: String,
    val lastConnected: Long
)

@Dao
interface ConnectionDao {
    @Query("SELECT * FROM serverconnection ORDER BY lastConnected DESC")
    fun getAllConnections(): Flow<List<ServerConnection>>

    @Insert
    suspend fun insert(connection: ServerConnection)
}
```

---

## QR Code

### ZXing (Zebra Crossing)
**Version**: 3.5.x

**Why ZXing**:
- Industry standard
- Fast and accurate
- No Google Play Services dependency
- Open source

**Alternatives Considered**:
- **ML Kit**: Requires Google Play Services
- **CameraX + ML Kit**: More complex

**Usage**:
```kotlin
// Using ZXing Android Embedded
IntentIntegrator(activity).apply {
    setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
    setPrompt("Scan Weylus QR Code")
    setOrientationLocked(true)
    initiateScan()
}
```

---

## Testing

### Unit Testing

#### JUnit 5
**Version**: 5.10.x

**Why JUnit 5**:
- Modern testing framework
- Better than JUnit 4
- Parameterized tests
- Extension model

#### MockK
**Version**: 1.13.x

**Why MockK**:
- Kotlin-first mocking library
- Better than Mockito for Kotlin
- Coroutines support
- DSL syntax

**Usage**:
```kotlin
@Test
fun `test connection establishes successfully`() = runTest {
    val mockWebSocket = mockk<WeylusWebSocket>()
    coEvery { mockWebSocket.connect(any()) } returns Unit

    val repository = WeylusRepository(mockWebSocket)
    repository.connect("ws://test")

    coVerify { mockWebSocket.connect("ws://test") }
}
```

#### Turbine
**Version**: 1.0.x

**Why Turbine**:
- Flow testing library
- Clean API for testing emissions
- Timeout handling

**Usage**:
```kotlin
@Test
fun `test video state changes`() = runTest {
    viewModel.videoState.test {
        assertEquals(VideoState.Idle, awaitItem())

        viewModel.startVideo()
        assertEquals(VideoState.Loading, awaitItem())
        assertEquals(VideoState.Playing, awaitItem())

        cancelAndIgnoreRemainingEvents()
    }
}
```

### Integration Testing

#### AndroidX Test
**Why AndroidX Test**:
- Official testing library
- Hermetic test environment
- Instrumentation tests

### UI Testing

#### Compose Testing
**Why Compose Testing**:
- Built into Compose
- Semantic tree testing
- Better than Espresso for Compose

**Usage**:
```kotlin
@Test
fun testConnectionScreen() {
    composeTestRule.setContent {
        ConnectionScreen()
    }

    composeTestRule
        .onNodeWithText("Server URL")
        .performTextInput("ws://192.168.1.100:1701")

    composeTestRule
        .onNodeWithText("Connect")
        .performClick()

    composeTestRule
        .onNodeWithText("Connected")
        .assertIsDisplayed()
}
```

---

## Build System

### Gradle (Kotlin DSL)
**Version**: 8.2+

**Why Gradle KTS**:
- Official Android build system
- Kotlin DSL for type safety
- IDE support
- Powerful plugin system

**Project Structure**:
```kotlin
// build.gradle.kts (project level)
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}

// build.gradle.kts (app level)
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    kotlin("kapt")
}

android {
    namespace = "com.weylus.android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.weylus.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
```

### Version Catalogs
**Why Version Catalogs**:
- Centralized dependency management
- Type-safe accessors
- Share versions across modules

**Usage** (gradle/libs.versions.toml):
```toml
[versions]
kotlin = "1.9.20"
compose = "1.5.4"
okhttp = "4.12.0"

[libraries]
okhttp = { module = "com.squareup.okhttp3:okhttp", version.ref = "okhttp" }
compose-ui = { module = "androidx.compose.ui:ui" }

[plugins]
android-application = { id = "com.android.application", version = "8.2.0" }
```

---

## CI/CD

### GitHub Actions

**Why GitHub Actions**:
- Free for open source
- Native GitHub integration
- Good Android support
- Matrix builds for multiple configs

**Workflow**:
```yaml
name: Android CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Build with Gradle
      run: ./gradlew build

    - name: Run tests
      run: ./gradlew test

    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-debug
        path: app/build/outputs/apk/debug/app-debug.apk
```

---

## Logging

### Timber
**Version**: 5.0.x

**Why Timber**:
- Better than Log class
- Tree-based logging
- Automatic tagging
- Release/debug configurations

**Usage**:
```kotlin
class WeylusApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }
}

// Usage
Timber.d("Connection established")
Timber.e(exception, "Failed to decode frame")
```

---

## Crash Reporting (Future)

### Firebase Crashlytics

**Why Crashlytics** (when ready for production):
- Free
- Real-time crash reports
- Detailed stack traces
- User impact metrics
- Integration with Firebase

**Alternatives**:
- **Sentry**: Good, but paid for high volume
- **Bugsnag**: Similar to Crashlytics

---

## Performance Monitoring

### Android Profiler
**Built into Android Studio**

**Features**:
- CPU profiling
- Memory profiling
- Network profiling
- Energy profiling

### Custom Metrics
```kotlin
class PerformanceMonitor {
    private var frameCount = 0
    private var startTime = System.nanoTime()

    fun onFrameDecoded() {
        frameCount++
        val elapsed = System.nanoTime() - startTime
        if (elapsed > 1_000_000_000) { // 1 second
            val fps = frameCount.toFloat() / (elapsed / 1_000_000_000f)
            Timber.d("FPS: %.2f", fps)
            frameCount = 0
            startTime = System.nanoTime()
        }
    }
}
```

---

## Summary Table

| Category | Technology | Version | Why |
|----------|-----------|---------|-----|
| Language | Kotlin | 1.9.x | Modern, safe, Android-first |
| UI | Jetpack Compose | 1.5.x | Declarative, modern |
| Architecture | MVVM | - | Google recommended |
| DI | Hilt | 2.48+ | Simplified Dagger |
| Networking | OkHttp | 4.12.x | Best WebSocket support |
| Serialization | kotlinx.serialization | 1.6.x | Fast, type-safe |
| Async | Coroutines + Flow | 1.7.x | Kotlin native |
| Video | MediaCodec | Android API | Hardware accelerated |
| Storage | DataStore | 1.0.x | Modern preferences |
| QR Code | ZXing | 3.5.x | Industry standard |
| Testing | JUnit 5 + MockK | Latest | Kotlin-friendly |
| Build | Gradle (KTS) | 8.2+ | Android standard |
| Logging | Timber | 5.0.x | Better than Log |

---

This technology stack provides a solid foundation for building a high-performance, maintainable Android application with modern best practices.
