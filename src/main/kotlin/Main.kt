package org.example

import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.io.File
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.li
import kotlinx.html.title
import kotlinx.html.ul

fun main() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    embeddedServer(Netty, port = readServerPort(config)) {
        routing {
            staticResources("static", "resources")
            get("/") {
                call.respondHtml(HttpStatusCode.OK) {
                    head { title { +"Git Log" } }
                    body {
                        h1 { +"Git Repository Log" }
                        ul { readGitLog(config).forEach { li { +it } } }
                    }
                }
            }
        }
    }.start(wait = true)
}

fun readServerPort(config: HoconApplicationConfig): Int {
    return try {
        config.property("server.port").getString().toIntOrNull() ?: 8080
    } catch (e: io.ktor.server.config.ApplicationConfigurationException) {
        println("Warning: server.port not found or invalid, using default port 8080.")
        8080
    }
}

fun readGitLog(config: HoconApplicationConfig): List<String> {
    return ProcessBuilder("git", "log", "--pretty=format:%h %s").directory(
        File(
            config.property("git.repo.path").getString()
        )
    ).start().inputStream.bufferedReader().readLines()
}
