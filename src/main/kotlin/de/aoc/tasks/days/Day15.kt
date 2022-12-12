package de.aoc.tasks.days

import de.aoc.model.Coordinate
import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream
import kotlin.math.abs

class Day15(override val day: Int = 15) : TimeCapturingTask<List<Day15.InputWrapper>, List<Day15.InputWrapper>> {

    private companion object {
        private val NUMBER_RX = Regex("-?\\d+")
    }

    override fun preparePart1Input(input: InputStream): List<InputWrapper> =
        input.bufferedReader()
            .lines()
            .map { NUMBER_RX.findAll(it).map(MatchResult::groupValues).flatMap { it.map(String::toInt) }.toList() }
            .map { InputWrapper(Coordinate(it[1], it[0]), Coordinate(it[3], it[2])) }
            .toList()

    override fun preparePart2Input(input: InputStream): List<InputWrapper> =
        preparePart1Input(input)

    override fun executePart1(input: List<InputWrapper>): Long {
        // in the example we check the row 10 instead of Y
        val rowToCheck = if (input.size == 14) 10 else 2_000_000

        return findImplausibleCoords(input, rowToCheck)
            .count()
            .toLong()
    }

    private fun findImplausibleCoords(
        input: List<InputWrapper>,
        rowToCheck: Int
    ): List<Coordinate> {

        val sensors = input.map { it.sensor }
        val minColl = sensors.minOf { it.column }
        val maxColl = sensors.maxOf { it.column }


        val sensorAndBeacons = input.flatMap { listOf(it.sensor, it.beacon).toSet() }
        val maxDistance = input.maxOf { it.beacon.manhanttan(it.sensor) }

        return IntRange(minColl - maxDistance, maxColl + maxDistance)
            .map { coll -> Coordinate(rowToCheck, coll) }
            .filterNot(sensorAndBeacons::contains)
            .filter {
                input.any { wrapper ->
                    val distToBeacon = wrapper.beacon.manhanttan(wrapper.sensor)
                    val distToColl = wrapper.sensor.manhanttan(it)

                    distToColl <= distToBeacon
                }
            }
    }

    override fun executePart2(input: List<InputWrapper>): Long {
        val max = if (input.size == 14) 20 else 4_000_000
        for (row in 0..max) {
            var column = 0
            while (column < max) {
                var foundNext = false
                for ((sensor, beacon) in input) {
                    val dist = sensor.manhanttan(beacon)
                    val minDist = sensor.column - (dist - abs(sensor.row - row))
                    val maxDist = sensor.column + (dist - abs(sensor.row - row))
                    if (minDist < maxDist && column in minDist..maxDist) {
                        column = maxDist + 1
                        foundNext = true
                        break
                    }
                }
                if (!foundNext)
                    return column * 4_000_000L + row
            }

        }
        error("")
    }


    data class InputWrapper(
        val sensor: Coordinate,
        val beacon: Coordinate
    )
}
