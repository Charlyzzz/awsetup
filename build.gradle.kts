import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.serialization") version "1.5.0"
    id("com.palantir.graal") version "0.7.2"

    application
}

group = "com.charlyzzz"
version = "0.1"

val appMain = "HashKt"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

application {
    mainClass.set(appMain)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

val binaryName = "awsetup"

graal {
    graalVersion("21.1.0")
    javaVersion("11")
    outputName(binaryName)
    mainClass(appMain)
    option("--no-fallback")
    option("-H:ReflectionConfigurationFiles=/Users/charlyzzz/Documents/Kotlin/awsetup/configs/reflect.json")
}

tasks.register<Zip>("release") {
    dependsOn("nativeImage")
    archiveFileName.set("release.zip")
    destinationDirectory.set(layout.buildDirectory.dir("dist"))
    from(layout.buildDirectory.file("graal/$binaryName"))
}