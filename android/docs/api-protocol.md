# Weylus Android - API & Protocol Specification

## Overview

This document specifies the WebSocket protocol used for communication between the Android client and the Weylus server. The protocol is based on the existing Weylus web client protocol with extensions for Android-specific features.

---

## Connection

### WebSocket URL Format

```
ws://[server-host]:[port]
```

**Default Port**: 1701

**Example**:
```
ws://192.168.1.100:1701
ws://localhost:1701
```

### Authentication

If the server has access code protection enabled, include in HTTP headers:

```http
Authorization: Bearer [access-code]
```

### Connection Flow

```
Client                          Server
  │                               │
  │──── HTTP Upgrade ────────────>│
  │<──── 101 Switching Protocols ─│
  │                               │
  │──── GetCapturableList ───────>│
  │<──── CapturableList ──────────│
  │                               │
  │──── Config ──────────────────>│
  │<──── ConfigOk/ConfigError ────│
  │                               │
  │<──── NewVideo ────────────────│
  │<──── Video Frames (binary) ───│
  │──── PointerEvent ────────────>│
  │──── KeyboardEvent ───────────>│
  │                               │
```

---

## Message Format

### Text Messages (JSON)

All control messages are sent as JSON text frames.

**Structure**:
```json
{
  "MessageType": {
    "field1": value1,
    "field2": value2
  }
}
```

### Binary Messages

Video frames are sent as binary WebSocket frames containing H.264 encoded data in fragmented MP4 (fMP4) format.

---

## Client Messages

Messages sent from Android client to server.

### 1. GetCapturableList

Request list of available windows and screens to capture.

**Format**:
```json
"GetCapturableList"
```

**Example**:
```json
"GetCapturableList"
```

**Response**: CapturableList

---

### 2. Config

Configure video stream and select what to capture.

**Format**:
```json
{
  "Config": {
    "capture_id": number,
    "max_width": number,
    "max_height": number,
    "client_name": string,
    "frame_rate": number
  }
}
```

**Fields**:
- `capture_id`: ID of the capturable (from CapturableList)
- `max_width`: Maximum width in pixels (e.g., 1920)
- `max_height`: Maximum height in pixels (e.g., 1080)
- `client_name`: Client identifier (e.g., "Weylus Android/1.0")
- `frame_rate`: Desired frame rate (0-120, 0 = unlimited)

**Example**:
```json
{
  "Config": {
    "capture_id": 1,
    "max_width": 1920,
    "max_height": 1080,
    "client_name": "Weylus Android/1.0",
    "frame_rate": 60
  }
}
```

**Response**: ConfigOk or ConfigError

---

### 3. PointerEvent

Send touch, stylus, or mouse input events.

**Format**:
```json
{
  "PointerEvent": {
    "event_type": string,
    "pointer_id": number,
    "timestamp": number,
    "x": number,
    "y": number,
    "pressure": number,
    "tilt_x": number,
    "tilt_y": number,
    "tool_type": string,
    "buttons": number
  }
}
```

**Fields**:
- `event_type`: "DOWN" | "MOVE" | "UP" | "CANCEL"
- `pointer_id`: Unique ID for this pointer (0-9 for multi-touch)
- `timestamp`: Event timestamp in milliseconds
- `x`: X coordinate (0.0 to width)
- `y`: Y coordinate (0.0 to height)
- `pressure`: Pressure (0.0 to 1.0)
- `tilt_x`: Tilt angle X in degrees (-90 to 90)
- `tilt_y`: Tilt angle Y in degrees (-90 to 90)
- `tool_type`: "TOUCH" | "PEN" | "ERASER" | "MOUSE"
- `buttons`: Bitmask of pressed buttons

**Example**:
```json
{
  "PointerEvent": {
    "event_type": "DOWN",
    "pointer_id": 0,
    "timestamp": 1698765432100,
    "x": 500.5,
    "y": 300.2,
    "pressure": 0.75,
    "tilt_x": 15.0,
    "tilt_y": -10.0,
    "tool_type": "PEN",
    "buttons": 0
  }
}
```

