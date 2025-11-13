# Weylus Android - Testing Strategy

## Overview

This document outlines the comprehensive testing strategy for the Weylus Android application, covering unit tests, integration tests, UI tests, performance tests, and manual testing procedures.

---

## Testing Pyramid

```
           ╱╲
          ╱  ╲
         ╱ E2E ╲           (5-10% of tests)
        ╱────────╲
       ╱          ╲
      ╱ Integration╲       (20-30% of tests)
     ╱──────────────╲
    ╱                ╲
   ╱   Unit Tests     ╲    (60-70% of tests)
  ╱────────────────────╲
```

**Philosophy**: More unit tests, fewer integration tests, even fewer end-to-end tests.

---

## Unit Testing

**Goal**: Test individual components in isolation

**Coverage Target**: 80%+ for critical code paths

### 1. Protocol Tests

Test message serialization and deserialization.

```kotlin
class ProtocolTest {

    @Test
    fun `serialize PointerEvent to JSON`() {
        val event = PointerEvent(
            eventType = "DOWN",
            pointerId = 0,
            timestamp = 1698765432100,
            x = 100.5f,
            y = 200.3f,
            pressure = 0.75f,
            tiltX = 15.0f,
            tiltY = -10.0f,
            toolType = "PEN",
            buttons = 0
        )

        val json = Json.encodeToString(ClientMessage.PointerEvent.serializer(), event)

        assertTrue(json.contains("\"event_type\":\"DOWN\""))
        assertTrue(json.contains("\"x\":100.5"))
        assertTrue(json.contains("\"tool_type\":\"PEN\""))
    }

    @Test
    fun `deserialize CapturableList from JSON`() {
        val json = """
            {
              "CapturableList": {
                "capturables": [
                  {"id": 0, "name": "Screen 1", "capturable_type": "Screen"},
                  {"id": 1, "name": "Chrome", "capturable_type": "Window"}
                ]
              }
            }
        """.trimIndent()

        val message = Json.decodeFromString<ServerMessage>(json)

        assertIs<ServerMessage.CapturableList>(message)
        assertEquals(2, message.capturables.size)
        assertEquals("Screen 1", message.capturables[0].name)
    }

    @Test
    fun `handle invalid JSON gracefully`() {
        val invalidJson = "{ invalid }"

        assertFailsWith<SerializationException> {
            Json.decodeFromString<ServerMessage>(invalidJson)
        }
    }
}
```

### 2. Input Mapping Tests

Test coordinate and pressure mapping.

```kotlin
class CoordinateMapperTest {

    private lateinit var mapper: CoordinateMapper

    @BeforeEach
    fun setup() {
        // Client: 1080x2400 portrait
        // Server: 1920x1080 landscape
        mapper = CoordinateMapper(
            clientWidth = 1080,
            clientHeight = 2400,
            serverWidth = 1920,
            serverHeight = 1080,
            aspectRatioMode = AspectRatioMode.FIT
        )
    }

    @Test
    fun `map coordinates with aspect ratio fit`() {
        val (x, y) = mapper.mapToServer(540f, 1200f)

        // Center of client should map to center of server
        assertEquals(960f, x, 1f)
        assertEquals(540f, y, 1f)
    }

    @Test
    fun `clamp out-of-bounds coordinates`() {
        val (x, y) = mapper.mapToServer(-100f, 3000f)

        assertTrue(x >= 0f)
        assertTrue(y >= 0f)
        assertTrue(x <= 1920f)
        assertTrue(y <= 1080f)
    }
}

class PressureMapperTest {

    @Test
    fun `linear pressure curve`() {
        val mapper = PressureMapper(curve = PressureCurve.LINEAR)

        assertEquals(0f, mapper.map(0f))
        assertEquals(0.5f, mapper.map(0.5f))
        assertEquals(1f, mapper.map(1f))
    }

    @Test
    fun `soft pressure curve increases sensitivity`() {
        val mapper = PressureMapper(curve = PressureCurve.SOFT)

        val mapped = mapper.map(0.5f)

        // Soft curve should give higher output for same input
        assertTrue(mapped > 0.5f)
    }

    @Test
    fun `hard pressure curve decreases sensitivity`() {
        val mapper = PressureMapper(curve = PressureCurve.HARD)

        val mapped = mapper.map(0.5f)

        // Hard curve should give lower output for same input
        assertTrue(mapped < 0.5f)
    }
}
```

