plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version embeddedKotlinVersion
}
repositories {
    google()
    mavenCentral()
}

apply(from = "../dependencies/dependencies.gradle.kts")
val libs: Map<String, String> by extra
dependencies {
    implementation(libs["kotlinx-serialization-json"]!!)
    implementation(libs["android-gradle-plugin"]!!)
    implementation(libs["jgit"]!!)

    implementation("org.jetbrains.dokka:dokka-core:1.6.21")
}
