//val kotlin_version: String by extra
val kotlin_version:String = "1.8.0"
plugins {
    id("com.android.library")
    kotlin("android")
}
apply {
    plugin("kotlin-android")
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
    //implementation("androidx.core:core-ktx:+")
    implementation(kotlin("stdlib-jdk8", kotlin_version))
}
repositories {
    mavenCentral()
}