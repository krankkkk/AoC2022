package de.aoc.tasks.days

import de.aoc.model.Coordinate
import de.aoc.model.Direction
import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day14(override val day: Int = 14) : TimeCapturingTask<Set<Coordinate>, Set<Coordinate>> {

    override fun preparePart1Input(input: InputStream): Set<Coordinate> {

        val filled = mutableSetOf<Coordinate>()
        input.bufferedReader()
            .lines()
            .forEach { line ->
                val coordinates = line
                    .split(" -> ")
                    .map {
                        it.split(',')
                            .map(String::toInt)
                            .let { l -> Coordinate(l[1], l[0]) }
                    }
                var last = coordinates.first()
                filled.add(last)

                coordinates
                    .drop(1)
                    .forEach { pos ->
                        //filling the Lines between the points
                        val diff = pos - last
                        IntRange(0, diff.abs().max())
                            .map { Coordinate(it, it) * diff.sign() + last }
                            .onEach(filled::add)
                        last = pos
                    }
            }
        return filled
    }

    override fun preparePart2Input(input: InputStream): Set<Coordinate> =
        preparePart1Input(input)

    override fun executePart1(input: Set<Coordinate>): Long {
        val floor = input.maxOf { it.row } + 1
        return dropSand(input, floor) {
            it.row + 1 >= floor
        }
    }

    override fun executePart2(input: Set<Coordinate>): Long {
        val floor = input.maxOf { it.row } + 2
        val finalCoord = Coordinate(0, 500)
        return dropSand(input, floor) {
            it == finalCoord
        } + 1
    }

    private fun dropSand(
        input: Set<Coordinate>,
        floor: Int,
        debugPrint: Boolean = false,
        finisher: (Coordinate) -> Boolean,
    ): Long {
        val filledCoords = mutableSetOf<Coordinate>()

        fun canMove(sand: Coordinate) =
            sand.row != floor && sand !in filledCoords && sand !in input

        val posDirections = listOf(Direction.DOWN, Direction.RIGHT_DOWN, Direction.LEFT_DOWN)
            .map(Direction::invert)

        for (tick in 0L..Long.MAX_VALUE) {
            var sand = Coordinate(0, 500)
            while (true) {
                sand = posDirections.map { it.move(sand) }
                    .firstOrNull(::canMove) ?: break
            }

            if (debugPrint)
                print(input, filledCoords)

            if (finisher(sand)) {
                return tick
            }

            filledCoords += sand
        }

        error("How did we even get here?")
    }

    private fun print(blocks: Set<Coordinate>, sand: Set<Coordinate>) {
        println()
        IntRange(0, blocks.maxOf { it.row } - 1)
            .map { row -> row to IntRange((blocks + sand).minOf { it.column } - 1, (blocks + sand).maxOf { it.column } + 1) }
            .onEach { (row, collumns) ->
                collumns.map { coll ->
                    if (Coordinate(row, coll) in sand)
                        '°'
                    else if (Coordinate(row, coll) in blocks)
                        '#'
                    else
                        '.'
                }.joinToString(prefix = "$row ", separator = "")
                    .also(::println)
            }
    }

}
