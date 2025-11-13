# Weylus Android - Features

## Overview

This document provides a comprehensive list of features for the Weylus Android native application, comparing it with the web client and SuperDisplay.

---

## Feature Comparison Matrix

| Feature | Web Client | Android Native | SuperDisplay | Notes |
|---------|-----------|----------------|--------------|-------|
| **Core Functionality** |
| Screen mirroring | ✅ | ✅ | ✅ | All support basic mirroring |
| Window capture | ✅ | ✅ | ✅ | Select specific windows |
| Touch input | ✅ | ✅ | ✅ | Basic touch support |
| Multi-touch | ✅ | ✅ | ✅ | Up to 10 points |
| Mouse input | ✅ | ✅ | ✅ | Cursor control |
| Keyboard input | ✅ | ✅ | ✅ | Physical keyboard |
| **Video** |
| H.264 decoding | ✅ (Software) | ✅ (Hardware) | ✅ (Hardware) | Native uses HW acceleration |
| H.265/HEVC | ❌ | 🔶 Planned | ✅ | Better compression |
| 60 FPS | ✅ | ✅ | ✅ | Standard frame rate |
| 90/120 FPS | ❌ | ✅ | ✅ | High refresh rate displays |
| Configurable bitrate | Limited | ✅ (1-50 Mbps) | ✅ | Better quality control |
| Adaptive bitrate | ❌ | ✅ | ✅ | Adjusts to network |
| Low latency mode | ✅ | ✅ Enhanced | ✅ | Minimized buffering |
| **Stylus Support** |
| Pressure sensitivity | ✅ | ✅ Enhanced | ✅ | 0-1 normalized |
| Tilt detection | ✅ | ✅ Enhanced | ✅ | X/Y tilt angles |
| Hover detection | Limited | ✅ | ✅ | Proximity sensing |
| Barrel rotation | ❌ | ✅ | ✅ | S Pen Pro twist |
| Button support | ✅ | ✅ | ✅ | Stylus buttons |
| Pressure curves | ❌ | ✅ | ✅ | Customizable |
| Palm rejection | Basic | ✅ Enhanced | ✅ | Configurable |
| **Display Modes** |
| Mirror mode | ✅ | ✅ | ✅ | Duplicate display |
| Extended display | ❌ | ✅ | ✅ | Virtual monitor |
| Virtual display driver | ❌ | ✅ | ✅ | Windows IDD driver |
| **Connectivity** |
| WiFi | ✅ | ✅ | ✅ | Wireless connection |
| USB | ❌ | ✅ | ✅ | USB tethering |
| Auto-discovery | ❌ | ✅ | ❌ | mDNS/Bonjour |
| QR code connect | Manual | ✅ | ❌ | Scan to connect |
| **User Experience** |
| Native UI | ❌ | ✅ | ✅ | Material Design 3 |
| Dark mode | ✅ | ✅ | ✅ | Theme support |
| Settings persistence | Browser | ✅ DataStore | ✅ | Saved preferences |
| Recent connections | ❌ | ✅ | ✅ | Quick reconnect |
| Background operation | ❌ | ✅ | ✅ | Foreground service |
| Widgets | ❌ | ✅ | ✅ | Quick connect widget |
| Notifications | ❌ | ✅ | ✅ | Status updates |
| **Performance** |
| Latency | ~30-50ms | ~15-30ms | ~15-30ms | Native is faster |
| CPU usage | Medium | Low | Low | HW acceleration |
| Battery efficiency | Low | High | High | Optimized |
| Memory usage | High | Medium | Medium | Browser overhead |
| **Advanced Features** |
| Custom input areas | ✅ | ✅ | ❌ | Map different regions |
| Gestures | Basic | ✅ Enhanced | ✅ | Multi-finger gestures |
| Performance metrics | ❌ | ✅ | ❌ | FPS, latency display |
| Screenshot | ❌ | 🔶 Planned | ✅ | Capture frame |
| Recording | ❌ | 🔶 Future | ✅ | Record session |

**Legend**: ✅ Supported | ❌ Not supported | 🔶 Planned/Future

---

