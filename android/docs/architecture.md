# Weylus Android - Technical Architecture

## Overview

The Weylus Android native implementation follows a modern Android architecture pattern using MVVM (Model-View-ViewModel) with clean separation of concerns.

## Architecture Pattern: MVVM

```
┌─────────────────────────────────────────────────────────┐
│                         UI Layer                         │
│  ┌────────────┐  ┌────────────┐  ┌─────────────────┐   │
│  │ Activities │  │ Fragments  │  │ Compose Screens │   │
│  └────────────┘  └────────────┘  └─────────────────┘   │
└───────────────────────┬─────────────────────────────────┘
                        │ observes
                        ▼
┌─────────────────────────────────────────────────────────┐
│                    ViewModel Layer                       │
│  ┌────────────────┐  ┌──────────────┐  ┌─────────────┐ │
│  │ ConnectionVM   │  │  VideoVM     │  │  InputVM    │ │
│  └────────────────┘  └──────────────┘  └─────────────┘ │
└───────────────────────┬─────────────────────────────────┘
                        │ uses
                        ▼
┌─────────────────────────────────────────────────────────┐
│                    Repository Layer                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ WeylusRepo   │  │  VideoRepo   │  │  SettingsRepo│  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└───────────────────────┬─────────────────────────────────┘
                        │ uses
                        ▼
┌─────────────────────────────────────────────────────────┐
│                     Data/Service Layer                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ WebSocketSvc │  │ VideoDecoder │  │ InputHandler │  │
│  │ NetworkSvc   │  │ MediaCodec   │  │ MotionEvents │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
```

## Project Structure

