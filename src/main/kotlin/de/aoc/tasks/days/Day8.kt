package de.aoc.tasks.days

import de.aoc.model.Coordinate
import de.aoc.model.Direction
import de.aoc.model.Grid
import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day8(override val day: Int = 8) : TimeCapturingTask<Grid<Int>, Grid<Int>> {

    override fun preparePart1Input(input: InputStream): Grid<Int> =
        input.bufferedReader()
            .lines()
            .map { it.toCharArray().map(Char::digitToInt) }
            .toList()
            .let(::Grid)

    override fun preparePart2Input(input: InputStream): Grid<Int> = preparePart1Input(input)

    override fun executePart1(input: Grid<Int>): Long =
        input.coordinates()
            .count(input::isTreeVisible)
            .toLong()


    override fun executePart2(input: Grid<Int>): Long =
        input.coordinates()
            .filter { !input.isAtEdge(it) && input.isTreeVisible(it) }
            .maxOf(input::getScore)
}

private fun Grid<Int>.getScore(coordinate: Coordinate): Long =
    Direction.values()
        .map { it.filter(this, coordinate).countVisibleTreesInSight(get(coordinate)) }
        .reduce { acc, l -> acc * l }
        .toLong()


private fun Grid<Int>.isTreeVisible(direction: Direction, coordinate: Coordinate): Boolean =
    direction.filter(this, coordinate)
        .all { it < get(coordinate) }


private fun List<Int>.countVisibleTreesInSight(currentHeight: Int): Int {
    var seen = 0
    for (heights in this) {
        seen++
        if (heights >= currentHeight) {
            break
        }
    }
    return seen
}

private fun Grid<Int>.isTreeVisible(coordinate: Coordinate): Boolean =
    isAtEdge(coordinate) || Direction.values().any { isTreeVisible(it, coordinate) }


