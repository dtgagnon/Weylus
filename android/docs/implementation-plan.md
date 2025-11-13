# Weylus Android - Implementation Plan

## Overview

This document details the phased implementation plan for the Weylus Android native application, including specific tasks, dependencies, and deliverables for each phase.

---

## Phase 1: Core Android Client (8-10 weeks)

**Goal**: Achieve feature parity with the web-based Weylus client

### Week 1-2: Project Setup & Foundation

#### Tasks

1. **Project Initialization**
   - [ ] Create Android Studio project
   - [ ] Configure Gradle with Kotlin DSL
   - [ ] Set up Git repository structure
   - [ ] Configure version catalogs for dependencies
   - [ ] Set up CI/CD pipeline (GitHub Actions)

2. **Architecture Setup**
   - [ ] Implement MVVM structure
   - [ ] Configure Hilt for dependency injection
   - [ ] Set up navigation with Jetpack Compose
   - [ ] Create base ViewModel and Repository classes
   - [ ] Set up Kotlin Coroutines and Flow

3. **UI Foundation**
   - [ ] Create Material 3 theme
   - [ ] Set up color scheme (light/dark modes)
   - [ ] Create reusable UI components
   - [ ] Implement navigation graph
   - [ ] Create splash screen

4. **Configuration**
   - [ ] Set up DataStore for preferences
   - [ ] Create settings data models
   - [ ] Implement configuration manager
   - [ ] Set up logging framework

**Deliverables**:
- Empty Android project with full architecture
- Basic UI scaffold
- CI/CD pipeline functional

---

### Week 3: Network Layer

#### Tasks

1. **WebSocket Implementation**
   - [ ] Add OkHttp dependency
   - [ ] Create WeylusWebSocket class
   - [ ] Implement WebSocketListener
   - [ ] Create ConnectionState flow
   - [ ] Implement auto-reconnect logic
   - [ ] Add connection timeout handling

2. **Message Protocol**
   - [ ] Define Kotlin data classes for all messages
   - [ ] Implement kotlinx.serialization
   - [ ] Create message serializers/deserializers
   - [ ] Handle JSON messages
   - [ ] Handle binary video frames
   - [ ] Create protocol version negotiation

3. **HTTP Client**
   - [ ] Create HTTP client for initial connection
   - [ ] Implement QR code scanning (ZXing)
   - [ ] Parse server URL and access code
   - [ ] Handle SSL/TLS certificates

4. **Testing**
   - [ ] Unit tests for message serialization
   - [ ] Mock WebSocket tests
   - [ ] Connection state machine tests

**Deliverables**:
- Functional WebSocket communication
- Message protocol implementation
- Connection management

---

### Week 4-6: Video Decoding & Rendering

#### Tasks

1. **MediaCodec Setup** (Week 4)
   - [ ] Create H264Decoder class
   - [ ] Initialize MediaCodec with H.264 format
   - [ ] Configure hardware acceleration
   - [ ] Set up MediaCodec callbacks
   - [ ] Implement error handling
   - [ ] Add decoder restart logic

2. **Frame Buffer Management** (Week 4)
   - [ ] Create FrameBuffer class
   - [ ] Implement frame queue
   - [ ] Handle buffer overflow
   - [ ] Implement frame dropping for low latency
   - [ ] Track presentation timestamps

3. **Video Rendering** (Week 5)
   - [ ] Create SurfaceView for rendering
   - [ ] Connect MediaCodec to Surface
   - [ ] Implement frame pacing
   - [ ] Add vsync synchronization
   - [ ] Handle surface lifecycle
   - [ ] Support aspect ratio modes (fit, fill, stretch)

4. **Video Stream Management** (Week 5-6)
   - [ ] Handle NewVideo message
   - [ ] Dynamic resolution changes
   - [ ] Handle video restart
   - [ ] Pause/resume functionality
   - [ ] Track video statistics (FPS, bitrate, dropped frames)

5. **UI Integration** (Week 6)
   - [ ] Create VideoDisplayScreen composable
   - [ ] Show performance overlay
   - [ ] Display connection status
   - [ ] Add loading indicators
   - [ ] Handle orientation changes

6. **Testing**
   - [ ] Unit tests for frame buffer
   - [ ] Integration tests with mock video data
   - [ ] Performance tests

**Deliverables**:
- Working video decoder
- Smooth video playback
- Performance metrics