```
weylus-android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/weylus/android/
│   │   │   │   ├── ui/
│   │   │   │   │   ├── connection/
│   │   │   │   │   │   ├── ConnectionScreen.kt
│   │   │   │   │   │   └── ConnectionViewModel.kt
│   │   │   │   │   ├── video/
│   │   │   │   │   │   ├── VideoDisplayScreen.kt
│   │   │   │   │   │   └── VideoViewModel.kt
│   │   │   │   │   ├── settings/
│   │   │   │   │   │   ├── SettingsScreen.kt
│   │   │   │   │   │   └── SettingsViewModel.kt
│   │   │   │   │   ├── capturable/
│   │   │   │   │   │   ├── CapturableListScreen.kt
│   │   │   │   │   │   └── CapturableViewModel.kt
│   │   │   │   │   ├── navigation/
│   │   │   │   │   │   └── NavGraph.kt
│   │   │   │   │   └── theme/
│   │   │   │   │       ├── Theme.kt
│   │   │   │   │       └── Color.kt
│   │   │   │   │
│   │   │   │   ├── viewmodel/
│   │   │   │   │   └── base/
│   │   │   │   │       └── BaseViewModel.kt
│   │   │   │   │
│   │   │   │   ├── service/
│   │   │   │   │   ├── WeylusService.kt         # Foreground service
│   │   │   │   │   ├── ConnectionService.kt     # WebSocket management
│   │   │   │   │   └── VideoService.kt          # Video streaming
│   │   │   │   │
│   │   │   │   ├── network/
│   │   │   │   │   ├── websocket/
│   │   │   │   │   │   ├── WeylusWebSocket.kt
│   │   │   │   │   │   ├── MessageHandler.kt
│   │   │   │   │   │   └── ConnectionState.kt
│   │   │   │   │   ├── http/
│   │   │   │   │   │   └── HttpClient.kt
│   │   │   │   │   └── discovery/
│   │   │   │   │       └── ServerDiscovery.kt   # mDNS/Bonjour
│   │   │   │   │
│   │   │   │   ├── video/
│   │   │   │   │   ├── decoder/
│   │   │   │   │   │   ├── VideoDecoder.kt
│   │   │   │   │   │   ├── H264Decoder.kt
│   │   │   │   │   │   └── DecoderCallback.kt
│   │   │   │   │   ├── renderer/
│   │   │   │   │   │   ├── VideoRenderer.kt
│   │   │   │   │   │   └── SurfaceRenderer.kt
│   │   │   │   │   └── buffer/
│   │   │   │   │       ├── FrameBuffer.kt
│   │   │   │   │       └── BufferManager.kt
│   │   │   │   │
│   │   │   │   ├── input/
│   │   │   │   │   ├── handler/
│   │   │   │   │   │   ├── InputHandler.kt
│   │   │   │   │   │   ├── TouchHandler.kt
│   │   │   │   │   │   ├── StylusHandler.kt
│   │   │   │   │   │   └── KeyboardHandler.kt
│   │   │   │   │   ├── mapper/
│   │   │   │   │   │   ├── CoordinateMapper.kt
│   │   │   │   │   │   └── PressureMapper.kt
│   │   │   │   │   └── filter/
│   │   │   │   │       ├── PalmRejection.kt
│   │   │   │   │       └── InputFilter.kt
│   │   │   │   │
│   │   │   │   ├── protocol/
│   │   │   │   │   ├── messages/
│   │   │   │   │   │   ├── ClientMessage.kt
│   │   │   │   │   │   ├── ServerMessage.kt
│   │   │   │   │   │   ├── PointerEvent.kt
│   │   │   │   │   │   ├── KeyboardEvent.kt
│   │   │   │   │   │   └── ConfigMessage.kt
│   │   │   │   │   ├── serialization/
│   │   │   │   │   │   ├── MessageSerializer.kt
│   │   │   │   │   │   └── BinaryParser.kt
│   │   │   │   │   └── ProtocolVersion.kt
│   │   │   │   │
│   │   │   │   ├── repository/
│   │   │   │   │   ├── WeylusRepository.kt
│   │   │   │   │   ├── VideoRepository.kt
│   │   │   │   │   ├── SettingsRepository.kt
│   │   │   │   │   └── ConnectionRepository.kt
│   │   │   │   │
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── ServerConnection.kt
│   │   │   │   │   │   ├── VideoConfig.kt
│   │   │   │   │   │   ├── InputConfig.kt
│   │   │   │   │   │   └── Capturable.kt
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── datastore/
│   │   │   │   │   │   │   └── PreferencesManager.kt
│   │   │   │   │   │   └── database/
│   │   │   │   │   │       ├── WeylusDatabase.kt
│   │   │   │   │   │       └── dao/
│   │   │   │   │   │           └── ConnectionDao.kt
│   │   │   │   │   └── remote/
│   │   │   │   │       └── WeylusApi.kt
│   │   │   │   │
│   │   │   │   ├── util/
│   │   │   │   │   ├── extensions/
│   │   │   │   │   │   ├── ContextExt.kt
│   │   │   │   │   │   └── FlowExt.kt
│   │   │   │   │   ├── Constants.kt
│   │   │   │   │   ├── Logger.kt
│   │   │   │   │   └── NetworkUtils.kt
│   │   │   │   │
│   │   │   │   └── di/
│   │   │   │       ├── AppModule.kt
│   │   │   │       ├── NetworkModule.kt
│   │   │   │       ├── VideoModule.kt
│   │   │   │       └── RepositoryModule.kt
│   │   │   │
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   ├── values/
│   │   │   │   ├── drawable/
│   │   │   │   └── xml/
│   │   │   │       └── network_security_config.xml
│   │   │   │
│   │   │   └── AndroidManifest.xml
│   │   │
│   │   ├── test/
│   │   │   └── java/com/weylus/android/
│   │   │       ├── protocol/
│   │   │       ├── input/
│   │   │       └── network/
│   │   │
│   │   └── androidTest/
│   │       └── java/com/weylus/android/
│   │           ├── ui/
│   │           └── integration/
│   │
│   └── build.gradle.kts
│
├── gradle/
├── build.gradle.kts
└── settings.gradle.kts
```

