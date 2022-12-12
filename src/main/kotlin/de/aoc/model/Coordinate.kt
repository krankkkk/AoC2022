package de.aoc.model

import kotlin.math.sign

data class Coordinate(
    /** y */
    val row: Int,
    /** x */
    val column: Int
) {

    fun move(dir: Direction, steps: Int = 1): Coordinate = dir.move(this, steps)

    fun inBounds(minRow: Int, maxRow: Int, minColl: Int, maxColl: Int) =
        (row in minRow..maxRow
                && column in minColl..maxColl)

    operator fun minus(p: Coordinate) = Coordinate(row - p.row, column - p.column)
    operator fun times(p: Coordinate) = Coordinate(row * p.row, column * p.column)
    operator fun plus(p: Coordinate) = Coordinate(row + p.row, column + p.column)
    fun abs() = Coordinate(kotlin.math.abs(row), kotlin.math.abs(column))
    fun max() = kotlin.math.max(row, column)
    fun min() = kotlin.math.min(row, column)
    fun sign() = Coordinate(row.sign, column.sign)
    fun manhanttan(other: Coordinate) =
        (this - other).abs().let { it.row + it.column }
}
