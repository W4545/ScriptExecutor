import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.json.JsonSlurper
import org.apache.groovy.json.internal.LazyMap
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI
import java.nio.file.Files
import java.util.*

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.github.hierynomus.license") version "0.16.1"
}

group = "dev.jacaro.mc"
version = "1.2.0-dev"

repositories {
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")
    //testCompile group: 'junit', name: 'junit', version: '4.12'
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

license {
    header = rootProject.file("HEADER")
    strictCheck = true
    exclude("**/*.yml")
    ext.set("year", Calendar.getInstance().get(Calendar.YEAR))
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
        archiveBaseName.set("ScriptExecutor")
        isEnableRelocation = true
        relocationPrefix = "scriptexecutor"
    }

    build {
        dependsOn("shadowJar")
    }

    named<ProcessResources>("processResources") {
        expand("version" to version)
    }

    register("installPlugin") {
        group = "server"
        dependsOn("build")

        delete("testServer/plugins/ScriptExecutor-$version.jar")
        doLast {
            copy {
                from("build/libs/ScriptExecutor-$version.jar")
                into("${project.property("test_server_subdir")}/plugins")
            }
        }
    }

    //TODO Remove casts and rework to be more safe
    @Suppress("UNCHECKED_CAST")
    register("installServer") {
        group = "server"

//        val semver = (project.property("minecraft_version") as String).split('.')
        val semver = (project.property("minecraft_version") as String)
        val channel = (project.property("channel") as String)

        val serverDir = File("${project.rootDir}/${project.property("test_server_subdir")}")
        val serverJar = File("${serverDir}/paper.jar")


        onlyIf {
            !serverJar.exists()
        }
        doFirst {
            serverDir.mkdirs()
            val baseUrl = "https://api.papermc.io/v2/projects/paper"
            val builds = URI("${baseUrl}/versions/$semver/builds").toURL()

            val latest = ((JsonSlurper().parseText(builds.readText()) as LazyMap)["builds"] as List<LazyMap>)
                .reversed()
                .find {
                    it["channel"] == channel
                }!!


            val fileName = ((latest["downloads"] as LazyMap)["application"] as LazyMap)["name"]

            val jarUrl = URI("${baseUrl}/versions/$semver/builds/${latest["build"]}/downloads/${fileName}").toURL()

            jarUrl.openStream().use { Files.copy(it, serverJar.toPath()) }
        }
    }
}