## Core Components

### 1. Network Layer

#### WebSocket Communication

```kotlin
class WeylusWebSocket(
    private val client: OkHttpClient,
    private val messageHandler: MessageHandler
) {
    private var webSocket: WebSocket? = null
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    suspend fun connect(serverUrl: String, accessCode: String? = null) {
        val request = Request.Builder()
            .url(serverUrl)
            .addHeader("Authorization", "Bearer $accessCode")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                _connectionState.value = ConnectionState.Connected
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                messageHandler.handleTextMessage(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                messageHandler.handleBinaryMessage(bytes.toByteArray())
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _connectionState.value = ConnectionState.Error(t.message ?: "Unknown error")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                _connectionState.value = ConnectionState.Disconnecting
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                _connectionState.value = ConnectionState.Disconnected
            }
        })
    }

    fun sendMessage(message: ClientMessage) {
        val json = Json.encodeToString(ClientMessage.serializer(), message)
        webSocket?.send(json)
    }

    fun disconnect() {
        webSocket?.close(1000, "Client disconnecting")
    }
}
```

#### Connection States

```kotlin
sealed class ConnectionState {
    object Disconnected : ConnectionState()
    object Connecting : ConnectionState()
    object Connected : ConnectionState()
    object Disconnecting : ConnectionState()
    data class Error(val message: String) : ConnectionState()
}
```

### 2. Video Pipeline

#### Video Decoder Architecture

```
┌─────────────────────────────────────────────────────┐
│          WebSocket (Binary Frames)                  │
└────────────────────┬────────────────────────────────┘
                     │ H.264 packets
                     ▼
┌─────────────────────────────────────────────────────┐
│            Frame Buffer Manager                      │
│  - Queue incoming frames                            │
│  - Handle buffer overflow                           │
│  - Manage presentation timestamps                   │
└────────────────────┬────────────────────────────────┘
                     │ Buffered frames
                     ▼
┌─────────────────────────────────────────────────────┐
│              MediaCodec Decoder                      │
│  - Hardware-accelerated H.264 decoding              │
│  - Input buffers for compressed data                │
│  - Output buffers for decoded frames                │
└────────────────────┬────────────────────────────────┘
                     │ Decoded frames
                     ▼
┌─────────────────────────────────────────────────────┐
│              Surface Renderer                        │
│  - Direct rendering to Surface                      │
│  - Frame pacing and synchronization                 │
│  - Low latency mode (skip old frames)               │
└─────────────────────────────────────────────────────┘
                     │
                     ▼
                [Display Screen]
```

#### MediaCodec Implementation

```kotlin
class H264Decoder(
    private val surface: Surface,
    private val callback: DecoderCallback
) {
    private var codec: MediaCodec? = null
    private val decoderThread = HandlerThread("H264Decoder").apply { start() }
    private val handler = Handler(decoderThread.looper)

    fun initialize(width: Int, height: Int) {
        val format = MediaFormat.createVideoFormat(
            MediaFormat.MIMETYPE_VIDEO_AVC,
            width,
            height
        ).apply {
            setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 0)
            setInteger(MediaFormat.KEY_LOW_LATENCY, 1)
            setInteger(MediaFormat.KEY_PRIORITY, 0) // Realtime priority
        }

        codec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC).apply {
            setCallback(object : MediaCodec.Callback() {
                override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {
                    callback.onInputBufferAvailable(codec, index)
                }

                override fun onOutputBufferAvailable(
                    codec: MediaCodec,
                    index: Int,
                    info: MediaCodec.BufferInfo
                ) {
                    // Release immediately for rendering
                    codec.releaseOutputBuffer(index, true)
                    callback.onFrameDecoded(info.presentationTimeUs)
                }

                override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {
                    callback.onFormatChanged(format)
                }

                override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
                    callback.onError(e)
                }
            }, handler)

            configure(format, surface, null, 0)
            start()
        }
    }

    fun decodeFrame(data: ByteArray, presentationTimeUs: Long, isKeyFrame: Boolean) {
        // Input buffer will be provided via callback
    }

    fun release() {
        codec?.stop()
        codec?.release()
        codec = null
        decoderThread.quitSafely()
    }
}
```

