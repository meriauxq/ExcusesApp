# ExcusesApp

*ExcusesApp* is a simple Android application connected to a backend that provides a list developer excuses. It allows
users to view random excuses, search for specific excuses by HTTP code, and add new excuses.

## Backend (Ktor)

> Ktor is a framework for quickly creating connected applications in Kotlin with minimal effort

### Plugins

Here are the plugins used for this Ktor project:

- **Call Logging**: Logs client requests
- **Content Negotiation**: Provides automatic content conversion according to Content-Type and Accept headers
- **kotlinx.serialization**: Handles JSON serialization using kotlinx.serialization library
- **Exposed**: Adds Exposed database to your application
- **Routing**: Allows to define structured routes and associated handlers

### Routes

| Method | Route        | Description         |
|--------|--------------|---------------------|
| POST   | /excuses     | Create a new excuse |
| GET    | /excuses     | Read all excuses    |
| GET    | /{http_code} | Read an excuse      |

### Start the server

#### Run from your IDE

1. Open the project `excuses-api` in IntelliJ IDEA or your preferred IDE.
2. Wait for the Gradle sync completion.
3. Run the `main()` function in `src/main/kotlin/com/quentinmeriaux/Application.kt`.

#### Run manually

If you prefer, you can execute the following commands to start the server:

```bash
cd excuses-api
.\gradlew.bat build
.\gradlew.bat run
```

#### Tests

Some basic tests testing the different routes are defined in `src/test/kotlin/com/quentinmeriaux/ApplicationTest.kt`.

## Frontend (Android)

> The app is fully written in Kotlin and uses Jetpack Compose to build the UI

### Dependencies

Here are the main dependencies used for this Android project:

- **[Jetpack Compose](https://developer.android.com/jetpack/compose/)**: UI toolkit
- **[Coil](https://coil-kt.github.io/coil/)**: Image loading
- **[Koin](https://insert-koin.io/)**: Dependency Injection
- **[Ktor Client](https://ktor.io/docs/getting-started-ktor-client.html)**: Network client
- **[kotlinx.serialization.json](https://github.com/Kotlin/kotlinx.serialization)**: JSON serialization

### Install the app

#### Run from Android Studio

The simplest solution is to run the project directly from [Android Studio](https://developer.android.com/studio) to
install the app on your phone or an emulator.

1. Open the project `excuses-app` in Android Studio.
2. Wait for the Gradle sync completion.
3. Update `BASE_URL` in `app/src/main/java/com/quentinmeriaux/excusesapp/data/remote/HttpRoutes.kt` according to the
   IPv4 address of the machine running the server, or use the emulator address.
4. Select an emulator or your device (it may require to enable `Developer options` and `USB Debug` on the device).
5. Run the `app` configuration through the IDE. If the configuration does not exist, go to `Edit Configurations...`, add
   a new `Android App` configuration and select the `ExcusesApp.app.main` module.