**Button Bitmask**:
- Bit 0 (1): Primary button (left mouse button)
- Bit 1 (2): Secondary button (right mouse button)
- Bit 2 (4): Tertiary button (middle mouse button)
- Bit 3 (8): Stylus button 1
- Bit 4 (16): Stylus button 2

---

### 4. WheelEvent

Send scroll/wheel events.

**Format**:
```json
{
  "WheelEvent": {
    "timestamp": number,
    "x": number,
    "y": number,
    "delta_x": number,
    "delta_y": number
  }
}
```

**Fields**:
- `timestamp`: Event timestamp in milliseconds
- `x`: X coordinate where scroll occurred
- `y`: Y coordinate where scroll occurred
- `delta_x`: Horizontal scroll amount
- `delta_y`: Vertical scroll amount (positive = down, negative = up)

**Example**:
```json
{
  "WheelEvent": {
    "timestamp": 1698765432100,
    "x": 500.0,
    "y": 300.0,
    "delta_x": 0.0,
    "delta_y": 120.0
  }
}
```

---

### 5. KeyboardEvent

Send keyboard input events.

**Format**:
```json
{
  "KeyboardEvent": {
    "key_code": number,
    "event_type": string,
    "modifiers": number
  }
}
```

**Fields**:
- `key_code`: Key code (Android KeyEvent keyCode)
- `event_type`: "DOWN" | "UP"
- `modifiers`: Bitmask of modifier keys

**Example**:
```json
{
  "KeyboardEvent": {
    "key_code": 29,
    "event_type": "DOWN",
    "modifiers": 1
  }
}
```

**Modifier Bitmask**:
- Bit 0 (1): Ctrl
- Bit 1 (2): Alt
- Bit 2 (4): Shift
- Bit 3 (8): Meta/Windows key

---

### 6. PauseVideo

Request server to pause video streaming.

**Format**:
```json
"PauseVideo"
```

**Example**:
```json
"PauseVideo"
```

---

### 7. ResumeVideo

Request server to resume video streaming.

**Format**:
```json
"ResumeVideo"
```

**Example**:
```json
"ResumeVideo"
```

---

### 8. RestartVideo

Request server to restart video stream.

**Format**:
```json
"RestartVideo"
```

**Example**:
```json
"RestartVideo"
```

---

## Server Messages

Messages sent from server to Android client.

### 1. CapturableList

List of available windows and screens.

**Format**:
```json
{
  "CapturableList": {
    "capturables": [
      {
        "id": number,
        "name": string,
        "capturable_type": string
      }
    ]
  }
}
```

**Fields**:
- `id`: Unique ID for this capturable
- `name`: Display name (e.g., "Screen 1", "Google Chrome")
- `capturable_type`: "Screen" | "Window"

**Example**:
```json
{
  "CapturableList": {
    "capturables": [
      {
        "id": 0,
        "name": "Screen 1",
        "capturable_type": "Screen"
      },
      {
        "id": 1,
        "name": "Google Chrome",
        "capturable_type": "Window"
      }
    ]
  }
}
```

---

### 2. NewVideo

Notification that new video stream is starting.

**Format**:
```json
{
  "NewVideo": {
    "width": number,
    "height": number
  }
}
```

**Fields**:
- `width`: Video width in pixels
- `height`: Video height in pixels

**Example**:
```json
{
  "NewVideo": {
    "width": 1920,
    "height": 1080
  }
}
```

**Client Action**: Initialize/reconfigure video decoder with new dimensions.

---

### 3. ConfigOk

Acknowledgment that configuration was accepted.

**Format**:
```json
"ConfigOk"
```

**Example**:
```json
"ConfigOk"
```

---

### 4. ConfigError

Configuration was rejected.

**Format**:
```json
{
  "ConfigError": {
    "msg": string
  }
}
```

**Fields**:
- `msg`: Error message describing why config was rejected

**Example**:
```json
{
  "ConfigError": {
    "msg": "Invalid capturable ID"
  }
}
```

---

### 5. Error

General error message.

**Format**:
```json
{
  "Error": {
    "msg": string
  }
}
```