### 3. Input Handling

#### Input Architecture

```
┌─────────────────────────────────────────────────────┐
│          Compose PointerInput / View Touch          │
│               MotionEvent API                        │
└────────────────────┬────────────────────────────────┘
                     │ Raw motion events
                     ▼
┌─────────────────────────────────────────────────────┐
│              Input Handler                           │
│  - Detect tool type (finger/stylus/eraser)          │
│  - Extract pressure, tilt, orientation              │
│  - Multi-touch tracking                             │
└────────────────────┬────────────────────────────────┘
                     │ Classified events
                     ▼
┌─────────────────────────────────────────────────────┐
│              Input Filters                           │
│  - Palm rejection                                   │
│  - Hover detection                                  │
│  - Pressure curve mapping                           │
└────────────────────┬────────────────────────────────┘
                     │ Filtered events
                     ▼
┌─────────────────────────────────────────────────────┐
│           Coordinate Mapper                          │
│  - Map Android coords to server display coords      │
│  - Handle aspect ratio differences                  │
│  - Apply custom input areas                         │
└────────────────────┬────────────────────────────────┘
                     │ Mapped events
                     ▼
┌─────────────────────────────────────────────────────┐
│         Protocol Message Builder                     │
│  - Convert to PointerEvent message                  │
│  - Serialize to JSON                                │
└────────────────────┬────────────────────────────────┘
                     │ Protocol messages
                     ▼
┌─────────────────────────────────────────────────────┐
│              WebSocket Send                          │
└─────────────────────────────────────────────────────┘
```

#### Stylus Input Handler

```kotlin
class StylusHandler {
    fun handleMotionEvent(event: MotionEvent): List<PointerEvent> {
        return buildList {
            for (i in 0 until event.pointerCount) {
                val toolType = when (event.getToolType(i)) {
                    MotionEvent.TOOL_TYPE_STYLUS -> ToolType.PEN
                    MotionEvent.TOOL_TYPE_ERASER -> ToolType.ERASER
                    MotionEvent.TOOL_TYPE_FINGER -> ToolType.TOUCH
                    else -> ToolType.MOUSE
                }

                // Extract all stylus properties
                val pointerEvent = PointerEvent(
                    pointerId = event.getPointerId(i),
                    x = event.getX(i),
                    y = event.getY(i),
                    pressure = event.getPressure(i),
                    tiltX = event.getAxisValue(MotionEvent.AXIS_TILT, i),
                    tiltY = event.getAxisValue(MotionEvent.AXIS_TILT, i),
                    orientation = event.getOrientation(i),
                    toolType = toolType,
                    eventType = when (event.actionMasked) {
                        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> EventType.DOWN
                        MotionEvent.ACTION_MOVE -> EventType.MOVE
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> EventType.UP
                        MotionEvent.ACTION_CANCEL -> EventType.CANCEL
                        MotionEvent.ACTION_HOVER_ENTER -> EventType.HOVER_ENTER
                        MotionEvent.ACTION_HOVER_MOVE -> EventType.HOVER_MOVE
                        MotionEvent.ACTION_HOVER_EXIT -> EventType.HOVER_EXIT
                        else -> EventType.MOVE
                    },
                    timestamp = event.eventTime,
                    touchMajor = event.getTouchMajor(i),
                    touchMinor = event.getTouchMinor(i),
                    buttons = event.buttonState
                )

                add(pointerEvent)
            }
        }
    }
}
```

### 4. Protocol Layer

#### Message Definitions

