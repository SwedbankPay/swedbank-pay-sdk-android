import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

// Helper to access extra["libs"] more fluently in Kotlin
@Suppress("UNCHECKED_CAST")
val Project.libs get() = Dependencies(rootProject.extra["libs"] as Map<String, String>)

class Dependencies(map: Map<String, String>) {
    val `android-gradle-plugin` by map

    val `kotlinx-coroutines-core` by map
    val `kotlinx-coroutines-android` by map

    val `androidx-appcompat` by map
    val `androidx-core-ktx` by map
    val `fragment-ktx` by map
    val `fragment-testing` by map
    val `lifecycle-livedata-ktx` by map
    val `lifecycle-viewmodel-ktx` by map
    val constraintlayout by map
    val coordinatorlayout by map
    val `navigation-fragment` by map
    val swiperefreshlayout by map

    val `androidx-test-core` by map
    val `androidx-test-runner` by map

    val `espresso-core` by map
    val `espresso-intents` by map
    val `espresso-web` by map

    val `androidx-test-junit` by map
    val uiautomator by map

    val material by map
    val `play-services-base` by map
    val `play-services-basement` by map
    val gson by map

    val junit by map

    val `joda-time` by map
    val threetenbp by map

    val okhttp by map
    val mockwebserver by map

    val robolectric by map

    //val `mockito-inline` by map
    val `mockito-kotlin` by map
    val `dexmaker-mockito-inline` by map
}
