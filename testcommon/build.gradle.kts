plugins {
    id("com.android.library")
    kotlin("android")
}

val javaVersion = JavaVersion.VERSION_11
android {
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        targetSdk = 33
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

    implementation(libs.`lifecycle-livedata-ktx`)
    implementation(libs.junit)
    implementation(libs.`mockito-kotlin`)
}
