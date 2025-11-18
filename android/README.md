# Weylus Android Native Client

This is the native Android application for Weylus, providing enhanced performance and features compared to the web-based client.

## Project Status

**Current Phase**: Week 1-2 - Project Setup & Foundation ✅

### Completed
- ✅ Android Studio project structure
- ✅ Gradle configuration with Kotlin DSL
- ✅ Version catalogs for dependencies
- ✅ Hilt dependency injection setup
- ✅ MVVM architecture foundation
- ✅ Jetpack Compose with Material 3 theme
- ✅ Navigation graph
- ✅ DataStore for preferences
- ✅ Logging framework (Timber)

### Next Steps
- Week 3: Network Layer implementation
  - WebSocket communication
  - Message protocol
  - HTTP client for QR code scanning

See [docs/implementation-plan.md](docs/implementation-plan.md) for the full development roadmap.

## Building the Project

This project uses Gradle with version catalogs. To build:

```bash
cd android
./gradlew assembleDebug
```

## Architecture

The app follows MVVM (Model-View-ViewModel) architecture with:
- **UI Layer**: Jetpack Compose screens
- **ViewModel Layer**: ViewModels for state management
- **Repository Layer**: Data coordination
- **Data/Service Layer**: WebSocket, Video decoder, Input handlers

See [docs/architecture.md](docs/architecture.md) for detailed architecture documentation.

## Requirements

- Android 7.0 (API 24) or higher
- Android Studio Hedgehog (2023.1.1) or newer
- Kotlin 1.9.22
- Gradle 8.2

## Features (Planned)

### Phase 1: Core Client
- WebSocket communication
- H.264 hardware video decoding
- Touch and stylus input handling
- Basic UI

### Phase 2: SuperDisplay Features
- Virtual display mode
- USB connectivity
- Enhanced video quality (up to 120Hz)
- Advanced stylus features

### Phase 3: Polish & Optimization
- Performance optimizations
- Low latency mode
- UX enhancements

## Documentation

- [Implementation Plan](docs/implementation-plan.md)
- [Architecture](docs/architecture.md)
- [Technology Stack](docs/technology-stack.md)
- [API Protocol](docs/api-protocol.md)
- [Testing Strategy](docs/testing-strategy.md)

## License

Same as the main Weylus project - see [LICENSE](../LICENSE)