---

### Week 7-8: Input Handling

#### Tasks

1. **Touch Input** (Week 7)
   - [ ] Create TouchHandler class
   - [ ] Capture MotionEvent in Compose
   - [ ] Extract touch coordinates
   - [ ] Handle multi-touch
   - [ ] Map to server coordinates
   - [ ] Send PointerEvent messages

2. **Stylus Input** (Week 7)
   - [ ] Create StylusHandler class
   - [ ] Detect stylus tool type
   - [ ] Extract pressure values
   - [ ] Extract tilt angles (X/Y)
   - [ ] Extract orientation/twist
   - [ ] Detect stylus buttons
   - [ ] Handle hover events

3. **Input Mapping** (Week 7)
   - [ ] Create CoordinateMapper
   - [ ] Map Android coords to server display
   - [ ] Handle aspect ratio differences
   - [ ] Support custom input areas
   - [ ] Implement pressure curve mapping

4. **Input Filtering** (Week 8)
   - [ ] Implement palm rejection
   - [ ] Filter spurious events
   - [ ] Debounce rapid events
   - [ ] Handle edge cases

5. **Keyboard Input** (Week 8)
   - [ ] Create KeyboardHandler
   - [ ] Capture hardware keyboard events
   - [ ] Handle modifier keys
   - [ ] Send KeyboardEvent messages
   - [ ] Optional on-screen keyboard

6. **Wheel/Scroll Events** (Week 8)
   - [ ] Capture scroll gestures
   - [ ] Convert to wheel events
   - [ ] Send WheelEvent messages

7. **Testing**
   - [ ] Unit tests for coordinate mapping
   - [ ] Unit tests for pressure mapping
   - [ ] Integration tests with mock server
   - [ ] Test with different devices

**Deliverables**:
- Full input handling
- Stylus support with pressure/tilt
- Keyboard and scroll support

---

### Week 9-10: User Interface

#### Tasks

1. **Connection Screen** (Week 9)
   - [ ] Create ConnectionScreen composable
   - [ ] Server URL input field
   - [ ] QR code scanner button
   - [ ] Access code input
   - [ ] Recent connections list
   - [ ] Auto-discovery UI (mDNS)
   - [ ] Connection button
   - [ ] Error messages

2. **Capturable Selection Screen** (Week 9)
   - [ ] Create CapturableListScreen
   - [ ] Display list of windows/screens
   - [ ] Show capturable icons
   - [ ] Quick selection
   - [ ] Refresh button

3. **Settings Screen** (Week 10)
   - [ ] Create SettingsScreen
   - [ ] Video quality settings (resolution, FPS, bitrate)
   - [ ] Input settings (pressure curve, palm rejection)
   - [ ] Network settings (buffer size, latency mode)
   - [ ] Display settings (aspect ratio mode)
   - [ ] About section

4. **Video Screen Enhancements** (Week 10)
   - [ ] Full-screen video mode
   - [ ] Gesture toolbar (edge swipe)
   - [ ] Quick settings overlay
   - [ ] Performance metrics toggle
   - [ ] Screenshot button
   - [ ] Disconnect button

5. **Navigation** (Week 10)
   - [ ] Implement navigation flow
   - [ ] Handle back navigation
   - [ ] Deep linking support
   - [ ] Handle process death

6. **Polish** (Week 10)
   - [ ] Add animations
   - [ ] Improve error messages
   - [ ] Add help tooltips
   - [ ] Implement empty states
   - [ ] Add loading states

**Deliverables**:
- Complete UI for all screens
- Smooth navigation
- Polished user experience

---

## Phase 2: SuperDisplay Features (8-10 weeks)

**Goal**: Add advanced features from SuperDisplay to differentiate from web client

### Week 11-14: Virtual Display Driver (Windows)

#### Tasks

1. **Research & Design** (Week 11)
   - [ ] Research Windows IDD framework
   - [ ] Study existing virtual display drivers
   - [ ] Design driver architecture
   - [ ] Plan integration with Weylus server
   - [ ] Document driver protocol

2. **Driver Development** (Week 11-13)
   - [ ] Set up Windows Driver Kit (WDK)
   - [ ] Create IDD driver project
   - [ ] Implement driver initialization
   - [ ] Implement monitor creation
   - [ ] Implement frame callback
   - [ ] Implement EDID generation
   - [ ] Handle resolution changes
   - [ ] Add driver signing

