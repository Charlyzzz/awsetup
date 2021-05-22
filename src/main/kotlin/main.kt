import com.github.ajalt.clikt.core.subcommands
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File


fun main(args: Array<String>) = Cli()
    .subcommands(
        ListProfiles(),
        SetProfile(),
        ImportProfiles(),
        Remove()
    ).main(args)

@Serializable
data class Config(
    val profiles: List<Profile>
)

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

fun getConfigFile() = File(getHomePath(), ".awsetup.json")

fun saveConfigFile(config: Config) {
    val configFile = getConfigFile()
    configFile.writeText(Json.encodeToString(Config.serializer(), config))
}

fun loadConfig(): Config {
    val configFile = getConfigFile()
    if (!configFile.exists()) {
        error("config file does not exist")
    }
    return Json.decodeFromString(Config.serializer(), configFile.readText())
}

fun getHomePath(): String {
    return System.getenv("HOME") ?: error("\$HOME is not defined")
}