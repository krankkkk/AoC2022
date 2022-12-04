package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream
import java.util.stream.IntStream
import kotlin.streams.toList

class Day4(override val day: Int = 4) : TimeCapturingTask<List<String>, List<String>> {

    override fun preparePart1Input(input: InputStream): List<String> =
        input.bufferedReader().readLines()

    override fun preparePart2Input(input: InputStream): List<String> =
        preparePart1Input(input)

    override fun executePart1(input: List<String>): Long =
        input.map { it.split(Regex(",")) }
            .map { it[0].toRange() to it[1].toRange() }
            .count { (l, r) -> l.intersect(r.toSet()).size.let { it == l.size || it == r.size } }
            .toLong()

    override fun executePart2(input: List<String>): Long =
        input.map { it.split(Regex(",")) }
            .map { it[0].toRange() to it[1].toRange() }
            .count { (l, r) -> l.intersect(r.toSet()).isNotEmpty() }
            .toLong()

}

private fun String.toRange(): List<Int> =
    this.split(Regex("-"))
        .let { IntStream.rangeClosed(it[0].toInt(), it[1].toInt()).toList() }
