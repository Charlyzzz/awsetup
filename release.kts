#!/usr/bin/env kotlin

val release = ProcessBuilder("./gradlew", "build")
    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
    .redirectErrorStream(true)
    .start()
    .waitFor()