3. **Installer** (Week 13)
   - [ ] Create driver installer
   - [ ] Handle driver installation
   - [ ] Handle driver updates
   - [ ] Create uninstaller
   - [ ] Add installation UI

4. **Server Integration** (Week 13-14)
   - [ ] Modify Weylus server to detect virtual display
   - [ ] Add virtual display capture mode
   - [ ] Implement frame routing to driver
   - [ ] Add display mode protocol messages
   - [ ] Test integration

5. **Android Client Updates** (Week 14)
   - [ ] Add display mode selection UI
   - [ ] Send display mode requests
   - [ ] Handle virtual display responses
   - [ ] Update coordinate mapping for extended mode

6. **Testing** (Week 14)
   - [ ] Test driver installation
   - [ ] Test with different Windows versions
   - [ ] Test resolution changes
   - [ ] Test with multiple monitors
   - [ ] Performance testing

**Deliverables**:
- Working Windows virtual display driver
- Weylus server with virtual display support
- Android client with display mode selection

**Note**: This is the most complex feature and may require additional time.

---

### Week 15-16: Extended Display Mode

#### Tasks

1. **Protocol Extension** (Week 15)
   - [ ] Define DisplayMode enum (Mirror, Extend)
   - [ ] Create RequestDisplayMode message
   - [ ] Create DisplayModeStatus message
   - [ ] Update server to handle display modes
   - [ ] Update Android client protocol

2. **Server Implementation** (Week 15)
   - [ ] Add display mode state management
   - [ ] Implement mode switching
   - [ ] Handle virtual monitor selection
   - [ ] Update capture to match mode
   - [ ] Send mode status to client

3. **Android Client Implementation** (Week 16)
   - [ ] Add display mode selector UI
   - [ ] Send mode change requests
   - [ ] Handle mode change responses
   - [ ] Update coordinate mapping for extend mode
   - [ ] Update input handling for extend mode
   - [ ] Show current mode indicator

4. **Testing** (Week 16)
   - [ ] Test mode switching
   - [ ] Test with different resolutions
   - [ ] Test input accuracy in extend mode
   - [ ] Test edge cases

**Deliverables**:
- Functional extended display mode
- Seamless mode switching

---

### Week 17-19: USB Connectivity

#### Tasks

1. **Research & Design** (Week 17)
   - [ ] Research USB tethering on Android
   - [ ] Research USB Accessory mode (alternative)
   - [ ] Choose implementation approach
   - [ ] Design USB connection flow
   - [ ] Document USB protocol

2. **USB Tethering Implementation** (Week 17-18)
   - [ ] Request USB tethering permission
   - [ ] Enable USB tethering programmatically
   - [ ] Detect USB network interface
   - [ ] Auto-detect server on USB network
   - [ ] Connect via USB IP address
   - [ ] Handle USB disconnection

3. **Server Updates** (Week 18)
   - [ ] Listen on USB network interface
   - [ ] Auto-detect USB connections
   - [ ] Optimize for USB bandwidth
   - [ ] Add USB connection indicator

4. **Android Client UI** (Week 18-19)
   - [ ] Add USB connection option
   - [ ] Show USB connection status
   - [ ] Handle USB permission requests
   - [ ] Show USB setup instructions
   - [ ] Auto-connect when USB plugged

5. **Fallback & Auto-Switch** (Week 19)
   - [ ] Implement WiFi fallback
   - [ ] Auto-switch between USB and WiFi
   - [ ] Seamless reconnection
   - [ ] Connection preference settings

6. **Testing** (Week 19)
   - [ ] Test USB connection setup
   - [ ] Test USB performance
   - [ ] Test disconnection handling
   - [ ] Test fallback to WiFi
   - [ ] Test with different Android devices

**Deliverables**:
- Working USB connectivity
- Automatic connection management
- WiFi fallback

---

### Week 20-21: Enhanced Video Quality

#### Tasks

1. **High Bitrate Support** (Week 20)
   - [ ] Add bitrate configuration to protocol
   - [ ] Update server encoding settings
   - [ ] Add bitrate slider in Android settings
   - [ ] Implement quality presets (Low/Medium/High/Ultra)
   - [ ] Test with different bitrates

