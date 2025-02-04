apply(from = "../dependencies/dependencies.gradle.kts")
val kotlin_version: String by extra

plugins {
    id("com.android.library")
    kotlin("android")
}
apply {
    plugin("kotlin-android")
}

val javaVersion = JavaVersion.VERSION_17
android {
    compileSdk = 34
    defaultConfig {
        minSdk = 21

        namespace = "com.swedbankpay.mobilesdk.testcommon"

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

    implementation(libs.`lifecycle-livedata-ktx`)
    implementation(libs.junit)
    implementation(libs.`mockito-kotlin`)
    //implementation("androidx.core:core-ktx:+")
    implementation(kotlin("stdlib-jdk8", kotlin_version))
}
repositories {
    mavenCentral()
}