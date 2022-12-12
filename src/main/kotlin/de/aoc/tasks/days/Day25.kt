package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream
import kotlin.math.pow

class Day25(override val day: Int = 25) : TimeCapturingTask<List<String>, List<String>> {

    override fun preparePart1Input(input: InputStream): List<String> =
        input.bufferedReader()
            .readLines()
            .map(String::reversed)

    override fun preparePart2Input(input: InputStream): List<String> = preparePart1Input(input)

    override fun executePart1(input: List<String>): String {
        var total: Long = input.sumOf { line ->
            line.mapIndexed { index, c ->
                ("=-012".indexOf(c) - 2) * 5.0.pow(index).toLong()
            }.sum()
        }

        var output = ""

        while (total > 0) {
            val rem = total.mod(5)
            total /= 5

            if (rem <= 2)
                output = "$rem$output"
            else {
                output = "   =-"[rem] + output
                total++
            }
        }

        return output
    }

    override fun executePart2(input: List<String>): Long = -1
}
