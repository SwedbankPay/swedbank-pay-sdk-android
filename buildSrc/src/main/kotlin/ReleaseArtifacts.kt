import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.task


fun Project.javadocJar() = task<Jar>("javadocJar") {
    from(tasks["dokkaJavadoc"])
    archiveClassifier.set("javadoc")
}
