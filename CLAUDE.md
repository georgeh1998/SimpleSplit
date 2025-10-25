# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SimpleSplit is a Kotlin Multiplatform (KMP) application targeting Android and iOS platforms. 
The project uses Compose Multiplatform for shared UI code and integrates with Supabase for backend services.
This application allows users to manage group expenses.

## Architecture

### Platform Structure

The codebase follows the standard Kotlin Multiplatform structure:

- **composeApp/src/commonMain**: Shared code for all platforms
  - UI components in `ui_component/`
  - Theme configuration in `theme/`
  - Main app entry point in `App.kt`
- **composeApp/src/androidMain**: Android-specific implementations
- **composeApp/src/iosMain**: iOS-specific implementations
- **iosApp/**: iOS application entry point and SwiftUI code (if needed)

### Key Dependencies

- **Compose Multiplatform 1.9.0**: Shared UI framework
- **Kotlin 2.2.20**: Language version
- **Supabase 3.2.4**: Backend integration (PostgREST)
- **Ktor 3.3.0**: HTTP client (platform-specific engines: OkHttp for Android, Darwin for iOS, CIO for common)
- **Kotlin Serialization**: Enabled for data serialization

### Build Configuration

The project uses Gradle version catalogs (`gradle/libs.versions.toml`) for dependency management. The iOS framework is built as a static framework named "ComposeApp" targeting both iosArm64 (physical devices) and iosSimulatorArm64 (simulators).

## Build and Development Commands

### Android

```bash
# Build debug APK
./gradlew :composeApp:assembleDebug

# Build release APK
./gradlew :composeApp:assembleRelease

# Install and run on connected device/emulator
./gradlew :composeApp:installDebug

# Clean build
./gradlew clean
```

### iOS

Open the Xcode project and run from there:
```bash
open iosApp/iosApp.xcodeproj
```

Or use Xcode command line tools:
```bash
xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -configuration Debug
```

### Testing

```bash
# Run all tests
./gradlew test

# Run tests for specific platform
./gradlew :composeApp:testDebugUnitTest  # Android
./gradlew :composeApp:iosSimulatorArm64Test  # iOS simulator
```

### Other Useful Commands

```bash
# Check dependencies
./gradlew :composeApp:dependencies

# Verify project setup
./gradlew tasks --all

# Generate Compose resources
./gradlew :composeApp:generateComposeResClass
```

## Development Notes

### Package Structure

The main package is `com.github.georgeh1998.simplesplit`. All Kotlin code should be placed under this namespace following the existing structure.

### Resource Management

String resources are managed through Compose Multiplatform's resource system. Add strings to `composeApp/src/commonMain/composeResources/values/strings.xml` and access them using `stringResource(Res.string.resource_name)`.

### Theme System

The project uses a custom theme (`SSTheme`) built on Material3. Theme components are in the `theme/` package including `Typography.kt`, `Color.kt`, and `Theme.kt`.

### Platform-Specific Code

When implementing platform-specific features:
- Use `expect`/`actual` declarations for API definitions
- Place Android implementations in `androidMain`
- Place iOS implementations in `iosMain`
- Use appropriate Ktor client engines per platform (already configured)

### Gradle Configuration

- JVM target: Java 11 for Android
- Min SDK: 24 (Android 7.0)
- Target SDK: 36
- Compile SDK: 36
- Configuration cache and build cache are enabled for faster builds
