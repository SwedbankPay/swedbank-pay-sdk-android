// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.8.0"
    extra["kotlin_version"] = kotlin_version
    apply(from = "dependencies/dependencies.gradle.kts")

    repositories {
        google()
        mavenCentral()
    }
    
    dependencies {
        classpath(libs.`android-gradle-plugin`)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath(kotlin("gradle-plugin", kotlin_version))
    }
}

plugins {
    kotlin("android") version "1.8.10" apply false
    id("org.jetbrains.dokka") version "1.7.20" apply false
    id("io.github.gradle-nexus.publish-plugin") version "1.2.0"
}

// Remember to use your own group if you fork this library
group = "com.swedbankpay.mobilesdk"
version = getVersionName()

allprojects {
    group = rootProject.group
    version = rootProject.version
    repositories {
        google()
        mavenCentral()
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}

nexusPublishing {
    repositories {
        sonatype()
    }
}
