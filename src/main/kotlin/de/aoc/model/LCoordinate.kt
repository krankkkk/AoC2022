package de.aoc.model

import kotlin.math.sign

data class LCoordinate(
    /** y */
    val row: Long,
    /** x */
    val column: Long
) {

    fun inBounds(minRow: Int, maxRow: Int, minColl: Int, maxColl: Int) =
        (row in minRow..maxRow
                && column in minColl..maxColl)

    operator fun minus(p: LCoordinate) = LCoordinate(row - p.row, column - p.column)
    operator fun times(p: LCoordinate) = LCoordinate(row * p.row, column * p.column)
    operator fun plus(p: LCoordinate) = LCoordinate(row + p.row, column + p.column)
    fun abs() = LCoordinate(kotlin.math.abs(row), kotlin.math.abs(column))
    fun max() = kotlin.math.max(row, column)
    fun min() = kotlin.math.min(row, column)
    fun sign() = LCoordinate(row.sign.toLong(), column.sign.toLong())
    fun manhanttan(other: LCoordinate) =
        (this - other).abs().let { it.row + it.column }
}
