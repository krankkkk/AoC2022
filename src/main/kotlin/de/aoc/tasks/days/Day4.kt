package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day4(override val day: Int = 4) : TimeCapturingTask<List<String>, List<String>> {

    override fun preparePart1Input(input: InputStream): List<String> =
        input.bufferedReader().readLines()

    override fun preparePart2Input(input: InputStream): List<String> =
        preparePart1Input(input)

    override fun executePart1(input: List<String>): Long =
        input.map { it.toPair() }
            .map { (l, r) -> l.toRange() to r.toRange() }
            .count { (l, r) -> l.fullyContains(r) || r.fullyContains(l) }
            .toLong()

    override fun executePart2(input: List<String>): Long =
        input.map { it.toPair() }
            .map { (l, r) -> l.toRange() to r.toRange() }
            .count { (l, r) -> l.hasOverlap(r) }
            .toLong()

    private fun String.toPair(): Pair<String, String> =
        indexOf(',')
            .let { substring(0, it) to substring(it + 1) }

    private fun String.toRange(): IntRange =
        indexOf('-')
            .let { IntRange(substring(0, it).toInt(), substring(it + 1).toInt()) }

    private fun ClosedRange<Int>.fullyContains(other: ClosedRange<Int>): Boolean =
        contains(other.start) && contains(other.endInclusive)

    private fun ClosedRange<Int>.hasOverlap(other: ClosedRange<Int>): Boolean =
        contains(other.start) || other.contains(start)

}
