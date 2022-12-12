package de.aoc.tasks

import de.aoc.model.TimedResult
import de.aoc.utils.InputResolver
import mu.KLogging

class TaskVerifier(private val task: TimeCapturingTask<*, *>) : Task<TimedResult<*>> {

    private companion object : KLogging()

    override val day: Int
        get() = task.day

    override fun solvePart1(): TimedResult<*> {
        val (toVerify, _) = getExampleInput().use(task::part1)
        val expected = getExampleValue(1)

        if (toVerify != expected)
            error("Day $day : Part 1 example expected $expected but received $toVerify")
        else
            logger.info("Day $day : Part 1 example matched expectation")

        val actual = task.solvePart1()
        val expectedActual = getActualValue(1)
        if (expectedActual != null)
            if (actual.result != expectedActual)
                error("Day $day : Part 1 actual expected $expected but received $toVerify")
            else
                logger.info("Day $day : Part 1 actual matched expectation")

        return actual
    }

    override fun solvePart2(): TimedResult<*> {
        val (toVerify, _) = getExampleInput().use(task::part2)
        val expected = getExampleValue(2)

        if (toVerify != expected)
            error("Day $day : Part 2 example expected $expected but received $toVerify")
        else
            logger.info("Day $day : Part 2 example matched expectation")

        val actual = task.solvePart2()
        val expectedActual = getActualValue(2)
        if (expectedActual != null)
            if (actual.result != expectedActual)
                error("Day $day : Part 2 actual expected $expected but received $toVerify")
            else
                logger.info("Day $day : Part 2 actual matched expectation")

        return actual
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
            .let { if (it.matches(Regex("-?\\d+"))) it.toLong() else it }

    private fun getActualValue(part: Int) =
        javaClass
            .getResourceAsStream("/days/$day/results")!!
            .use { it.bufferedReader().readLines() }
            .find { it.startsWith("Part $part:") }!!
            .substringAfter("Part $part:")
            .trim()
            .let {
                if (it.isBlank()) return@let null
                if (it.matches(Regex("-?\\d+"))) it.toLong() else it
            }

}
