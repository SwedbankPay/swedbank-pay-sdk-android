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
        minSdk = 21 // Required by okhttp (from version 3.13 onwards)
        namespace = "com.swedbankpay.mobilesdk"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("proguard-rules.pro")
        buildConfigField("String", "SDK_VERSION", "\"$version\"")
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    kotlinOptions {
        jvmTarget = javaVersion.toString()
    }

    buildFeatures {
        buildConfig = true
    }

}

dependencies {
    testImplementation(project(":testcommon"))
    androidTestImplementation(project(":testcommon"))

    implementation(libs.`kotlinx-coroutines-core`)
    implementation(libs.`kotlinx-coroutines-android`)

    implementation(libs.`androidx-appcompat`)
    implementation(libs.`androidx-core-ktx`)

    implementation(libs.`fragment-ktx`)
    debugImplementation(libs.`fragment-testing`) {
        isTransitive = false
    }

    implementation(libs.`lifecycle-livedata-ktx`)
    implementation(libs.`lifecycle-viewmodel-ktx`)

    implementation(libs.constraintlayout)
    implementation(libs.swiperefreshlayout)
    implementation(libs.coordinatorlayout)
    implementation(libs.material)
    implementation(libs.`navigation-fragment`)

    implementation(libs.okhttp)

    implementation(libs.gson)

    compileOnly(libs.`joda-time`)
    compileOnly(libs.threetenbp)

    testImplementation(libs.`androidx-test-core`)
    testImplementation(libs.`espresso-core`)
    testImplementation(libs.`espresso-intents`)
    testImplementation(libs.`androidx-test-junit`)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.`mockito-kotlin`)

    androidTestImplementation(kotlin("reflect"))
    androidTestImplementation(libs.`androidx-test-core`)
    androidTestImplementation(libs.`androidx-test-runner`)
    androidTestImplementation(libs.`espresso-core`)
    androidTestImplementation(libs.`espresso-intents`)
    androidTestImplementation(libs.`espresso-web`)
    androidTestImplementation(libs.`uiautomator`)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.`dexmaker-mockito-inline`)
    androidTestImplementation(libs.`mockito-kotlin`)
    implementation(kotlin("stdlib-jdk8", kotlin_version))
}

publishToMaven(
    description = "A library for integrating Swedbank Pay payments into an Android application"
)
repositories {
    mavenCentral()
}