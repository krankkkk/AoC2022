package de.aoc.model

import kotlin.math.sign

data class CubeCoordinate(
    /** x */
    val column: Int,
    /** y */
    val row: Int,
    /** z */
    val height: Int
) {

    fun inBounds(minRow: Int, maxRow: Int, minColl: Int, maxColl: Int, minHeight: Int, maxHeight: Int) =
        (row in minRow..maxRow
                && column in minColl..maxColl
                && height in minHeight..maxHeight)

    operator fun minus(p: CubeCoordinate) = CubeCoordinate(column - p.column, row - p.row, height - p.height)
    operator fun times(p: CubeCoordinate) = CubeCoordinate(column * p.column, row * p.row, height * p.height)
    operator fun plus(p: CubeCoordinate) = CubeCoordinate(column + p.column, row + p.row, height + p.height)
    fun abs() = CubeCoordinate(kotlin.math.abs(column), kotlin.math.abs(row), kotlin.math.abs(height))
    fun max() = maxOf(row, column, height)
    fun min() = minOf(row, column, height)
    fun sign() = CubeCoordinate(column.sign, row.sign, height.sign)
    fun manhanttan(other: CubeCoordinate) =
        (this - other).abs().let { it.row + it.column + it.height }
}
