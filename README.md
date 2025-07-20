# NoviSlide

NoviSlide is a simple Android application that displays a fullscreen looping slideshow of media items (images and videos) retrieved from a remote backend.

## Features

- Jetpack Compose-based UI
- Displays images and videos in a seamless slideshow
- Crossfade animation for transitions
- Supports both image and video media types
- Built with MVVM architecture
- Retrofit-powered backend integration
- Coil for image loading
- ExoPlayer for video playback
- Kotlin Coroutines and Flows for reactive data handling
- Hilt for dependency injection

## Screens

- **HomeScreen**: Displays a loading indicator, error message, or a full-screen slideshow depending on the state.
- **Slideshow**: Cycles through media items with configurable durations and transitions.

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Media**: Coil (images), ExoPlayer (videos)
- **Architecture**: MVVM + Clean Architecture principles
- **Dependency Injection**: Hilt
- **Networking**: Retrofit
- **Asynchronous**: Coroutines, Flow

## Getting Started

### Prerequisites

- Android Studio Giraffe or newer
- Kotlin 1.9+
- Android SDK 33+