### 3. Video Buffer Tests

Test frame buffer management.

```kotlin
class FrameBufferTest {

    private lateinit var buffer: FrameBuffer

    @BeforeEach
    fun setup() {
        buffer = FrameBuffer(capacity = 5)
    }

    @Test
    fun `enqueue and dequeue frames`() {
        val frame1 = ByteArray(1000)
        val frame2 = ByteArray(1000)

        buffer.enqueue(frame1, timestamp = 100)
        buffer.enqueue(frame2, timestamp = 200)

        assertEquals(2, buffer.size)

        val dequeued1 = buffer.dequeue()
        assertNotNull(dequeued1)
        assertEquals(100, dequeued1.timestamp)

        val dequeued2 = buffer.dequeue()
        assertNotNull(dequeued2)
        assertEquals(200, dequeued2.timestamp)
    }

    @Test
    fun `drop oldest frame when buffer is full`() {
        repeat(6) { i ->
            buffer.enqueue(ByteArray(1000), timestamp = i * 100L)
        }

        // Buffer capacity is 5, so oldest should be dropped
        assertEquals(5, buffer.size)

        val oldest = buffer.dequeue()
        assertNotNull(oldest)
        assertEquals(100, oldest.timestamp) // First frame (0) was dropped
    }

    @Test
    fun `clear buffer`() {
        buffer.enqueue(ByteArray(1000), timestamp = 100)
        buffer.enqueue(ByteArray(1000), timestamp = 200)

        buffer.clear()

        assertEquals(0, buffer.size)
        assertNull(buffer.dequeue())
    }
}
```

### 4. ViewModel Tests

Test ViewModels with fake repositories.

```kotlin
class ConnectionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ConnectionViewModel
    private lateinit var fakeRepository: FakeWeylusRepository

    @BeforeEach
    fun setup() {
        fakeRepository = FakeWeylusRepository()
        viewModel = ConnectionViewModel(fakeRepository)
    }

    @Test
    fun `connect updates state to Connected on success`() = runTest {
        viewModel.connectionState.test {
            assertEquals(ConnectionState.Disconnected, awaitItem())

            viewModel.connect("ws://test")

            assertEquals(ConnectionState.Connecting, awaitItem())
            assertEquals(ConnectionState.Connected, awaitItem())
        }
    }

    @Test
    fun `connect updates state to Error on failure`() = runTest {
        fakeRepository.shouldFail = true

        viewModel.connectionState.test {
            assertEquals(ConnectionState.Disconnected, awaitItem())

            viewModel.connect("ws://test")

            assertEquals(ConnectionState.Connecting, awaitItem())

            val error = awaitItem()
            assertIs<ConnectionState.Error>(error)
        }
    }

    @Test
    fun `disconnect closes connection`() = runTest {
        viewModel.connect("ws://test")
        advanceUntilIdle()

        viewModel.disconnect()
        advanceUntilIdle()

        assertFalse(fakeRepository.isConnected)
    }
}
```

### 5. Repository Tests

Test repositories with mocked dependencies.

```kotlin
class WeylusRepositoryTest {

    private lateinit var repository: WeylusRepository
    private lateinit var mockWebSocket: WeylusWebSocket

    @BeforeEach
    fun setup() {
        mockWebSocket = mockk<WeylusWebSocket>(relaxed = true)
        repository = WeylusRepository(mockWebSocket)
    }

    @Test
    fun `connect establishes WebSocket connection`() = runTest {
        coEvery { mockWebSocket.connect(any()) } returns Unit

        repository.connect("ws://test")

        coVerify { mockWebSocket.connect("ws://test") }
    }

    @Test
    fun `sendMessage serializes and sends`() = runTest {
        val message = ClientMessage.GetCapturableList

        repository.sendMessage(message)

        verify { mockWebSocket.send(any<String>()) }
    }
}
```

---

## Integration Testing

**Goal**: Test how components work together

