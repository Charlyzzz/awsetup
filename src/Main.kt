import com.github.ajalt.clikt.core.subcommands

const val version = "0.1"

fun main(args: Array<String>) =
    Cli()
        .subcommands(
            ListProfiles(),
            SetProfile(),
            ImportProfiles(),
            Remove(),
            Version()
        )
        .main(args)