## Detailed Feature Descriptions

### 1. Core Input Features

#### 1.1 Touch Input
- **Multi-touch**: Support up to 10 simultaneous touch points
- **Coordinate mapping**: Accurate mapping between Android screen and PC display
- **Touch gestures**: Tap, double-tap, long-press, drag
- **Scroll gestures**: Two-finger scroll (maps to mouse wheel)
- **Pinch-to-zoom**: (Planned) Maps to Ctrl+scroll

#### 1.2 Stylus Support
- **Pressure sensitivity**: Full pressure range (0.0 to 1.0)
- **Tilt angles**: X and Y axis tilt (-90° to +90°)
- **Hover detection**: Cursor preview before touching screen
- **Barrel rotation**: S Pen Pro twist detection (orientation)
- **Button support**: Primary and secondary stylus buttons
- **Tool detection**: Pen vs eraser automatic switching
- **Pressure curves**: Linear, soft, hard, custom presets
- **Palm rejection**: Filter accidental palm touches
  - Size-based detection
  - Timing-based detection
  - Configurable sensitivity

#### 1.3 Keyboard Input
- **Hardware keyboard**: Full support for physical keyboards
- **Modifier keys**: Ctrl, Alt, Shift, Meta/Windows
- **Special keys**: Function keys, media keys
- **Key repeat**: Standard repeat behavior
- **On-screen keyboard**: Optional (can be disabled for drawing)

---

### 2. Video Streaming Features

#### 2.1 Codecs
- **H.264 (AVC)**: Primary codec, universal support
  - Hardware-accelerated decoding on all devices
  - Baseline, Main, and High profiles
  - Up to 4K resolution support
- **H.265 (HEVC)**: Planned for Phase 2
  - Better compression (50% bitrate reduction)
  - Device support varies
  - Automatic fallback to H.264

#### 2.2 Quality Settings
- **Resolution**: Auto-adjust to device screen, or custom (480p to 4K)
- **Frame rate**:
  - 30 FPS: Battery saving
  - 60 FPS: Standard (default)
  - 90 FPS: High-end devices
  - 120 FPS: Premium devices with high refresh rate
- **Bitrate**: 1-50 Mbps
  - Low (1-5 Mbps): Good for WiFi
  - Medium (5-10 Mbps): Default
  - High (10-20 Mbps): USB or fast WiFi
  - Ultra (20-50 Mbps): USB only, lossless quality
- **Quality presets**:
  - Battery Saver: 720p, 30fps, 3Mbps
  - Balanced: 1080p, 60fps, 8Mbps
  - High Quality: 1080p, 60fps, 15Mbps
  - Maximum: Device native, 120fps, 30Mbps

#### 2.3 Adaptive Streaming
- **Network monitoring**: Track connection quality
- **Automatic adjustment**: Reduce bitrate on poor connection
- **Buffer management**: Minimize latency while preventing stuttering
- **Frame dropping**: Drop old frames to maintain low latency

---

### 3. Display Mode Features

#### 3.1 Mirror Mode
- **Standard operation**: Mirror primary display
- **Window selection**: Mirror specific application window
- **Aspect ratio options**:
  - Fit: Maintain aspect ratio, add letterboxing
  - Fill: Fill screen, may crop
  - Stretch: Fill screen, distort if needed

#### 3.2 Extended Display Mode (Requires Windows driver)
- **Virtual monitor**: Create virtual display on Windows
- **True multi-monitor**: Windows treats Android as second screen
- **Independent content**: Different content than main display
- **Drag and drop**: Move windows to Android device
- **Resolution control**: Set virtual monitor resolution
- **Position**: Configure virtual monitor position (left/right/above/below)
- **Primary/secondary**: Choose if virtual display is primary

**Use cases**:
- Reference materials while working
- Chat/communication apps on tablet
- Video playback on second screen
- Drawing on tablet while viewing on main screen

---

### 4. Connectivity Features

