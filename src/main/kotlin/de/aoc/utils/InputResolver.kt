package de.aoc.utils

import java.io.InputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.writeText

object InputResolver {

    fun getInputForDay(day: Int): InputStream =
        javaClass.getResourceAsStream("/days/$day/input")
            ?: fromLocalStorage(day)
            ?: fromServer(day)


    fun getExampleInputForDay(day: Int): InputStream =
        javaClass
            .getResourceAsStream("/days/$day/example")
            ?: error("Input for Day $day does not exist.")

    private fun fromLocalStorage(day: Int): InputStream? {
        val path = Path("src/main/resources/days/$day/input")
        return if (path.exists())
            path.inputStream()
        else
            null
    }

    private fun fromServer(day: Int): InputStream {
        return HttpClient
            .newHttpClient()
            .send(
                HttpRequest.newBuilder(
                    URI.create("https://adventofcode.com/2022/day/$day/input")
                ).GET()
                    .header("Cookie", "session=${getSessionCookie()}")
                    .build(),
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
            )
            .body()
            .also {
                //save to file so we won't ask again
                Path("src/main/resources/days/$day")
                    .createDirectories()
                    .resolve("input")
                    .writeText(it, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW)
            }
            .byteInputStream()
    }

    private fun getSessionCookie() =
        javaClass
            .getResourceAsStream("/session")!!
            .use(InputStream::readAllBytes)
            .toString(StandardCharsets.UTF_8)
}
