import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option

class Cli : CliktCommand() {
    override fun run() = Unit
}

class ListProfiles : CliktCommand(name = "list", help = "list available profiles") {
    override fun run() {
        val profileNames = loadConfig().profiles.map { it.name }
        println(profileNames)
    }
}

class SetProfile : CliktCommand(name = "set", help = "set given profile") {
    private val name by argument(name = "name")

    override fun run() {
        val config = loadConfig()
        val profile = config.profiles.find { name == it.name } ?: error("profile not found")
        AwsConfig().write(profile)
    }
}

class ImportProfiles : CliktCommand(name = "import", help = "imports aws credentials") {
    private val customConfig by option("-c", "--config")

    override fun run() {
        val config = AwsConfig(customConfig).toConfig()
        saveConfigFile(config)
    }
}

class Remove : CliktCommand(name = "remove", help = "removes AWSetup changes") {
    override fun run() {
        val awsConfig = AwsConfig()
        Files.restoreBackup(awsConfig.path)
        getConfigFile().delete()
    }
}

