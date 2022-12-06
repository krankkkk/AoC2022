package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day6(override val day: Int = 6) : TimeCapturingTask<String, String> {

    override fun preparePart1Input(input: InputStream): String =
        input.bufferedReader().readText()

    override fun preparePart2Input(input: InputStream): String =
        preparePart1Input(input)

    override fun executePart1(input: String): Long =
        getMarker(4, input)

    private fun getMarker(packageSize: Int, input: String): Long =
        (packageSize until input.length)
            .first { input.substring(it - packageSize, it).toSet().size == packageSize }
            .toLong()

    override fun executePart2(input: String): Long =
        getMarker(14, input)

}
