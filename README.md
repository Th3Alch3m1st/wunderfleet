# Coding Challenge at Wunder Mobility

## Screenshots
<a href="url"><img src="https://user-images.githubusercontent.com/35175271/144422867-eca822a4-07cd-4732-94f7-21b889ec04de.png" height="480" width="230" />
<a href="url"><img src="https://user-images.githubusercontent.com/35175271/144423003-f7e54364-3889-4b89-873b-8fcff2c85ea9.png" height="480" width="230" />
<a href="url"><img src="https://user-images.githubusercontent.com/35175271/144423120-300ea56f-2b5c-44bb-97d6-420c68315ebd.png" height="480" width="230" />
<a href="url"><img src="https://user-images.githubusercontent.com/35175271/144423244-de024adf-ed08-4fef-803d-5713466bf929.png" height="480" width="230" />
<a href="url"><img src="https://user-images.githubusercontent.com/35175271/144423347-0cd412fd-8ff7-4355-85eb-16fdcbb4bbae.png" height="480" width="230" />
<a href="url"><img src="https://user-images.githubusercontent.com/35175271/144423423-c9fdebc5-d45c-4427-999b-58088061e352.png" height="480" width="230" />

## Architecture
MVVM

## Third-party libraries
- Android Architecture Components: LiveData, ViewModel, AppCompat, Android KTX
- Image loading: Glide
- UI component: Material
- View binding: Data Binding
- Dependency injector: Hilt
- Networking: Retrofit
- Unit testing: JUnit4, Mockito, Arch core testing (InstantTaskExecutorRule)
- UI testing: Espresso
- Build configuration : Kotlin DSL
- Full list: https://github.com/Th3Alch3m1st/wunderfleet/blob/master/buildSrc/src/main/java/Dependencies.kt

## performance and optimizations
- ViewModel separate UI and Data layer. ViewModel allow data to survive configuration changes and improve testabilities.
- Jetpack LiveData is a lifecycle aware component, so it can avoid memory leaks and unwanted null pointer exception.
- Jetpack DataBinding to bind the layouts views and it's null safe.
- Use Navigation architecture components for navigation and use navigation safe args gradle plugin for argument passing to ensure safe argument pass
- Use Kotlin DSL for gradle management - it help better gradle management in multi module project. And increase readability, provide code navigation and auto suggestions
- Write Unit test and UI test to ensure app stability and performance
- Write some infix function to increase unit test redability
- Add documentation in UI test to explain test scenario and write short comment for unit test

## Idea for improvement
- Could have used coroutine over RX, it is lighter than RX and has better testing support. For example - Coroutine support flow. Flow can handle back pressure, it can provide better support when need to test something inOrder for example loader show/hide. Live data doesn't support back pressure hence test fail time to time due to this.
- I have user lastLocation from FusedLocationProviderClient class to get user location, could have used requestLocationUpdates for location tracking on user movement
- Could have used a separate viewmodel for car list map and car details fragment. Currently I shared same viewmodel instance in two different fragment, it is better for performance but break single responsibility and interface segregation rule from SOLID rule.
- Could have cache network call, since it's a static api call. But it wouldn't applicable for real scenario, because car location will be updated frequently.

## Build tools
- Android Studio Arctic Fox | 2020.3.1 Patch 3
- Gradle 7.0.2

## Troubleshoot
1. Get the error when compiling Gradle plugin requires Java 11 but IDE uses Java 1.8 ![required-java11](https://user-images.githubusercontent.com/35175271/144035750-16757d5e-2fa1-4e9a-8007-9ca0d8ba1239.png)

- One of a solution is going to Android Studio Preferences->Built, Execution, Deployment->Build Tools->Gradle-> Gradle Project and select Java 11 and try to compile again
![select-java-11](https://user-images.githubusercontent.com/35175271/144036093-103e7a65-52cf-4e56-b39b-5d4fbfcda64a.png)