```kotlin
@Serializable
sealed class ClientMessage {
    @Serializable
    @SerialName("PointerEvent")
    data class PointerEvent(
        val event_type: String,
        val pointer_id: Int,
        val timestamp: Long,
        val x: Float,
        val y: Float,
        val pressure: Float,
        val tilt_x: Float,
        val tilt_y: Float,
        val tool_type: String,
        val buttons: Int
    ) : ClientMessage()

    @Serializable
    @SerialName("WheelEvent")
    data class WheelEvent(
        val timestamp: Long,
        val delta_x: Float,
        val delta_y: Float
    ) : ClientMessage()

    @Serializable
    @SerialName("KeyboardEvent")
    data class KeyboardEvent(
        val key_code: Int,
        val event_type: String,
        val modifiers: Int
    ) : ClientMessage()

    @Serializable
    @SerialName("Config")
    data class Config(
        val capturable_id: Int,
        val max_width: Int,
        val max_height: Int,
        val client_name: String,
        val frame_rate: Int
    ) : ClientMessage()

    @Serializable
    @SerialName("GetCapturableList")
    object GetCapturableList : ClientMessage()
}

@Serializable
sealed class ServerMessage {
    @Serializable
    @SerialName("CapturableList")
    data class CapturableList(
        val capturables: List<Capturable>
    ) : ServerMessage()

    @Serializable
    @SerialName("NewVideo")
    data class NewVideo(
        val width: Int,
        val height: Int
    ) : ServerMessage()

    @Serializable
    @SerialName("ConfigOk")
    object ConfigOk : ServerMessage()

    @Serializable
    @SerialName("Error")
    data class Error(
        val message: String
    ) : ServerMessage()
}

@Serializable
data class Capturable(
    val id: Int,
    val name: String,
    val capturable_type: String
)
```

## Data Flow

### Connection Flow

```
User launches app
    │
    ├─> Enter/scan server URL
    │
    ├─> ConnectionViewModel.connect()
    │
    ├─> WeylusRepository.connect()
    │
    ├─> WeylusWebSocket.connect()
    │
    ├─> WebSocket established
    │
    ├─> Send GetCapturableList
    │
    ├─> Receive CapturableList
    │
    ├─> User selects capturable
    │
    ├─> Send Config message
    │
    ├─> Receive ConfigOk
    │
    ├─> Receive NewVideo message
    │
    ├─> Initialize VideoDecoder
    │
    └─> Navigate to VideoScreen
```

### Video Streaming Flow

```
Server sends video frame (binary)
    │
    ├─> WeylusWebSocket.onMessage(bytes)
    │
    ├─> MessageHandler.handleBinaryMessage()
    │
    ├─> VideoRepository.onFrameReceived()
    │
    ├─> FrameBuffer.enqueue()
    │
    ├─> H264Decoder.decodeFrame()
    │
    ├─> MediaCodec input buffer
    │
    ├─> Hardware decode
    │
    ├─> MediaCodec output buffer
    │
    ├─> releaseOutputBuffer(render=true)
    │
    └─> Surface renders to screen
```

### Input Flow

```
User touches screen
    │
    ├─> Compose PointerInput / View.onTouchEvent()
    │
    ├─> StylusHandler.handleMotionEvent()
    │
    ├─> PalmRejection.filter()
    │
    ├─> PressureMapper.mapPressure()
    │
    ├─> CoordinateMapper.mapToServerCoords()
    │
    ├─> Build PointerEvent message
    │
    ├─> MessageSerializer.serialize()
    │
    ├─> WeylusWebSocket.send()
    │
    └─> WebSocket transmits to server
```

## State Management

### ViewModel Pattern