2. **High Refresh Rate** (Week 20-21)
   - [ ] Detect device refresh rate
   - [ ] Request high frame rate from server
   - [ ] Implement frame pacing for 90/120Hz
   - [ ] Add refresh rate settings
   - [ ] Optimize for high FPS

3. **Adaptive Bitrate** (Week 21)
   - [ ] Monitor connection quality
   - [ ] Implement bitrate adaptation algorithm
   - [ ] Request bitrate changes from server
   - [ ] Show quality indicator
   - [ ] Add adaptive mode toggle

4. **Advanced Codec Support** (Week 21)
   - [ ] Research HEVC/H.265 support
   - [ ] Add codec negotiation
   - [ ] Implement H.265 decoder
   - [ ] Fallback to H.264
   - [ ] Performance comparison

5. **Testing**
   - [ ] Test different bitrates
   - [ ] Test high refresh rates
   - [ ] Test adaptive bitrate
   - [ ] Benchmark performance

**Deliverables**:
- Configurable bitrate up to 50 Mbps
- Up to 120Hz refresh rate support
- Adaptive bitrate streaming

---

### Week 22: Advanced Stylus Features

#### Tasks

1. **Hover Detection** (Week 22)
   - [ ] Detect hover events (ACTION_HOVER_*)
   - [ ] Send hover position to server
   - [ ] Add hover distance to protocol
   - [ ] Test with S Pen and other styluses

2. **Enhanced Tilt & Twist** (Week 22)
   - [ ] Extract azimuth angle
   - [ ] Extract altitude angle
   - [ ] Extract barrel rotation
   - [ ] Convert to server format
   - [ ] Update protocol for enhanced data

3. **Pressure Curve Customization** (Week 22)
   - [ ] Add pressure curve UI
   - [ ] Implement curve presets
   - [ ] Allow custom curve editing
   - [ ] Apply curve to input
   - [ ] Save curve preferences

4. **Palm Rejection Tuning** (Week 22)
   - [ ] Improve palm detection algorithm
   - [ ] Add sensitivity settings
   - [ ] Test with different hand sizes
   - [ ] Add palm rejection toggle

5. **Testing**
   - [ ] Test with Samsung S Pen
   - [ ] Test with USI stylus
   - [ ] Test with third-party styluses
   - [ ] Test pressure curves
   - [ ] Test palm rejection

**Deliverables**:
- Advanced stylus support
- Customizable pressure curves
- Improved palm rejection

---

## Phase 3: Polish & Optimization (4-5 weeks)

**Goal**: Optimize performance and enhance user experience

### Week 23-24: Performance Optimizations

#### Tasks

1. **Low Latency Mode** (Week 23)
   - [ ] Minimize buffer sizes
   - [ ] Implement aggressive frame dropping
   - [ ] Direct rendering without composition
   - [ ] Optimize decoder configuration
   - [ ] Measure and display latency
   - [ ] Compare with web client

2. **Memory Optimization** (Week 23)
   - [ ] Implement object pooling for byte arrays
   - [ ] Optimize bitmap allocations
   - [ ] Profile memory usage
   - [ ] Fix memory leaks
   - [ ] Reduce allocation rate

3. **Battery Optimization** (Week 24)
   - [ ] Profile battery usage
   - [ ] Optimize encoder settings
   - [ ] Handle Doze mode
   - [ ] Implement battery saver mode
   - [ ] Add battery usage stats

4. **Network Optimization** (Week 24)
   - [ ] Implement connection quality monitoring
   - [ ] Add packet loss recovery
   - [ ] Optimize buffer sizes
   - [ ] Reduce network overhead
   - [ ] Add network stats

5. **Profiling & Benchmarking**
   - [ ] CPU profiling
   - [ ] GPU profiling
   - [ ] Network profiling
   - [ ] Battery profiling
   - [ ] Create performance report

**Deliverables**:
- Optimized performance
- Low latency mode
- Battery efficiency improvements

---

### Week 25-26: User Experience Enhancements

#### Tasks

1. **Multi-Device Support** (Week 25)
   - [ ] Save multiple server configs
   - [ ] Quick switch between servers
   - [ ] Server nicknames
   - [ ] Recent connections history
   - [ ] Favorite servers

2. **Gesture Controls** (Week 25)
   - [ ] Edge swipe for toolbar
   - [ ] Two-finger scroll
   - [ ] Pinch to zoom (map to PC)
   - [ ] Custom gestures
   - [ ] Gesture settings

