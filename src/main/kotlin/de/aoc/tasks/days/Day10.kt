package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream
import kotlin.math.abs

private const val CHARS_PER_LINE = 40
private const val MAX_LINES_INDEX = 5

class Day10(override val day: Int = 10) : TimeCapturingTask<List<String>, List<String>> {

    override fun preparePart1Input(input: InputStream): List<String> = input.bufferedReader()
        .lines()
        .toList()

    override fun preparePart2Input(input: InputStream): List<String> = preparePart1Input(input)

    override fun executePart1(input: List<String>): Long {
        var score = 0L
        var register = 1L
        var cycle = 0

        input.forEach { modifier ->
            if (modifier == "noop") {
                score += handleTick1(++cycle, register)
            } else {
                score += handleTick1(++cycle, register)
                score += handleTick1(++cycle, register)
                register += modifier.substringAfter(" ").toLong()
            }
        }

        return score
    }

    private fun handleTick1(cycle: Int, register: Long): Long =
        if ((cycle - CHARS_PER_LINE / 2) % CHARS_PER_LINE == 0)
            (register * cycle)
                .also { println(it) }
        else
            0

    override fun executePart2(input: List<String>): Long {
        val result = IntRange(0, MAX_LINES_INDEX)
            .map { StringBuilder().append("?".repeat(CHARS_PER_LINE)) }

        var register = 1L
        var cycle = 0

        input.map { modifier ->
            if (modifier == "noop") {
                handleTick2(++cycle, register, result)
            } else {
                handleTick2(++cycle, register, result)
                handleTick2(++cycle, register, result)
                register += modifier.substringAfter(" ").toLong()
            }
        }

        result.forEach(::println)
        return 1
    }

    private fun handleTick2(cycle: Int, register: Long, result: List<StringBuilder>) {
        val c1 = cycle - 1
        result[c1 / CHARS_PER_LINE][c1 % CHARS_PER_LINE] = if (abs(register - (c1 % CHARS_PER_LINE)) <= 1)
            '#'
        else
            ' '
    }


}
