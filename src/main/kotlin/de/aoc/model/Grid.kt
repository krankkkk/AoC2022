package de.aoc.model

class Grid<T>(list: Iterable<Iterable<T>>) : ArrayList<MutableList<T>>(list.map(Iterable<T>::toMutableList)) {

    private val innerSize = first().size

    fun coordinates(): List<Coordinate> =
        IntRange(0, size - 1)
            .map { it to IntRange(0, innerSize - 1) }
            .flatMap { (row, columns) -> columns.map { Coordinate(row, it) } }

    fun isAtEdge(coordinate: Coordinate): Boolean =
        coordinate.row == 0 || coordinate.row == size - 1
                || coordinate.column == 0 || coordinate.column == innerSize - 1

    operator fun get(coordinate: Coordinate): T =
        this[coordinate.row][coordinate.column]

    fun isValid(coordinate: Coordinate): Boolean =
        coordinate.row in 0 until size
                && coordinate.column in 0 until innerSize

    override fun toString(): String =
        joinToString(separator = "\n") { it.joinToString(separator = "") }

    operator fun set(coordinate: Coordinate, value: T) {
        check(isValid(coordinate)) {
            "Coordinates not in bound." +
                    " rows:$size columns:$innerSize requested:$coordinate"
        }
        this[coordinate.row][coordinate.column] = value
    }
}
