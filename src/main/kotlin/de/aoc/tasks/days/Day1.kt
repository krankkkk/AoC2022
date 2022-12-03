package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day1(override val day: Int = 1) : TimeCapturingTask<List<List<Long>>, List<List<Long>>> {

    override fun preparePart1Input(input: InputStream): List<List<Long>> =
        input.use { it.bufferedReader().readText() }
            .split(Regex("\n\n"))
            .map { it.split('\n').map(String::toLong) }

    override fun preparePart2Input(input: InputStream): List<List<Long>> =
        preparePart1Input(input)

    override fun executePart1(input: List<List<Long>>): Long =
        input.maxOf(List<Long>::sum)

    override fun executePart2(input: List<List<Long>>): Long =
        input.map(List<Long>::sum)
            .sortedDescending()
            .take(3)
            .sum()
}
