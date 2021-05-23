import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.serialization") version "1.5.0"
//    id("org.mikeneck.graalvm-native-image") version "1.4.0"
    id("com.palantir.graal") version "0.7.2"

    application
}

group = "com.charlyzzz"
version = "0.1"

val appMain = "MainKt"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
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

graal {
    graalVersion("21.1.0")
    javaVersion("11")
    outputName("awsetup")
    mainClass(appMain)
    option("--no-fallback")
}

//nativeImage {
//    graalVmHome = System.getenv("JAVA_HOME")
//    buildType {
//        it.executable(appMain)
//    }
//    executableName = "awsetup"
//    outputDirectory = file("$buildDir/bin")
//    arguments(
//        "--no-fallback",
//        "--enable-all-security-services",
//        "--report-unsupported-elements-at-runtime"
//    )
//}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}