#### 4.1 WiFi Connection
- **Local network**: Standard WiFi LAN connection
- **WiFi Direct**: (Planned) Direct device-to-device
- **Auto-discovery**: mDNS/Bonjour server discovery
- **QR code**: Scan QR code from server for instant setup
- **Manual entry**: Enter IP address and port manually
- **Recent connections**: Save and quickly reconnect

#### 4.2 USB Connection
- **USB tethering**: Use USB network interface
- **Automatic setup**: Enable tethering with one tap
- **Faster speed**: Lower latency than WiFi
- **Stable connection**: No WiFi interference
- **Charging**: Charge device while using

#### 4.3 Connection Management
- **Auto-reconnect**: Reconnect on connection loss
- **Fallback**: WiFi fallback if USB disconnects
- **Seamless switching**: Switch between USB/WiFi automatically
- **Connection indicator**: Show connection type and quality
- **Retry logic**: Exponential backoff for failed connections

---

### 5. User Interface Features

#### 5.1 Connection Screen
- **Server list**: Recent and saved servers
- **Quick connect**: One-tap reconnect
- **QR scanner**: Built-in QR code scanner
- **Server discovery**: Auto-find servers on network
- **Favorites**: Mark frequently used servers
- **Server nicknames**: Custom names for servers

#### 5.2 Video Display Screen
- **Full-screen mode**: Immersive video display
- **Overlay toolbar**: Swipe from edge to show controls
- **Performance overlay**: Toggle FPS, latency, bitrate display
- **Aspect ratio toggle**: Quickly change fit mode
- **Disconnect button**: Easy disconnect
- **Settings shortcut**: Quick access to settings

#### 5.3 Settings Screen
- **Video settings**:
  - Resolution
  - Frame rate
  - Bitrate/quality preset
  - Codec preference (H.264/H.265)
- **Input settings**:
  - Pressure curve selection
  - Palm rejection sensitivity
  - Touch sensitivity
  - Keyboard toggle
- **Display settings**:
  - Aspect ratio mode
  - Display mode (mirror/extend)
- **Network settings**:
  - Connection preference (WiFi/USB/Auto)
  - Buffer size
  - Latency mode
- **General settings**:
  - Theme (Light/Dark/Auto)
  - Language
  - Keep screen on
  - Battery optimization

#### 5.4 Capturable Selection
- **List view**: All available capturables
- **Icons**: Screen vs window icons
- **Search**: Filter capturables
- **Preview**: (Planned) Thumbnail preview
- **Quick switch**: Change capturable without disconnect

---

### 6. Performance Features

#### 6.1 Low Latency Optimization
- **Minimal buffering**: 1-3 frame buffer
- **Frame dropping**: Drop old frames aggressively
- **Direct rendering**: MediaCodec renders directly to Surface
- **Hardware acceleration**: Always use hardware decoder
- **Zero-copy**: Avoid unnecessary data copies
- **Latency target**: < 30ms glass-to-glass

#### 6.2 Battery Optimization
- **Hardware decoding**: Use GPU for video decode
- **Efficient networking**: Minimize wake-ups
- **Doze mode**: Compatible with Android Doze
- **Wake locks**: Only when actively streaming
- **Battery saver mode**: Reduce quality for longer battery
- **Power usage stats**: Show estimated battery time remaining

#### 6.3 Memory Management
- **Object pooling**: Reuse byte arrays
- **Bitmap caching**: Efficient bitmap handling
- **Leak prevention**: Proper lifecycle management
- **Memory monitoring**: Track memory usage
- **Low memory handling**: Graceful degradation

---

### 7. Advanced Features

#### 7.1 Custom Input Areas
- **Multiple regions**: Define different input areas
- **Per-region settings**: Different tool types for areas
- **Visual overlay**: Show area boundaries
- **Quick toggle**: Enable/disable areas
- **Save presets**: Store configurations

#### 7.2 Gestures
- **Edge swipe**: Show/hide toolbar
- **Two-finger scroll**: Mouse wheel emulation
- **Pinch-to-zoom**: (Planned) Ctrl+scroll emulation
- **Three-finger tap**: (Planned) Custom action
- **Custom gestures**: User-defined shortcuts

