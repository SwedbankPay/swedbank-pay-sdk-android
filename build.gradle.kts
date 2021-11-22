// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    apply(from = "dependencies/dependencies.gradle.kts")

    repositories {
        google()
    }
    dependencies {
        classpath(libs.`android-gradle-plugin`)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    kotlin("android") version "1.6.0" apply false
    id("org.jetbrains.dokka") version "1.5.31" apply false

    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
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
