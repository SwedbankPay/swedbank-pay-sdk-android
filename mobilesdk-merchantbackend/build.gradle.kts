apply(from = "../dependencies/dependencies.gradle.kts")
val kotlin_version: String by extra

plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}
apply {
    plugin("kotlin-android")
}

val javaVersion = JavaVersion.VERSION_17
android {
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        namespace = "com.swedbankpay.mobilesdk.merchantbackend"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("proguard-rules.pro")

        unitTestVariants.configureEach {
            mergedFlavor.manifestPlaceholders["swedbankPaymentUrlScheme"] = ""
        }

        testVariants.configureEach {
            mergedFlavor.manifestPlaceholders["swedbankPaymentUrlScheme"] = ""
        }
    }

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    kotlinOptions {
        jvmTarget = javaVersion.toString()
    }
}

dependencies {
    implementation(project(":mobilesdk"))
    testImplementation(project(":testcommon"))
    androidTestImplementation(project(":testcommon"))

    implementation(libs.`kotlinx-coroutines-core`)
    implementation(libs.`kotlinx-coroutines-android`)

    debugImplementation(libs.`fragment-testing`) {
        isTransitive = false
    }

    implementation(libs.`play-services-base`)
    implementation(libs.`play-services-basement`)

    implementation(libs.gson)

    implementation(libs.okhttp)

    testImplementation(kotlin("reflect"))
    testImplementation(libs.`androidx-test-junit`)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
//    testImplementation(libs.`mockito-inline`)
    testImplementation(libs.`mockito-kotlin`)
    testImplementation(libs.mockwebserver)

    androidTestImplementation(libs.`androidx-test-core`)
    androidTestImplementation(libs.`androidx-test-runner`)
    androidTestImplementation(libs.uiautomator)
    androidTestImplementation(libs.junit)
    implementation(kotlin("stdlib-jdk8", kotlin_version))
}

publishToMaven(
    description = "Utilities for interfacing the Swedbank Pay Android SDK with a Merchant Backend API backend"
)
repositories {
    mavenCentral()
}