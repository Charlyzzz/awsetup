import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.io.path.Path

fun getHomePath(): String {
    return System.getenv("HOME") ?: error("\$HOME is not defined")
}

val configFilePath = Path(getHomePath(), ".awsetup.json")

@Serializable
data class Config(
    val profiles: List<Profile>
) {
    fun save() {
        val configJson = Json.encodeToString(serializer(), this)
        file.writeText(configJson)
    }

    companion object {
        val file: File = configFilePath.toFile()

        fun loadFromFile(): Config {
            if (!file.exists()) {
                error("config file does not exist")
            }
            return Json.decodeFromString(serializer(), file.readText())
        }
    }
}

@JvmInline
@Serializable
value class SecretString(private val s: String) {
    override fun toString(): String {
        return "***"
    }

    fun unmask() = s
}

@Serializable
data class Profile(val name: String, val key: SecretString, val secret: SecretString) {
    companion object {
        private const val AWS_KEY_NAME = "aws_access_key_id"
        private const val AWS_SECRET_NAME = "aws_secret_access_key"

        fun fromSection(name: String, properties: Map<String, String>): Profile {
            val key = properties[AWS_KEY_NAME] ?: error("key is missing")
            val secret = properties[AWS_SECRET_NAME] ?: error("secret is missing")
            return Profile(name, SecretString(key), SecretString(secret))
        }
    }
}
