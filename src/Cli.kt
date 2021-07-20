import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlin.io.path.deleteIfExists

class Cli : CliktCommand() {
    private val versionOpt by option("--version").flag()

    override fun run() {
        if (versionOpt) println(version)
    }
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

class Version : CliktCommand(name = "version", help = "prints CLI version") {
    override fun run() {
        println(version)
    }
}

class CurrentProfile : CliktCommand(name = "current", help = "prints current profile") {
    override fun run() {
        AwsConfig().toConfig().defaultProfile()?.let { awsProfile ->
            val config = Config.loadFromFile()
            config.profiles.find { it.sameCredentials(awsProfile) }?.also {
                println(it.name)
            } ?: println("profile not present in config. Try running 'import' command first")
        } ?: println("no profile set")
    }
}
