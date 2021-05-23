import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import kotlin.io.path.deleteIfExists

class Cli : CliktCommand() {
    override fun run() = Unit
}

class ListProfiles : CliktCommand(name = "list", help = "list available profiles") {
    override fun run() {
        val profileNames = Config.loadFromFile().profiles.map { it.name }
        println(profileNames)
    }
}

class SetProfile : CliktCommand(name = "set", help = "set given profile") {
    private val name by argument(name = "name")

    override fun run() {
        val config = Config.loadFromFile()

        val profile = config.profiles.find { name == it.name } ?: error("profile not found")
        AwsConfig().write(profile)
    }
}

class ImportProfiles : CliktCommand(name = "import", help = "imports aws credentials") {
    private val customConfig by option("-c", "--config")

    override fun run() {
        val config = AwsConfig(customConfig).toConfig()
        config.save()
    }
}

class Remove : CliktCommand(name = "remove", help = "removes AWSetup changes") {
    override fun run() {
        val awsConfig = AwsConfig()
        FileHelpers.restoreBackup(awsConfig.path)
        configFilePath.deleteIfExists()
    }
}

class Version() : CliktCommand(name = "version", help = "prints CLI version") {
    override fun run() {
        println(version)
    }
}