**Fields**:
- `msg`: Error description

**Example**:
```json
{
  "Error": {
    "msg": "Screen capture failed"
  }
}
```

---

### 6. Video Frames (Binary)

H.264 encoded video frames in fragmented MP4 format.

**Format**: Raw binary WebSocket frames

**Structure**:
- MPEG-4 container (fMP4)
- H.264 (AVC) codec
- Fragmented for streaming
- Each fragment contains one or more frames

**Client Action**: Feed directly to MediaCodec decoder.

---

## Protocol Extensions (Android-Specific)

### Enhanced PointerEvent (Proposed)

Extended pointer event with additional Android capabilities.

**Format**:
```json
{
  "PointerEventV2": {
    "event_type": string,
    "pointer_id": number,
    "timestamp": number,
    "x": number,
    "y": number,
    "pressure": number,
    "tilt_x": number,
    "tilt_y": number,
    "orientation": number,
    "tool_type": string,
    "buttons": number,
    "hover_distance": number,
    "touch_major": number,
    "touch_minor": number
  }
}
```

**New Fields**:
- `orientation`: Stylus barrel rotation (0 to 2π radians)
- `hover_distance`: Distance from screen when hovering (normalized 0-1)
- `touch_major`: Major axis of touch ellipse (pixels)
- `touch_minor`: Minor axis of touch ellipse (pixels)

**Use Cases**:
- `orientation`: For stylus twist-sensitive brushes
- `hover_distance`: For cursor preview
- `touch_major/minor`: For palm rejection

---

### DisplayMode (Proposed)

Request display mode (mirror vs extend).

**Client → Server**:
```json
{
  "RequestDisplayMode": {
    "mode": string,
    "virtual_monitor_id": number
  }
}
```

**Fields**:
- `mode`: "Mirror" | "Extend"
- `virtual_monitor_id`: Optional, ID of virtual monitor (for Extend mode)

**Server → Client**:
```json
{
  "DisplayModeStatus": {
    "current_mode": string,
    "available_modes": [string],
    "virtual_monitor_id": number
  }
}
```

**Fields**:
- `current_mode`: Currently active mode
- `available_modes`: List of supported modes
- `virtual_monitor_id`: ID of virtual monitor if in Extend mode

---

### VideoConfig (Proposed)

Enhanced video configuration.

**Format**:
```json
{
  "VideoConfig": {
    "bitrate_kbps": number,
    "codec": string,
    "quality_preset": string
  }
}
```

**Fields**:
- `bitrate_kbps`: Target bitrate in Kbps (1000-50000)
- `codec`: "H264" | "H265"
- `quality_preset`: "Low" | "Medium" | "High" | "Ultra"

---

## Error Handling

### Connection Errors

| Error | Cause | Recovery |
|-------|-------|----------|
| Connection Refused | Server not running | Retry with backoff |
| Authentication Failed | Invalid access code | Prompt user |
| Timeout | Network issue | Retry connection |
| WebSocket Closed | Server disconnect | Auto-reconnect |

### Protocol Errors

| Error | Cause | Recovery |
|-------|-------|----------|
| Invalid Message | Malformed JSON | Log and ignore |
| Unknown Message Type | Version mismatch | Warn user |
| ConfigError | Bad configuration | Show error, retry |

### Video Errors

| Error | Cause | Recovery |
|-------|-------|----------|
| Decoder Failure | Codec issue | Reinitialize decoder |
| Format Change | Resolution change | Reconfigure decoder |
| Frame Drop | Buffer overflow | Increase buffer or drop frames |

---

## Best Practices

### 1. Connection Management

- Implement exponential backoff for reconnection
- Maximum 5 retry attempts
- Show connection status to user
- Handle network changes gracefully

```kotlin
suspend fun connectWithRetry(url: String, maxRetries: Int = 5) {
    var attempt = 0
    while (attempt < maxRetries) {
        try {
            webSocket.connect(url)
            return
        } catch (e: Exception) {
            val delay = (2.0.pow(attempt) * 1000).toLong()
            delay(delay)
            attempt++
        }
    }
    throw ConnectionException("Failed after $maxRetries attempts")
}
```