#### 7.3 Accessibility
- **Screen reader**: TalkBack support
- **High contrast**: High contrast mode
- **Large text**: Respect system text size
- **Button labels**: Clear content descriptions
- **Keyboard navigation**: Full keyboard navigation

#### 7.4 Monitoring & Debugging
- **Performance overlay**:
  - Current FPS
  - Average FPS
  - Latency (input and video)
  - Bitrate
  - Dropped frames
  - Network status
- **Connection log**: View connection events
- **Debug mode**: Detailed logging for troubleshooting

---

### 8. Future Features (Post-Release)

#### 8.1 Cloud Relay (Future)
- **Remote access**: Connect over internet
- **Relay server**: Cloud-based relay for NAT traversal
- **End-to-end encryption**: Secure remote connection
- **Subscription model**: Freemium or paid feature

#### 8.2 Multi-Monitor Support (Future)
- **Multiple devices**: Control multiple PCs
- **Synchronized input**: Control multiple screens at once
- **Quick switch**: Toggle between PCs

#### 8.3 Recording & Capture (Future)
- **Video recording**: Record screen session
- **Screenshot**: Capture current frame
- **Annotation**: Draw on screenshots
- **Share**: Quick share screenshots

#### 8.4 Macros & Shortcuts (Future)
- **Custom shortcuts**: Define keyboard shortcuts
- **Macros**: Record and playback actions
- **Button mapping**: Map stylus buttons to shortcuts
- **Profiles**: Different profiles for different apps

#### 8.5 Bluetooth Input Device Mode (Future)
- **HID profile**: Act as Bluetooth input device
- **Direct connection**: No server needed
- **Lower latency**: Direct HID connection
- **Compatibility**: Works with any device supporting Bluetooth HID

---

## Feature Priorities

### Phase 1 (MVP - 8-10 weeks)
**Must Have**:
- Core input (touch, stylus, keyboard)
- Video streaming (H.264, 60fps)
- WiFi connectivity
- Basic UI
- Settings persistence

### Phase 2 (SuperDisplay Features - 8-10 weeks)
**Should Have**:
- Virtual display driver
- Extended display mode
- USB connectivity
- High refresh rate (90/120fps)
- Enhanced stylus features
- Adaptive bitrate

### Phase 3 (Polish - 4-5 weeks)
**Nice to Have**:
- Performance optimizations
- Advanced gestures
- Widgets
- Accessibility
- Localization

### Future (Post-Release)
**Could Have**:
- Recording/screenshot
- Macros
- Cloud relay
- Bluetooth mode
- iOS client

---

## Success Criteria

A feature is considered successful if:

1. **Functional**: Works as designed on 95%+ of target devices
2. **Performant**: Meets latency/FPS targets
3. **Reliable**: < 1% crash rate
4. **Usable**: Positive user feedback
5. **Maintainable**: Well-documented and tested

---

## User Stories

### Story 1: Digital Artist
"As a digital artist, I want to use my Samsung Galaxy Tab with S Pen as a drawing tablet for my Windows PC running Photoshop, so I can draw with pressure and tilt sensitivity."

**Required Features**:
- Stylus pressure (✅)
- Stylus tilt (✅)
- Low latency (✅)
- High refresh rate (✅)
- Palm rejection (✅)

### Story 2: Remote Worker
"As a remote worker, I want to use my Android tablet as a second monitor for my laptop when traveling, so I can be more productive on the go."

**Required Features**:
- Extended display mode (✅)
- Virtual display driver (✅)
- USB connection (✅)
- Battery efficient (✅)

### Story 3: Gamer
"As a gamer, I want to stream my gaming PC to my Android device in bed, so I can play games remotely with minimal input lag."

**Required Features**:
- Low latency mode (✅)
- High frame rate (✅)
- Controller input support (🔶 Future)

### Story 4: Presenter
"As a presenter, I want to control my presentation slides from my Android phone, so I can move around the room while presenting."

**Required Features**:
- Touch input (✅)
- WiFi connection (✅)
- Simple UI (✅)

---

This feature set positions Weylus Android as a comprehensive solution that matches or exceeds both the web client and SuperDisplay while remaining free and open source.
