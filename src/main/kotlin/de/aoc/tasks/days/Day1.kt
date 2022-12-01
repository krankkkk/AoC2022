package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day1(override val day: Int = 1) : TimeCapturingTask<List<List<Long>>, List<List<Long>>> {

    override fun preparePart1Input(input: InputStream): List<List<Long>> {
        return input.use {
            val result = mutableListOf<List<Long>>()
            val temp = mutableListOf<String>()
            val lines = it.bufferedReader().lines()
            lines.forEach { line ->
                if (line.isBlank()) {
                    result.add(temp.map(String::toLong))
                    temp.clear()
                } else
                    temp.add(line)
            }
            if (temp.isNotEmpty())
                result.add(temp.map(String::toLong))
            result
        }
    }

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