3. **Notifications** (Week 25)
   - [ ] Connection status notifications
   - [ ] Performance warnings
   - [ ] Low battery alerts
   - [ ] Foreground service notification
   - [ ] Customizable notifications

4. **Widgets** (Week 26)
   - [ ] Quick connect widget
   - [ ] Connection status widget
   - [ ] Widget configuration
   - [ ] Widget updates

5. **Accessibility** (Week 26)
   - [ ] Screen reader support
   - [ ] Content descriptions
   - [ ] High contrast mode
   - [ ] Large text support
   - [ ] Accessibility testing

6. **Localization** (Week 26)
   - [ ] Extract strings to resources
   - [ ] Add translations (initial set)
   - [ ] RTL layout support
   - [ ] Date/time formatting

7. **Help & Documentation** (Week 26)
   - [ ] In-app help
   - [ ] Tooltips
   - [ ] Setup wizard
   - [ ] Troubleshooting guide
   - [ ] FAQ section

**Deliverables**:
- Enhanced user experience
- Widgets and notifications
- Accessibility support
- Initial localization

---

### Week 27: Server Integration & Testing

#### Tasks

1. **Server-Side Changes** (Week 27)
   - [ ] Review all server modifications
   - [ ] Test backward compatibility
   - [ ] Update server documentation
   - [ ] Create migration guide
   - [ ] Test with old clients

2. **Integration Testing** (Week 27)
   - [ ] End-to-end testing
   - [ ] Test all features
   - [ ] Test error scenarios
   - [ ] Test different network conditions
   - [ ] Test with different devices

3. **Beta Testing** (Week 27)
   - [ ] Create beta program
   - [ ] Recruit beta testers
   - [ ] Distribute beta builds
   - [ ] Collect feedback
   - [ ] Fix critical issues

4. **Documentation** (Week 27)
   - [ ] Write user guide
   - [ ] Create setup instructions
   - [ ] Document troubleshooting
   - [ ] Create video tutorials
   - [ ] Update README

5. **Release Preparation**
   - [ ] Create release builds
   - [ ] Test release builds
   - [ ] Prepare Play Store listing
   - [ ] Create screenshots
   - [ ] Write release notes

**Deliverables**:
- Fully integrated system
- Beta testing complete
- Documentation complete
- Ready for release

---

## Post-Release (Ongoing)

### Maintenance & Updates

- [ ] Monitor crash reports
- [ ] Fix bugs
- [ ] Performance improvements
- [ ] New device support
- [ ] Android version updates
- [ ] Feature requests

### Future Enhancements

- [ ] iOS client
- [ ] Direct WiFi connection (WiFi Direct)
- [ ] Bluetooth input device mode
- [ ] Cloud relay for remote access
- [ ] Multi-monitor support
- [ ] Recording functionality
- [ ] Macro/shortcut support

---

## Risk Mitigation

### High-Risk Items

1. **Virtual Display Driver Complexity**
   - **Mitigation**: Start early, allocate extra time, use existing drivers as reference
   - **Fallback**: Ship without virtual display, add in later update

2. **Low Latency Requirements**
   - **Mitigation**: Continuous performance testing, early prototypes
   - **Fallback**: Offer quality/latency trade-off settings

3. **Device Compatibility**
   - **Mitigation**: Test on many devices, use standard APIs
   - **Fallback**: Maintain compatibility matrix, graceful degradation

4. **Stylus Variance**
   - **Mitigation**: Test with multiple stylus types, make features optional
   - **Fallback**: Best-effort support, document supported styluses

---

## Definition of Done

Each feature is considered complete when:

- [ ] Implementation finished and code reviewed
- [ ] Unit tests written and passing
- [ ] Integration tests passing
- [ ] Documentation updated
- [ ] Tested on multiple devices
- [ ] No critical bugs
- [ ] Performance benchmarks met
- [ ] User feedback incorporated

---

## Tracking & Reporting

- **Daily**: Commit code, update task status
- **Weekly**: Team sync, review progress, update timeline
- **Bi-weekly**: Demo to stakeholders, gather feedback
- **Monthly**: Performance review, adjust plan as needed

---

This implementation plan provides a structured approach to building the Weylus Android native client with SuperDisplay features while maintaining flexibility to adjust based on progress and feedback.
