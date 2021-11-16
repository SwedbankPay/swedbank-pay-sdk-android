plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("proguard-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(project(":mobilesdk"))
    testImplementation(project(":testcommon"))
    androidTestImplementation(project(":testcommon"))

    implementation(libs.`kotlinx-coroutines-core`)
    implementation(libs.`kotlinx-coroutines-android`)

    debugImplementation(libs.`fragment-testing`)

    implementation(libs.`play-services-base`)
    implementation(libs.`play-services-basement`)

    implementation(libs.gson)

    implementation(libs.okhttp)

    testImplementation(kotlin("reflect"))
    testImplementation(libs.`androidx-test-junit`)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.`mockito-inline`)
    testImplementation(libs.`mockito-kotlin`)
    testImplementation(libs.mockwebserver)

    androidTestImplementation(libs.`androidx-test-core`)
    androidTestImplementation(libs.`androidx-test-runner`)
    androidTestImplementation(libs.uiautomator)
    androidTestImplementation(libs.junit)
}

publishToMaven(
    description = "Utilities for interfacing the Swedbank Pay Android SDK with a Merchant Backend API backend"
)
