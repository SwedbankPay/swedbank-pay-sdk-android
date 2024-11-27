import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.signing.SigningExtension

fun Project.publishToMaven(description: String) {
    afterEvaluate {
        val publication = createReleasePublication(description)
        setupSigning(publication)
    }
}

private fun Project.createReleasePublication(description: String): MavenPublication {
    return the<PublishingExtension>().publications.create<MavenPublication>("release") {
        from(components["release"])
        artifact(javadocJar())

        pom {
            name.set("$groupId:$artifactId")
            this.description.set(description)
            url.set("https://developer.swedbankpay.com/modules-sdks/mobile-sdk/")

            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }

            developers {
                developer {
                    name.set("Swedbank Pay")
                    email.set("opensource@swedbankpay.com")
                    organization.set("Swedbank Pay")
                    organizationUrl.set("https://www.swedbankpay.com/")
                }
            }

            scm {
                connection.set("scm:git:git://github.com/SwedbankPay/swedbank-pay-sdk-android.git")
                developerConnection.set("scm:git:ssh://github.com:SwedbankPay/swedbank-pay-sdk-android.git")
                url.set("https://github.com/SwedbankPay/swedbank-pay-sdk-android")
            }
        }
    }
}

private fun Project.setupSigning(publication: MavenPublication) {
    val signingKey = System.getenv("SIGNING_KEY")?.takeUnless { it.isEmpty() }
    val signingKeyPassword = System.getenv("SIGNING_KEY_PASSWORD")?.takeUnless { it.isEmpty() }
    if (signingKey != null && signingKeyPassword != null) {
        val signingKeyId = System.getenv("SIGNING_KEY_ID")?.takeUnless { it.isEmpty() }

        configure<SigningExtension> {
            useInMemoryPgpKeys(signingKeyId, signingKey, signingKeyPassword)
            sign(publication)
        }
    }
}
