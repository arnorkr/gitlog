package org.example

import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.io.File
import kotlinx.serialization.Serializable

@Serializable
data class GitLogEntry(val message: String)

fun Application.module() {
    install(ContentNegotiation) { json() }
    routing {
        staticResources("/", "static")
        get("/gitlog") {
            try {
                call.respond(getGitLog().map { GitLogEntry(it) })
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage)
            }
        }
    }
}

fun main() {
    embeddedServer(Netty, port = getServerPort(), module = Application::module).start(wait = true)
}

fun getServerPort(): Int {
    return try {
        HoconApplicationConfig(ConfigFactory.load()).property("server.port").getString().toIntOrNull() ?: 8080
    } catch (e: io.ktor.server.config.ApplicationConfigurationException) {
        println("Warning: server.port not found or invalid, using default port 8080.")
        8080
    }
}

fun getGitLog(): List<String> {
    return ProcessBuilder("git", "log", "--pretty=format:%h %s").directory(
        File(
            HoconApplicationConfig(ConfigFactory.load()).property("git.repo.path").getString()
        )
    ).start().inputStream.bufferedReader().readLines()
}
