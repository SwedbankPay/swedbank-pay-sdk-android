import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.task
import org.gradle.kotlin.dsl.the

fun Project.sourcesJar() = task<Jar>("sourcesJar") {
    from(project.the<LibraryExtension>().sourceSets["main"].java.srcDirs)
    archiveClassifier.set("sources")
}

fun Project.javadocJar() = task<Jar>("javadocJar") {
    from(tasks["dokkaJavadoc"])
    archiveClassifier.set("javadoc")
}
