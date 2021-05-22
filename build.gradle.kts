import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.serialization") version "1.5.0"
    id("org.mikeneck.graalvm-native-image") version "1.4.0"
    application
}

group = "me.charlyzzz"
version = "1.0-SNAPSHOT"

val appMain = "MainKt"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
}

application {
    mainClass.set(appMain)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

nativeImage {
    graalVmHome = System.getenv("JAVA_HOME")
    buildType {
        it.executable(appMain)
    }
    executableName = "awsetup"
    outputDirectory = file("$buildDir/bin")
    arguments(
        "--no-fallback",
        "--enable-all-security-services",
        "--report-unsupported-elements-at-runtime"
    )
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}