### 1. WebSocket Communication Tests

Test real WebSocket communication with mock server.

```kotlin
class WebSocketIntegrationTest {

    private lateinit var mockServer: MockWebServer
    private lateinit var webSocket: WeylusWebSocket

    @BeforeEach
    fun setup() {
        mockServer = MockWebServer()
        mockServer.start()
        webSocket = WeylusWebSocket(OkHttpClient(), MessageHandler())
    }

    @AfterEach
    fun teardown() {
        mockServer.shutdown()
    }

    @Test
    fun `establish WebSocket connection`() = runTest {
        webSocket.connect(mockServer.url("/").toString())

        advanceTimeBy(1000)

        assertEquals(ConnectionState.Connected, webSocket.connectionState.value)
    }

    @Test
    fun `send and receive messages`() = runTest {
        webSocket.connect(mockServer.url("/").toString())
        advanceUntilIdle()

        webSocket.send(ClientMessage.GetCapturableList)

        val request = mockServer.takeRequest()
        assertTrue(request.body.readUtf8().contains("GetCapturableList"))
    }

    @Test
    fun `auto-reconnect on connection loss`() = runTest {
        webSocket.connect(mockServer.url("/").toString())
        advanceUntilIdle()

        // Simulate connection loss
        mockServer.shutdown()
        advanceTimeBy(2000)

        // Start new server
        mockServer = MockWebServer()
        mockServer.start()

        advanceTimeBy(5000)

        // Should reconnect
        assertEquals(ConnectionState.Connected, webSocket.connectionState.value)
    }
}
```

### 2. End-to-End Flow Tests

Test complete workflows.

```kotlin
class EndToEndFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var mockServer: MockWebServer

    @BeforeEach
    fun setup() {
        mockServer = MockWebServer()
        mockServer.start()
    }

    @Test
    fun `complete connection flow`() {
        // Start at connection screen
        composeTestRule
            .onNodeWithText("Server URL")
            .performTextInput(mockServer.url("/").toString())

        composeTestRule
            .onNodeWithText("Connect")
            .performClick()

        // Mock server response
        mockServer.enqueue(MockResponse()
            .withWebSocketUpgrade(object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    val capturableList = """
                        {
                          "CapturableList": {
                            "capturables": [
                              {"id": 0, "name": "Screen 1", "capturable_type": "Screen"}
                            ]
                          }
                        }
                    """.trimIndent()
                    webSocket.send(capturableList)
                }
            })
        )

        // Should show capturable selection
        composeTestRule
            .onNodeWithText("Screen 1")
            .assertIsDisplayed()
            .performClick()

        // Should navigate to video screen
        composeTestRule
            .onNodeWithTag("video_surface")
            .assertIsDisplayed()
    }
}
```

---

## UI Testing

**Goal**: Test user interface and interactions

### Compose UI Tests

```kotlin
class ConnectionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `displays server URL input`() {
        composeTestRule.setContent {
            ConnectionScreen(viewModel = mockk(relaxed = true))
        }

        composeTestRule
            .onNodeWithText("Server URL")
            .assertIsDisplayed()
    }

    @Test
    fun `connect button disabled when URL empty`() {
        composeTestRule.setContent {
            ConnectionScreen(viewModel = mockk(relaxed = true))
        }

        composeTestRule
            .onNodeWithText("Connect")
            .assertIsNotEnabled()
    }

    @Test
    fun `connect button enabled when URL provided`() {
        composeTestRule.setContent {
            val viewModel = mockk<ConnectionViewModel>(relaxed = true) {
                every { serverUrl } returns mutableStateOf("ws://test")
            }
            ConnectionScreen(viewModel = viewModel)
        }

        composeTestRule
            .onNodeWithText("Connect")
            .assertIsEnabled()
    }

    @Test
    fun `shows error message on connection failure`() {
        val viewModel = mockk<ConnectionViewModel>(relaxed = true) {
            every { connectionState } returns MutableStateFlow(
                ConnectionState.Error("Connection failed")
            )
        }

        composeTestRule.setContent {
            ConnectionScreen(viewModel = viewModel)
        }

        composeTestRule
            .onNodeWithText("Connection failed")
            .assertIsDisplayed()
    }
}

class VideoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `displays video surface`() {
        composeTestRule.setContent {
            VideoScreen(viewModel = mockk(relaxed = true))
        }

        composeTestRule
            .onNodeWithTag("video_surface")
            .assertIsDisplayed()
    }

    @Test
    fun `shows performance metrics when enabled`() {
        val viewModel = mockk<VideoViewModel>(relaxed = true) {
            every { performanceMetrics } returns MutableStateFlow(
                PerformanceMetrics(fps = 60f, latency = 15)
            )
            every { showMetrics } returns mutableStateOf(true)
        }

        composeTestRule.setContent {
            VideoScreen(viewModel = viewModel)
        }

        composeTestRule
            .onNodeWithText("FPS: 60")
            .assertIsDisplayed()
    }
}
```

