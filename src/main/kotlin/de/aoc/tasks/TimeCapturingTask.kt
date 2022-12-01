package de.aoc.tasks

import de.aoc.model.TimedResult
import de.aoc.utils.InputResolver
import java.io.InputStream
import java.time.Duration
import java.time.Instant

interface TimeCapturingTask<T, J> : Task<TimedResult> {

    override fun solvePart1(): TimedResult = part1(InputResolver.getInputForDay(day))

    override fun solvePart2(): TimedResult = part2(InputResolver.getInputForDay(day))

    fun preparePart1Input(input: InputStream): T
    fun preparePart2Input(input: InputStream): J

    fun executePart1(input: T): Long
    fun executePart2(input: J): Long

    fun part1(input: InputStream): TimedResult {
        val preparedInput = preparePart1Input(input)

        val start = Instant.now()
        val result = executePart1(preparedInput)
        val end = Instant.now()

        return TimedResult(result, Duration.between(start, end))
    }

    fun part2(input: InputStream): TimedResult {
        val preparedInput = preparePart2Input(input)

        val start = Instant.now()
        val result = executePart2(preparedInput)
        val end = Instant.now()

        return TimedResult(result, Duration.between(start, end))
    }
}
