package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day3(override val day: Int = 3) : TimeCapturingTask<List<String>, List<String>> {

    private companion object {
        private const val GROUP_SIZE = 3
    }

    override fun preparePart1Input(input: InputStream): List<String> =
        input.bufferedReader().lines().toList()

    override fun preparePart2Input(input: InputStream): List<String> =
        preparePart1Input(input)

    override fun executePart1(input: List<String>): Long =
        input
            .flatMap {
                listOf(it.substring(0, it.length / 2), it.substring(it.length / 2))
                    .map(String::toSet)
                    .reduce(Set<Char>::intersect)
            }
            .sumOf(::toPriority)


    override fun executePart2(input: List<String>): Long =
        input
            .chunked(GROUP_SIZE)
            .flatMap { it.map(String::toSet).reduce(Set<Char>::intersect) }
            .sumOf(::toPriority)

    private fun toPriority(it: Char) =
        if (it.isUpperCase())
            it.minus('A') + 27L
        else
            it.minus('a') + 1L

}
