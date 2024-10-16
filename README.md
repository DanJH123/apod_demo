# APOD Viewer

An Android and iOS application built with Kotlin Multiplatform and Compose Multiplatform to display NASA's Astronomy Picture of the Day (APOD).

## Overview

This project fetches the APOD using NASA's API and displays the image along with its explanation and metadata. You can:

- Swipe to navigate through past astronomy photos.
- Tap to enlarge images, which loads the HD version if available.

**Note:** Videos are currently not supported, any APODs that are videos will show an error.

## Setup

To run the project, you'll need a NASA API key, which you can obtain for free [here](https://api.nasa.gov/).

1. Search the project for `API_KEY_HERE`.
2. Replace `API_KEY_HERE` with your NASA API key in the code.

## Project Highlights

- **Kotlin Multiplatform Mobile (KMM):** Shares code between Android and iOS platforms
- **Compose Multiplatform:** A declarative UI framework used for building native user interfaces on multiple platforms.
- **NASA APOD API Integration:** Fetches daily astronomy images and metadata from NASA's public API.

## Key Components

- **Data Layer:** Handles API calls and data retrieval using Ktor, a Kotlin networking library.
- **Domain Layer:** Contains business logic and use cases, such as fetching the APOD.
- **Presentation Layer:** Implements the UI using Compose Multiplatform, including screens and view models.
- **Dependency Injection with Koin:** For managing dependencies across the project.
- **Landscapist:** A Jetpack Compose image loading library for image rendering.

## Libraries and Plugins

- **Ktor:** For making asynchronous network requests and handling JSON serialization.
- **Koin:** A dependency injection framework for Kotlin.
- **Kotlinx DateTime:** Provides multiplatform support for date and time operations.
- **Landscapist:** Enhances image loading capabilities in Compose applications.
- **AndroidX Compose and Navigation:** Facilitates building and navigating between composable UIs.
- **Gradle Plugins:**
  - **Kotlin Multiplatform Plugin:** Supports multiplatform project configurations.
  - **Compose Plugin:** Enables Compose features in the project.
  - **Android Application and Library Plugins:** Required for Android-specific build configurations.
  - **Kotlin Serialization Plugin:** Adds support for Kotlin serialization.

## Disclaimer

This project was created to experiment with Kotlin Multiplatform Mobile (KMM) and Compose Multiplatform. Future updates may or may not occur.
