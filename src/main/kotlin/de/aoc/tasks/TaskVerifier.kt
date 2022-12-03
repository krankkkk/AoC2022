package de.aoc.tasks

import de.aoc.model.TimedResult
import de.aoc.utils.InputResolver

class TaskVerifier(private val task: TimeCapturingTask<*, *>) : Task<TimedResult> {

    override val day: Int
        get() = task.day

    override fun solvePart1(): TimedResult {
        val (toVerify, _) = getExampleInput().use(task::part1)
        val expected = getExampleValue(1)

        if (toVerify != expected) {
            error("Expected $expected but received $toVerify")
        }

        return task.solvePart1()
    }

    override fun solvePart2(): TimedResult {
        val (toVerify, _) = getExampleInput().use(task::part2)
        val expected = getExampleValue(2)

        if (toVerify != expected) {
            error("Expected $expected but received $toVerify")
        }

        return task.solvePart2()
    }

    private fun getExampleInput() =
        InputResolver.getExampleInputForDay(day)

    private fun getExampleValue(part: Int) =
        javaClass
            .getResourceAsStream("/days/$day/results")!!
            .use { it.bufferedReader().readLines() }
            .find { it.startsWith("Example $part:") }!!
            .substringAfter("Example $part:")
            .trim()
            .toLong()

}
