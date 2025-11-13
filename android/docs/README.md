# Weylus Android Native Implementation

## Project Overview

This directory contains the development plan and implementation for a native Android application for Weylus, providing enhanced performance and features compared to the web-based client.

### Goals

1. **Feature Parity**: Implement all existing Weylus functionality in a native Android app
2. **Enhanced Performance**: Leverage hardware-accelerated video decoding and native APIs
3. **SuperDisplay Features**: Add advanced features present in SuperDisplay application
4. **Better User Experience**: Provide a polished, native Android experience

### Key Improvements Over Web Client

- **Native H.264 Hardware Decoding**: Better performance and lower battery consumption
- **Lower Latency**: No browser overhead, direct rendering pipeline
- **Enhanced Stylus Support**: Full access to Android MotionEvent API (pressure, tilt, twist, hover)
- **Virtual Display Mode**: True extended display functionality (requires Windows driver)
- **USB Connectivity**: Fast, reliable connection via USB in addition to WiFi
- **High Refresh Rate**: Support for up to 120Hz displays
- **Background Operation**: Continue operation when app is backgrounded
- **Native Android Integration**: Better notifications, widgets, system integration

### Project Status

**Current Phase**: Planning and Documentation

**Target Timeline**: 5-6 months for full implementation

### Documentation Structure

This docs directory contains comprehensive documentation for the Android implementation:

- **[README.md](README.md)** - This file, project overview and quick reference
- **[architecture.md](architecture.md)** - Technical architecture and design decisions
- **[implementation-plan.md](implementation-plan.md)** - Detailed implementation phases and tasks
- **[technology-stack.md](technology-stack.md)** - Technology choices and rationale
- **[api-protocol.md](api-protocol.md)** - WebSocket protocol and extensions
- **[testing-strategy.md](testing-strategy.md)** - Testing approach and quality assurance
- **[features.md](features.md)** - Feature comparison and requirements
- **[timeline.md](timeline.md)** - Project timeline and milestones
- **[challenges.md](challenges.md)** - Key challenges and mitigation strategies

### Quick Start (Future)

Once implemented, the project structure will be:

```
android/
├── docs/                    # This documentation
├── app/                     # Android application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── res/
│   │   └── test/
│   └── build.gradle.kts
├── driver/                  # Windows virtual display driver
│   └── weylus-vdd/
├── server-patches/          # Patches for Weylus server
└── build.gradle.kts
```

### Development Phases

1. **Phase 1: Core Android Client** (8-10 weeks)
   - Network layer and WebSocket communication
   - Video decoding and rendering
   - Input handling (touch, stylus, keyboard)
   - Basic UI

2. **Phase 2: SuperDisplay Features** (8-10 weeks)
   - Virtual display driver (Windows)
   - Extended display mode
   - USB connectivity
   - Enhanced video quality
   - Advanced stylus features

3. **Phase 3: Polish & Optimization** (4-5 weeks)
   - Performance optimizations
   - UX enhancements
   - Server integration
   - Testing and bug fixes

### Technology Stack Summary

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Networking**: OkHttp 4.x
- **Video**: MediaCodec (hardware-accelerated)
- **Async**: Coroutines + Flow
- **Dependency Injection**: Hilt
- **Minimum SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 14 (API 34)

### Contributing

This is part of the Weylus project. See the main project README for contribution guidelines.

### License

Same as the main Weylus project.

### References

- [Weylus Main Project](../../README.md)
- [Weylus Architecture](../../README.md)
- [WebSocket Protocol](../../src/protocol.rs)
- SuperDisplay App (commercial reference)

---

For detailed information, see the individual documentation files in this directory.
