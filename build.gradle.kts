plugins {
    kotlin("jvm") version "2.2.20"
    id("com.gradleup.shadow") version "9.2.2"
    id("xyz.jpenilla.run-paper") version "3.1.0"
}

group = "net.ruvios"
version = "0.1.1"

base {
    archivesName.set("bgb_gui")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.opencollab.dev/main/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://repo.skriptlang.org/releases")
}

dependencies {
    // 1.21.11-API + Java 21 → läuft auf 1.21.11 und neuer (auch Paper 26.2)
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("org.geysermc.floodgate:api:2.2.4-SNAPSHOT")
    compileOnly("com.github.SkriptLang:Skript:2.12.2") {
        isTransitive = false
    }
    implementation(kotlin("stdlib"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        javaParameters.set(true)
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    processResources {
        filteringCharset = "UTF-8"
        val props = mapOf(
            "version" to version,
            "name" to "BGB_GUI",
        )
        filesMatching(listOf("paper-plugin.yml", "plugin.yml")) {
            expand(props)
        }
    }

    shadowJar {
        archiveClassifier.set("")
        mergeServiceFiles()
    }

    named<Jar>("jar") {
        archiveClassifier.set("plain")
    }

    build {
        dependsOn(shadowJar)
    }

    runServer {
        minecraftVersion("26.2")
        jvmArgs("-Dfile.encoding=UTF-8")
    }
}

runPaper.folia.registerTask {
    minecraftVersion("26.2")
    jvmArgs("-Dfile.encoding=UTF-8")
}
