import org.eclipse.jgit.api.Git
import org.gradle.api.Project
import java.io.IOException

fun Project.getVersionName(): String {
    return getVersionFromGithubRelease() ?: getVersionFromGitDescribe() ?: "local"
}

private fun getVersionFromGithubRelease() = readGithubReleaseEvent()?.release?.tag_name

private fun Project.getVersionFromGitDescribe(): String? {
    return try {
        Git.open(rootDir).use {
            val name = it.describe().setTags(true).call()
            val dirtyFlag = if (it.status().call().isClean) "" else "-dirty"
            "$name$dirtyFlag-SNAPSHOT"
        }
    } catch (e: IOException) {
        null
    }
}
