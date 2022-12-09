package de.aoc.tasks.days

import de.aoc.model.Coordinate
import de.aoc.model.Direction
import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream
import kotlin.math.abs
import kotlin.math.max

class Day9(override val day: Int = 9) : TimeCapturingTask<List<Day9.Move>, List<Day9.Move>> {

    override fun preparePart1Input(input: InputStream): List<Move> =
        input.bufferedReader()
            .lines()
            .map { Move(dir(it.substringBefore(" ")), it.substringAfter(" ").toInt()) }
            .toList()

    private fun dir(dirShort: String): Direction =
        when (dirShort) {
            "R" -> Direction.RIGHT
            "L" -> Direction.LEFT
            "U" -> Direction.UP
            "D" -> Direction.DOWN
            else -> error("Unknown dir $dirShort")
        }

    override fun preparePart2Input(input: InputStream): List<Move> = preparePart1Input(input)


    private inline fun dragKnot(lastKnot: Coordinate, thisKnot: Coordinate): Coordinate {
        val steps = distance(lastKnot, thisKnot) - 1
        var temp: Coordinate = thisKnot

        val dx = lastKnot.column - thisKnot.column
        val dy = lastKnot.row - thisKnot.row
        if (dx != 0) {
            temp = if (dx > 0) {
                Direction.RIGHT.move(temp, steps)
            } else {
                Direction.LEFT.move(temp, steps)
            }
        }

        if (dy != 0) {
            temp = if (dy > 0) {
                Direction.UP.move(temp, steps)
            } else {
                Direction.DOWN.move(temp, steps)
            }
        }

        return temp
    }

    override fun executePart1(input: List<Move>): Long =
        knotsToCount(input, 1)

    private fun knotsToCount(input: List<Move>, extraKnots: Int): Long {
        val knots = mutableMapOf<Int, Coordinate>()
            .apply {
                put(0, Coordinate(0, 0))
                repeat(extraKnots) {
                    put(it + 1, Coordinate(0, 0))
                }
            }
        val everyTailPos = mutableSetOf<Coordinate>()

        input.forEach { move ->
            repeat(move.steps) {
                knots[0] = move.direction.move(knots[0]!!)

                for (knot in 1..extraKnots) {
                    val lastKnot = knots[knot - 1]!!
                    val thisKnot = knots[knot]!!
                    val newPos = dragKnot(lastKnot, thisKnot)
                    if (lastKnot == thisKnot) break

                    knots[knot] = newPos
                }

                everyTailPos.add(knots[extraKnots]!!)
            }
        }

        return everyTailPos.size.toLong()
    }

    override fun executePart2(input: List<Move>): Long =
        knotsToCount(input, 9)

    data class Move(val direction: Direction, val steps: Int)

    fun distance(previous: Coordinate, current: Coordinate): Int =
        max(abs(previous.row - current.row), abs(previous.column - current.column))

}
