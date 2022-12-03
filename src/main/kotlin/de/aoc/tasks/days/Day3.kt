package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day3(override val day: Int = 3) : TimeCapturingTask<List<String>, List<String>> {

    override fun preparePart1Input(input: InputStream): List<String> {
        return input.bufferedReader().lines().toList()
    }

    override fun preparePart2Input(input: InputStream): List<String> {
        return preparePart1Input(input)
    }

    override fun executePart1(input: List<String>): Long {
        return input
            .map { it.substring(0, it.length / 2) to it.substring(it.length / 2) }
            .map { (l, r) -> l.groupBy { it }.keys to r.groupBy { it }.keys }
            .map { (l, r) -> l.first(r::contains) }
            .sumOf(::toPriority)

    }


    override fun executePart2(input: List<String>): Long {
        return input
            .chunked(3)
            .map { groups ->
                groups
                    .map { elf -> elf.groupBy { it }.keys }
                    .reduce { acc, b -> acc.filter(b::contains).toSet() }
                    .first()
            }
            .sumOf(::toPriority)
    }

    private fun toPriority(it: Char) =
        if (it.isUpperCase())
            it.minus('A') + 27L
        else
            it.minus('a') + 1L

}