```kotlin
@HiltViewModel
class VideoViewModel @Inject constructor(
    private val weylusRepository: WeylusRepository,
    private val videoRepository: VideoRepository
) : ViewModel() {

    private val _videoState = MutableStateFlow<VideoState>(VideoState.Idle)
    val videoState: StateFlow<VideoState> = _videoState.asStateFlow()

    private val _performanceMetrics = MutableStateFlow(PerformanceMetrics())
    val performanceMetrics: StateFlow<PerformanceMetrics> = _performanceMetrics.asStateFlow()

    init {
        viewModelScope.launch {
            videoRepository.videoFrames
                .collect { frame ->
                    updatePerformanceMetrics(frame)
                }
        }

        viewModelScope.launch {
            videoRepository.videoState
                .collect { state ->
                    _videoState.value = state
                }
        }
    }

    fun pauseVideo() {
        viewModelScope.launch {
            weylusRepository.sendMessage(ClientMessage.PauseVideo)
        }
    }

    fun resumeVideo() {
        viewModelScope.launch {
            weylusRepository.sendMessage(ClientMessage.ResumeVideo)
        }
    }
}
```

## Dependency Injection

### Hilt Modules

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS) // No timeout for streaming
            .writeTimeout(10, TimeUnit.SECONDS)
            .pingInterval(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeylusWebSocket(
        client: OkHttpClient,
        messageHandler: MessageHandler
    ): WeylusWebSocket {
        return WeylusWebSocket(client, messageHandler)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object VideoModule {

    @Provides
    fun provideVideoDecoder(
        @ApplicationContext context: Context
    ): VideoDecoder {
        return H264Decoder(context)
    }
}
```

## Threading Model

- **Main Thread**: UI updates, user interactions
- **IO Dispatcher**: Network operations, file I/O
- **Default Dispatcher**: CPU-intensive operations (parsing, serialization)
- **Decoder Thread**: MediaCodec operations (via Handler)
- **WebSocket Thread**: Managed by OkHttp internally

```kotlin
// Example usage
viewModelScope.launch(Dispatchers.IO) {
    // Network operation
    weylusWebSocket.connect(url)

    withContext(Dispatchers.Main) {
        // Update UI
        _connectionState.value = ConnectionState.Connected
    }
}
```

## Performance Considerations

### Low Latency Mode

1. **Minimal Buffering**: Keep buffer to 1-3 frames
2. **Frame Dropping**: Drop old frames if new ones arrive
3. **Direct Rendering**: Use MediaCodec surface rendering
4. **Hardware Acceleration**: Always use hardware decoder
5. **Zero-Copy**: Avoid unnecessary data copies

### Memory Management

1. **Object Pooling**: Reuse byte arrays and objects
2. **Weak References**: For large cached objects
3. **Lifecycle-Aware**: Release resources when not needed
4. **Bitmap Recycling**: Reuse bitmap allocations

### Battery Optimization

1. **Efficient Encoding**: Prefer hardware codecs
2. **Doze Mode**: Handle Android power management
3. **Wake Locks**: Only when actively streaming
4. **Screen On**: Keep screen awake during use

## Security

### Network Security

```xml
<!-- res/xml/network_security_config.xml -->
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">localhost</domain>
        <domain includeSubdomains="true">192.168.0.0/16</domain>
        <domain includeSubdomains="true">10.0.0.0/8</domain>
    </domain-config>
</network-security-config>
```

### Data Protection

1. **Access Codes**: Encrypted storage using EncryptedSharedPreferences
2. **Certificate Pinning**: For production servers
3. **Secure Storage**: Android Keystore for sensitive data

## Error Handling

### Network Errors

- **Connection Lost**: Auto-reconnect with exponential backoff
- **Server Unavailable**: Show user-friendly error
- **Timeout**: Retry with user notification

### Video Errors

- **Decoder Failure**: Reinitialize decoder
- **Format Change**: Reconfigure decoder
- **Buffer Overflow**: Drop old frames

### Input Errors

- **Invalid Coordinates**: Clamp to valid range
- **Disconnected**: Queue events, send when reconnected

---

This architecture provides a solid foundation for building a high-performance, maintainable Android application that integrates seamlessly with the existing Weylus server infrastructure.
