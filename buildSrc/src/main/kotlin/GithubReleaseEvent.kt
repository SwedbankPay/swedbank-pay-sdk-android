import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

@Serializable
data class GithubReleaseEvent(val release: GithubRelease)

@Serializable
data class GithubRelease(val tag_name: String)

private val json = Json { ignoreUnknownKeys = true }

fun readGithubReleaseEvent(): GithubReleaseEvent? {
    val isRelease = System.getenv("GITHUB_EVENT_NAME") == "release"
    val path = if (isRelease) System.getenv("GITHUB_EVENT_PATH") else null
    val file = path?.let(::File)
    val text = file?.readText()
    return text?.let(json::decodeFromString)
}
