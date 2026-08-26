# Appodeal Android SDK Test App

Minimal Android application demonstrating Appodeal Banner, Interstitial, Rewarded Video, and Native Ads in Test Mode. It explicitly shows SDK initialization, manual loading, display, and callback logging for every requested format.

## Verified result

Runtime verification was completed on 2026-08-26 on an Android API 35 Google APIs x86_64 emulator.

| Requirement | Verified result |
| --- | --- |
| SDK initialization | `INIT finished; errors=none` |
| Banner | loaded and shown |
| Interstitial | loaded, shown, and closed |
| Rewarded Video | loaded, shown, reward finished, and closed |
| Native Ad | loaded and shown |
| Test Mode | enabled before initialization with `Appodeal.setTesting(true)` |
| Callback logging | implemented for every callback exposed by the four callback interfaces |

The screenshots and captured Logcat files are listed in [QA_DOCUMENTATION.md](QA_DOCUMENTATION.md). The verified debug APK is `qa-artifacts/appodeal-test-debug.apk` in the submission package.

## Project configuration

- Application ID: `com.appodealsdktest.codex.a20260826f3c7`
- Minimum Android API: 26
- Target/compile Android API: 35
- Kotlin, Android Gradle Plugin 8.6.1, Gradle 8.7, JDK 21
- Appodeal Android SDK core: 4.3.0
- Official adapters used by the tested mediation configuration:
  - BidMachine 3.7.1.0
  - Bidon 0.14.0.0
  - iAB 1.8.1.0 (MRAID/VAST)
  - Sentry Analytics 8.44.1.0

The Appodeal app key is deliberately not committed or included in the submission archive. 
The included APK is the exact verified build. Rebuilding the project requires a valid Appodeal app key configured for the application package.

## Build

1. Install Android Studio with Android SDK Platform 35 and Build Tools 35.0.0. Use JDK 21.
2. Copy `local.properties.example` to `local.properties`.
3. Set the local Android SDK path and the Appodeal key created for the same Android package:

   ```properties
   sdk.dir=C\:\\Users\\you\\AppData\\Local\\Android\\Sdk
   APPODEAL_APP_KEY=your_appodeal_app_key
   ```

4. Open the project in Android Studio and build the `debug` variant, or use the included Gradle 8.7 Wrapper:

   ```powershell
   .\gradlew.bat assembleDebug
   ```

The APK will be written to `app/build/outputs/apk/debug/app-debug.apk`.

## Run and test

1. Start an emulator/device with Internet access.
2. Install and launch the debug build:

   ```powershell
   adb install -r app\build\outputs\apk\debug\app-debug.apk
   adb shell am force-stop com.appodealsdktest.codex.a20260826f3c7
   adb shell monkey -p com.appodealsdktest.codex.a20260826f3c7 1
   ```

3. Filter Logcat by `AppodealSample`.
4. Wait for `INIT finished; errors=none` and the four `loaded` events.
5. Press **Show Banner**, **Show Native**, **Show Interstitial**, and **Show Rewarded**. Close fullscreen ads normally and let the rewarded ad finish.
6. Verify the corresponding `shown`, `closed`, and `finished` callbacks in Logcat.

No ad click is required. The click, failure, show-failure, and expiration callbacks are implemented and logged, but they only appear when the SDK actually emits those events.

## Implementation summary

`MainActivity` installs all callback listeners before SDK initialization. It then enables Test Mode and verbose SDK logging, disables automatic caching, and initializes the four formats with one bit mask. The initialization callback calls `loadAll()`, which explicitly caches each format. Each UI button checks `Appodeal.isLoaded(...)` before display and records whether `Appodeal.show(...)` or native view registration was accepted. Both Logcat and an on-screen event panel use the `AppodealSample` event text.

The Native Ad is obtained through `Appodeal.getNativeAds(1)` and registered with `NativeAdViewNewsFeed`. The banner uses the XML `AppodealBannerView` linked through `Appodeal.setBannerViewId(...)`.

## Known issues and environment notes

- The Appodeal Dashboard still displayed `No First Ad impression yet` immediately after the verified run. Dashboard propagation was not proven; the runtime screenshots and Logcat are the evidence used here.
- Core 4.3.0 plus the wizard's BidMachine/Bidon/iAB adapters loaded and displayed all formats, but initialization still returned a non-fatal SDK configuration error because the server response had no `services` object. Adding Appodeal's official Sentry Analytics service adapter made the verified initialization finish with `errors=none`. This is a tested workaround for this mediation-only configuration, not a general claim that every integration requires Sentry.
- The test PC used Avast HTTPS inspection. Its public root certificate was installed only in the disposable emulator, and only the debug resource overlay trusts user-installed certificates. The main/release network policy trusts system certificates only.
- The first Banner/Native request once timed out on the slow emulator network; retrying loaded both. The final clean run loaded all four formats on their first request.
- The working Android toolchain and build outputs were placed on drive `D:` because drive `C:` had no usable free space.

See [QA_DOCUMENTATION.md](QA_DOCUMENTATION.md) for the complete troubleshooting record and exact verified callbacks.

## Official documentation used

- [Android Get Started](https://docs.appodeal.com/android/get-started)
- [Testing Appodeal integration](https://docs.appodeal.com/android/advanced/testing)
- [Configure mediated networks](https://docs.appodeal.com/android/advanced/configure-mediated-networks)
- [Banner](https://docs.appodeal.com/android/ad-types/banner)
- [Interstitial](https://docs.appodeal.com/android/ad-types/interstitial)
- [Rewarded Video](https://docs.appodeal.com/android/ad-types/rewarded-video)
- [Native Ads](https://docs.appodeal.com/android/ad-types/native)
- [Official Android demo repository](https://github.com/appodeal/appodeal-android-sdk)
