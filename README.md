# DramaFy — Short Drama Streaming App

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-1.9.22-7C5CFC?style=flat-square&logo=kotlin" />
  <img src="https://img.shields.io/badge/Compose-2024.01-7C5CFC?style=flat-square" />
  <img src="https://img.shields.io/badge/Material-3-7C5CFC?style=flat-square" />
  <img src="https://img.shields.io/badge/MinSDK-26-7C5CFC?style=flat-square" />
</p>

A modern short drama streaming application built with Kotlin and Jetpack Compose, featuring a sleek dark interface designed for immersive viewing.

## Features

- 🎬 **Home Feed** — Auto-scrolling featured carousel, trending dramas, and curated sections
- 🔍 **Smart Search** — Real-time suggestions and full-text search across the drama catalog
- 📚 **Browse** — Category-filtered grid view with tab navigation
- 📺 **Detail View** — Rich drama details with synopsis, cast, and episode selector
- ▶️ **Built-in Player** — ExoPlayer-powered video playback with episode navigation
- 🌙 **Dark Theme** — Premium dark UI with purple accent palette
- 📱 **Edge-to-Edge** — Full immersive display with transparent system bars
- ✨ **Floating Nav** — Elevated floating bottom navigation bar

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 1.9.22 |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Repository |
| Networking | Retrofit2 + OkHttp3 + Gson |
| Image Loading | Coil 2 |
| Video Player | Media3 (ExoPlayer) |
| Navigation | Navigation Compose |
| Build | Gradle 8.5 + AGP 8.2 |
| CI/CD | GitHub Actions |

## Project Structure

```
app/src/main/java/com/dramafy/app/
├── App.kt                    # Application class
├── MainActivity.kt           # Main activity with bottom navigation
├── data/
│   ├── api/
│   │   ├── DramaApiService.kt    # Retrofit API interface
│   │   └── AuthInterceptor.kt    # Auth header interceptor
│   ├── model/                    # Data classes for API responses
│   └── repository/
│       └── DramaRepository.kt    # Data layer
├── di/
│   └── NetworkModule.kt          # Dependency injection
└── ui/
    ├── theme/                    # Colors, typography, theme
    ├── components/               # Reusable UI components
    ├── navigation/               # Navigation graph & routes
    └── screens/
        ├── home/                 # Home feed
        ├── search/               # Search with suggestions
        ├── detail/               # Drama detail + episodes
        ├── player/               # Video player
        ├── bookmall/             # Browse/explore
        └── library/              # User library
```

## Building

### Via GitHub Actions (Recommended)

Every push to `main` automatically builds both debug and release APKs. Download them from the Actions tab → Artifacts.

### Locally

```bash
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

## API Integration

The app connects to a REST API with the following endpoints:

| Endpoint | Description |
|----------|-------------|
| `GET /languages` | Available languages |
| `GET /search` | Search dramas |
| `GET /search/suggest` | Search suggestions |
| `GET /bookmall` | Home catalog content |
| `GET /bookmall/tabs` | Category tabs |
| `GET /book` | Drama detail |
| `GET /series` | Episode list |
| `GET /multi-video` | Video URLs |

## License

Private project. All rights reserved.
