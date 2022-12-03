package de.aoc

import de.aoc.utils.TaskResolver
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

fun main(args: Array<String>) {
    val dayToRun = args.firstOrNull()?.toInt()
        ?: LocalDateTime.now(Clock.system(ZoneOffset.of("-6"))).dayOfMonth

    val task = TaskResolver.getTaskForDay(dayToRun)
    val firstResult = runCatching(task::solvePart1)
    val secondResult = runCatching(task::solvePart2)

    listOf(firstResult, secondResult)
        .onEach { it.onFailure(Throwable::printStackTrace) }

    val first = firstResult.getOrNull()
    val second = secondResult.getOrNull()

    val part1Readable = first?.time?.let(::humanReadableFormat) ?: "error"
    val part2Readable = second?.time?.let(::humanReadableFormat) ?: "error"
    val totalReadable =
        if (first?.time != null && second?.time != null)
            first.time.plus(second.time).let(::humanReadableFormat)
        else "error"

    println("Day ${task.day} -> Part 1: ${first?.result}   Part 2: ${second?.result}")
    println("Part 1 took $part1Readable")
    println("Part 2 took $part2Readable")
    println("Total took $totalReadable")
}

fun humanReadableFormat(duration: Duration): String =
    duration.toString()
        .substring(2)
        .replace("(\\d[HMS])(?!$)".toRegex(), "$1 ")
        .lowercase(Locale.getDefault())
