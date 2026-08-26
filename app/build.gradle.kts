import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

val localProperties = Properties().apply {
    val propertiesFile = rootProject.file("local.properties")
    if (propertiesFile.exists()) {
        propertiesFile.inputStream().use(::load)
    }
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.appodealtest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.appodealsdktest.codex.a20260826f3c7"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        val appodealKey = localProperties.getProperty("APPODEAL_APP_KEY", "")
        buildConfigField("String", "APPODEAL_APP_KEY", "\"$appodealKey\"")
    }

    buildFeatures { buildConfig = true }
}

kotlin {
    compilerOptions {
        // Keep Kotlin in sync with Android's Java compilation target.
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    // Current SDK plus the minimal non-optional adapters shown by Appodeal's
    // official Mediation Wizard (BidMachine, Bidon, and iAB).
    implementation("com.appodeal.ads.sdk:core:4.3.0")
    implementation("com.appodeal.ads.sdk.adapters:bidmachine:3.7.1.0")
    implementation("com.appodeal.ads.sdk.adapters:bidon:0.14.0.0")
    implementation("com.appodeal.ads.sdk.adapters:iab:1.8.1.0")
    // Added as a tested workaround for an initialization error observed
// in this specific mediation configuration.
    implementation("com.appodeal.ads.sdk.adapters:sentry_analytics:8.44.1.0")
}