### 2. Message Sending

- Queue messages if disconnected
- Send in order
- Don't block on send
- Batch input events if needed

```kotlin
private val messageQueue = Channel<ClientMessage>(Channel.UNLIMITED)

launch {
    messageQueue.consumeAsFlow()
        .collect { message ->
            webSocket.send(message)
        }
}
```

### 3. Video Frame Handling

- Process frames immediately
- Drop old frames if decoder falls behind
- Monitor buffer size
- Track performance metrics

```kotlin
private val frameBuffer = Channel<ByteArray>(capacity = 3)

launch {
    frameBuffer.consumeAsFlow()
        .collect { frame ->
            if (decoder.isReady()) {
                decoder.decode(frame)
            } else {
                // Drop frame, decoder is busy
                droppedFrames++
            }
        }
}
```

### 4. Input Event Batching

For high-frequency events (MOVE), consider batching:

```kotlin
private val inputEvents = MutableSharedFlow<PointerEvent>()

launch {
    inputEvents
        .buffer(10) // Buffer up to 10 events
        .chunked(5, 16) // Send every 5 events or 16ms
        .collect { batch ->
            batch.forEach { event ->
                webSocket.send(event)
            }
        }
}
```

---

## Protocol Versioning

### Current Version: 1.0

**Version Negotiation** (Future):
```json
{
  "ClientHello": {
    "protocol_version": "1.1",
    "client_name": "Weylus Android",
    "capabilities": ["display_mode", "enhanced_pointer", "high_bitrate"]
  }
}
```

```json
{
  "ServerHello": {
    "protocol_version": "1.1",
    "server_name": "Weylus Server",
    "capabilities": ["virtual_display", "h265", "high_refresh_rate"]
  }
}
```

---

## Security Considerations

### 1. Authentication

- Always use access codes in production
- Store access codes securely (EncryptedSharedPreferences)
- Never log access codes

### 2. Network Security

- Allow cleartext traffic only for local networks
- Support HTTPS/WSS for remote connections
- Validate server certificates
- Consider certificate pinning

### 3. Input Validation

- Validate all incoming messages
- Clamp coordinates to valid ranges
- Sanitize strings
- Handle unexpected message types gracefully

```kotlin
fun validatePointerEvent(event: PointerEvent): Boolean {
    return event.x in 0f..displayWidth &&
           event.y in 0f..displayHeight &&
           event.pressure in 0f..1f &&
           event.tilt_x in -90f..90f &&
           event.tilt_y in -90f..90f
}
```

---

## Testing

### Protocol Tests

```kotlin
@Test
fun `serialize and deserialize PointerEvent`() {
    val event = PointerEvent(
        eventType = "DOWN",
        pointerId = 0,
        timestamp = 123456789,
        x = 100f,
        y = 200f,
        pressure = 0.5f,
        tiltX = 10f,
        tiltY = -5f,
        toolType = "PEN",
        buttons = 0
    )

    val json = Json.encodeToString(event)
    val decoded = Json.decodeFromString<PointerEvent>(json)

    assertEquals(event, decoded)
}

@Test
fun `handle malformed JSON gracefully`() {
    val badJson = "{ invalid json }"

    assertDoesNotThrow {
        messageHandler.handleMessage(badJson)
    }
}
```

### Integration Tests

```kotlin
@Test
fun `full connection flow`() = runTest {
    val server = MockWebSocketServer()
    server.start()

    client.connect(server.url)
    client.send(ClientMessage.GetCapturableList)

    val response = server.receiveMessage()
    assertIs<ServerMessage.CapturableList>(response)
}
```

---

## Reference Implementation

See:
- Server protocol: `/home/user/Weylus/src/protocol.rs`
- Web client: `/home/user/Weylus/ts/lib.ts`
- Android client (TBD): `android/app/src/main/java/com/weylus/android/protocol/`

---

This protocol specification ensures compatibility with existing Weylus servers while providing a path for Android-specific enhancements.