---

## Performance Testing

**Goal**: Ensure app meets performance requirements

### 1. Latency Tests

```kotlin
class LatencyTest {

    @Test
    fun `measure input-to-server latency`() = runTest {
        val latencies = mutableListOf<Long>()

        repeat(100) {
            val startTime = System.nanoTime()

            // Simulate input event
            val event = PointerEvent(/* ... */)
            webSocket.send(event)

            // Wait for server acknowledgment (mock)
            delay(10) // Simulated network latency

            val endTime = System.nanoTime()
            val latency = (endTime - startTime) / 1_000_000 // Convert to ms
            latencies.add(latency)
        }

        val avgLatency = latencies.average()
        val p95Latency = latencies.sorted()[95]

        println("Average latency: $avgLatency ms")
        println("P95 latency: $p95Latency ms")

        // Assert latency requirements
        assertTrue(avgLatency < 20, "Average latency should be < 20ms")
        assertTrue(p95Latency < 50, "P95 latency should be < 50ms")
    }
}
```

### 2. Frame Rate Tests

```kotlin
class FrameRateTest {

    @Test
    fun `maintain 60fps during video playback`() = runTest {
        val decoder = H264Decoder(mockSurface, mockCallback)
        val frameTimestamps = mutableListOf<Long>()

        // Simulate 60fps video stream (16.67ms per frame)
        repeat(600) { // 10 seconds of video
            val timestamp = System.nanoTime()
            frameTimestamps.add(timestamp)

            decoder.decodeFrame(mockFrameData, timestamp)

            delay(16) // 16ms per frame = ~60fps
        }

        // Calculate actual FPS
        val durationMs = (frameTimestamps.last() - frameTimestamps.first()) / 1_000_000
        val actualFps = (frameTimestamps.size.toFloat() / durationMs) * 1000

        println("Actual FPS: $actualFps")

        // Allow 5% variance
        assertTrue(actualFps >= 57f, "FPS should be at least 57")
        assertTrue(actualFps <= 63f, "FPS should be at most 63")
    }
}
```

### 3. Memory Tests

```kotlin
class MemoryTest {

    @Test
    fun `no memory leaks after 1000 frames`() {
        val runtime = Runtime.getRuntime()
        val initialMemory = runtime.totalMemory() - runtime.freeMemory()

        repeat(1000) {
            val frame = ByteArray(1920 * 1080 * 3 / 2) // YUV420 frame
            frameBuffer.enqueue(frame, timestamp = it * 16L)
            frameBuffer.dequeue()
        }

        // Force garbage collection
        System.gc()
        Thread.sleep(1000)

        val finalMemory = runtime.totalMemory() - runtime.freeMemory()
        val memoryGrowth = finalMemory - initialMemory

        println("Memory growth: ${memoryGrowth / 1024 / 1024} MB")

        // Memory growth should be minimal (< 10MB)
        assertTrue(memoryGrowth < 10 * 1024 * 1024, "Memory growth should be < 10MB")
    }
}
```

### 4. Battery Tests

Manual testing with Android Studio Profiler:

1. Run app for 30 minutes
2. Monitor CPU usage (should be < 20% average)
3. Monitor battery drain (should be < 10% per hour)
4. Check wake locks (should release when idle)

---

## Device Testing

### Test Matrix

