package de.aoc.utils

import java.io.InputStream

object InputResolver {

    fun getInputForDay(day: Int): InputStream =
        javaClass
        .getResourceAsStream("/days/$day/input")
            ?: error("Input for Day $day does not exist.")

    fun getExampleInputForDay(day: Int): InputStream =
        javaClass
        .getResourceAsStream("/days/$day/example")
            ?: error("Input for Day $day does not exist.")
}
