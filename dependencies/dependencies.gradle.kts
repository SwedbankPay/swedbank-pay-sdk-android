// Declare all dependency versions here so that they are consistent between subprojects.
extra["kotlin_version"] = "1.8.10"

extra["libs"] = mapOf(
   "android-gradle-plugin" to "com.android.tools.build:gradle:7.4.2",   //7.1.3

   "jgit" to "org.eclipse.jgit:org.eclipse.jgit:6.5.0.202303070854-r",

   "kotlinx-coroutines-core" to "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0",
   "kotlinx-coroutines-android" to "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4",
   "kotlinx-serialization-json" to "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0",

   "androidx-appcompat" to "androidx.appcompat:appcompat:1.6.1",
   "androidx-core-ktx" to "androidx.core:core-ktx:1.10.0",
   "fragment-ktx" to "androidx.fragment:fragment-ktx:1.5.6",
   "lifecycle-livedata-ktx" to "androidx.lifecycle:lifecycle-livedata-ktx:2.6.1",
   "lifecycle-viewmodel-ktx" to "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1",
   "constraintlayout" to "androidx.constraintlayout:constraintlayout:2.1.4",
   "coordinatorlayout" to "androidx.coordinatorlayout:coordinatorlayout:1.2.0",
   "navigation-fragment" to "androidx.navigation:navigation-fragment:2.5.3",
   "swiperefreshlayout" to "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0",

   "androidx-test-core" to "androidx.test:core:1.5.0",
   "androidx-test-runner" to "androidx.test:runner:1.5.2",
   
   "espresso-core" to "androidx.test.espresso:espresso-core:3.5.1",
   "espresso-intents" to "androidx.test.espresso:espresso-intents:3.5.1",
   "espresso-web" to "androidx.test.espresso:espresso-web:3.5.1",

   "androidx-test-junit" to "androidx.test.ext:junit:1.1.5",
   "uiautomator" to "androidx.test.uiautomator:uiautomator:2.2.0",

   "material" to "com.google.android.material:material:1.8.0",
   "play-services-base" to "com.google.android.gms:play-services-base:18.2.0",
   "play-services-basement" to "com.google.android.gms:play-services-basement:18.2.0",
   "gson" to "com.google.code.gson:gson:2.10.1",

   "junit" to "junit:junit:4.13.2",

   "joda-time" to "joda-time:joda-time:2.12.5",
   "threetenbp" to "org.threeten:threetenbp:1.6.8",

   "okhttp" to "com.squareup.okhttp3:okhttp:4.10.0",
   "mockwebserver" to "com.squareup.okhttp3:mockwebserver:4.10.0",

   "robolectric" to "org.robolectric:robolectric:4.10",
   //"mockito-inline" to "org.mockito:mockito-inline:4.8.1",
   "mockito-kotlin" to "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0",
   "dexmaker-mockito-inline" to "com.linkedin.dexmaker:dexmaker-mockito-inline:2.28.3",
   "fragment-testing" to "androidx.fragment:fragment-testing:1.5.6",
)
