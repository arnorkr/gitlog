package org.example

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.ul
import kotlinx.html.li
import kotlinx.html.head
import kotlinx.html.title
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8000) {
        routing {
            static {
                resource("static")
            }
            get("/") {
                val name = "Ktor"
                val gitLog = getGitLog()
                call.respondHtml(HttpStatusCode.OK) {
                    head {
                        title {
                            +name
                        }
                    }
                    body {
                        h1 { +"Git Repository Log" }
                        ul {
                            gitLog.forEach {
                                li { +it }
                            }
                        }
                    }
                }
            }
        }
    }.start(wait = true)
}

fun getGitLog(): List<String> {
    val process = ProcessBuilder("git", "log", "--pretty=format:%h %s").directory(File("/home/arnor/IdeaProjects/gitlog/gitlog")).start()
    val reader = process.inputStream.bufferedReader()
    return reader.readLines()
}