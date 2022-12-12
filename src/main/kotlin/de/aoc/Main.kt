package de.aoc

import de.aoc.utils.TaskResolver
import mu.KLogging
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

object Main : KLogging()

fun main(args: Array<String>) {
    dummyRun()
    val dayToRun = args.firstOrNull()?.toInt()
        ?: LocalDateTime.now(Clock.system(ZoneOffset.of("-6"))).dayOfMonth

    val task = TaskResolver.getTaskForDay(dayToRun)
    val firstResult = runCatching(task::solvePart1)
    val secondResult = runCatching(task::solvePart2)

    val logger = Main.logger
    listOf(firstResult, secondResult)
        .onEach { it.onFailure { err -> logger.error(err.message, err) } }

    val first = firstResult.getOrNull()
    val second = secondResult.getOrNull()

    val part1Readable = first?.time?.let(::humanReadableFormat) ?: "error"
    val part2Readable = second?.time?.let(::humanReadableFormat) ?: "error"
    val totalReadable =
        if (first?.time != null && second?.time != null)
            first.time.plus(second.time).let(::humanReadableFormat)
        else "error"

    logger.info("Day ${task.day} -> Part 1: ${first?.result}   Part 2: ${second?.result}")
    logger.info("Part 1 took $part1Readable")
    logger.info("Part 2 took $part2Readable")
    logger.info("Total took $totalReadable")
}

fun dummyRun() {
    Main.logger.info { "Dummy Run for Warmup" }
    TaskResolver.getTaskForDay(1)
        .runCatching {
            solvePart1()
            solvePart2()
        }
    Main.logger.info { "Dummy Run is done" }
}

fun humanReadableFormat(duration: Duration): String =
    duration.toString()
        .substring(2)
        .replace("(\\d[HMS])(?!$)".toRegex(), "$1 ")
        .lowercase(Locale.getDefault())