| Device | Android Version | Screen Size | Stylus Support | Priority |
|--------|----------------|-------------|----------------|----------|
| Google Pixel 7 | 14 | 6.3" | No | High |
| Samsung Galaxy Tab S9 | 14 | 11" | S Pen | High |
| OnePlus 9 | 13 | 6.55" | No | Medium |
| Samsung Galaxy S22 Ultra | 13 | 6.8" | S Pen | High |
| Xiaomi Pad 6 | 13 | 11" | No | Medium |
| Old device (testing min SDK) | 7.0 | Various | No | Low |

### Test Scenarios

1. **Basic Functionality**
   - Connect to server
   - Select capturable
   - View video stream
   - Send touch input
   - Disconnect

2. **Stylus Testing** (S Pen devices)
   - Pressure sensitivity
   - Tilt detection
   - Hover events
   - Button functionality
   - Palm rejection

3. **Network Conditions**
   - WiFi with good signal
   - WiFi with poor signal
   - Mobile data
   - Network switch (WiFi to mobile)
   - Connection loss and recovery

4. **Edge Cases**
   - App in background
   - Screen rotation
   - Low battery mode
   - Low memory conditions
   - Airplane mode

---

## Automated Testing Pipeline

### CI/CD with GitHub Actions

```yaml
name: Android CI

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
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

      - name: Run unit tests
        run: ./gradlew test

      - name: Run instrumentation tests
        uses: reactivecircus/android-emulator-runner@v2
        with:
          api-level: 30
          target: default
          arch: x86_64
          script: ./gradlew connectedCheck

      - name: Upload test reports
        if: always()
        uses: actions/upload-artifact@v3
        with:
          name: test-reports
          path: |
            app/build/reports/tests/
            app/build/reports/androidTests/

      - name: Code coverage
        run: ./gradlew jacocoTestReport

      - name: Upload coverage to Codecov
        uses: codecov/codecov-action@v3
        with:
          files: app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml
```

---

## Manual Testing Checklist

### Before Each Release

- [ ] Connection to server works (WiFi)
- [ ] Connection to server works (USB tethering)
- [ ] QR code scanning works
- [ ] All capturables are listed correctly
- [ ] Video displays correctly
- [ ] Touch input works accurately
- [ ] Stylus pressure works (if available)
- [ ] Stylus tilt works (if available)
- [ ] Keyboard input works
- [ ] Scroll gestures work
- [ ] Settings persist after app restart
- [ ] App handles disconnection gracefully
- [ ] App handles network changes
- [ ] App doesn't crash when backgrounded
- [ ] Rotation works correctly
- [ ] Performance is acceptable (no lag)
- [ ] Battery usage is reasonable
- [ ] No memory leaks (use Profiler)

---

## Bug Reporting Template

```markdown
## Bug Report

**Device**: Samsung Galaxy Tab S9
**Android Version**: 14
**App Version**: 1.0.0
**Server Version**: 0.11.4

### Description
Brief description of the issue

### Steps to Reproduce
1. Step one
2. Step two
3. ...

### Expected Behavior
What should happen

### Actual Behavior
What actually happens

### Logs
```
Paste relevant logs here
```

### Screenshots
Attach screenshots if applicable
```

---

## Testing Tools

- **JUnit 5**: Unit testing framework
- **MockK**: Mocking library for Kotlin
- **Turbine**: Flow testing library
- **Compose Test**: UI testing for Compose
- **Robolectric**: Android unit tests that run on JVM
- **Espresso**: UI testing (legacy, if needed)
- **MockWebServer**: Mock HTTP/WebSocket server
- **Android Profiler**: Performance monitoring
- **LeakCanary**: Memory leak detection

---

## Definition of Done (Testing)

A feature is considered complete when:

- [ ] Unit tests written and passing (80%+ coverage)
- [ ] Integration tests passing
- [ ] UI tests passing (for UI components)
- [ ] Manual testing completed on 3+ devices
- [ ] Performance benchmarks met
- [ ] No critical bugs
- [ ] Code reviewed
- [ ] Documentation updated

---

This comprehensive testing strategy ensures the Weylus Android app is reliable, performant, and provides an excellent user experience across a wide range of devices.
